package org.devocative.wickomp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.IWebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.WebSocketResponse;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.registry.PageIdKey;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.devocative.adroit.AdroitList;
import org.devocative.wickomp.async.WebSocketBroadcastMessage;
import org.devocative.wickomp.async.WebSocketToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

/*
 * Some useful annotations:
 *
 * @JsonIgnore
 *
 * @JsonInclude(JsonInclude.Include.NON_NULL)
 *
 * @JsonRawValue
 *
 * @JsonProperty("<as property name>")
 *
 * @JsonValue
 */
public class WebUtil {
	private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

	// ------------------------------

	public static String toJson(Object obj) {
		StringWriter sw = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		try {
			mapper.writeValue(sw, obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}

	public static <T> T fromJson(String json, Class<T> cls) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return mapper.readValue(json, cls);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			return mapper.readValue(json, typeReference);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeJQueryCall(String script, boolean decorateWithInit) {
		AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
		if (target != null) {
			target.appendJavaScript(script);
		} else {
			Response response = RequestCycle.get().getResponse();
			if (decorateWithInit) {
				response.write(String.format("<script>$(function(){%s});</script>", script));
			} else {
				response.write(String.format("<script>%s</script>", script));
			}
		}
	}

	// ---------------

	public static Map<String, List<String>> toMap(boolean ignoreCase, boolean ignoreEmpty) {
		return toMap(ignoreCase, Collections.<String>emptyList(), ignoreEmpty, Collections.<String>emptyList());
	}

	public static Map<String, List<String>> toMap(boolean ignoreCase, boolean ignoreEmpty, List<String> ignoreValues) {
		return toMap(ignoreCase, Collections.<String>emptyList(), ignoreEmpty, ignoreValues);
	}

	public static Map<String, List<String>> toMap(boolean ignoreCase, List<String> ignoreParams, boolean ignoreEmpty) {
		return toMap(ignoreCase, ignoreParams, ignoreEmpty, Collections.<String>emptyList());
	}

	// Main toMap without IRequestParameters
	public static Map<String, List<String>> toMap(boolean ignoreCase, List<String> ignoreParams, boolean ignoreEmpty, List<String> ignoreValues) {
		return toMap(getRequestParameters(), ignoreCase, ignoreParams, ignoreEmpty, ignoreValues);
	}

	// Main toMap with IRequestParameters
	public static Map<String, List<String>> toMap(IRequestParameters parameters, boolean ignoreCase, List<String> ignoreParams, boolean ignoreEmptyValue, List<String> ignoreValues) {
		Map<String, List<String>> result = ignoreCase ?
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER) :
			new HashMap<>();

		if (ignoreCase) {
			ignoreParams = new AdroitList<>(ignoreParams)
				.setComparator(String.CASE_INSENSITIVE_ORDER);

			ignoreValues = new AdroitList<>(ignoreValues)
				.setComparator(String.CASE_INSENSITIVE_ORDER);
		}

		for (String param : parameters.getParameterNames()) {
			List<StringValue> parameterValues = parameters.getParameterValues(param);

			if (ignoreParams.contains(param)) {
				continue;
			}

			List<String> values = ignoreCase ?
				new AdroitList<>(String.CASE_INSENSITIVE_ORDER) :
				new ArrayList<>();

			for (StringValue paramValue : parameterValues) {
				if (ignoreValues.contains(paramValue.toString())) {
					continue;
				}

				if (ignoreEmptyValue) {
					if (!paramValue.isEmpty()) {
						values.add(paramValue.toString());
					}
				} else {
					values.add(paramValue.toString());
				}
			}

			if (values.size() > 0) {
				if (result.containsKey(param)) {
					result.get(param).addAll(values);
				} else {
					result.put(param, values);
				}
			}
		}
		return result;
	}

	public static Map<String, List<String>> toMap(String paramsAsUrl, boolean ignoreCase, boolean ignoreEmpty) {
		Map<String, List<String>> result = ignoreCase ?
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER) :
			new HashMap<>();

		String[] paramValueArr = paramsAsUrl.split("[&]");

		for (String paramValue : paramValueArr) {
			int i = paramValue.indexOf('=');
			if (i > 0) {
				String param = paramValue.substring(0, i);
				String value = paramValue.substring(i + 1);

				if (!result.containsKey(param)) {
					ArrayList<String> list = ignoreCase ?
						new AdroitList<>(String.CASE_INSENSITIVE_ORDER) :
						new ArrayList<>();
					result.put(param, list);
				}

				List<String> values = result.get(param);
				if (!ignoreEmpty || !value.isEmpty()) {
					values.add(value);
				}
			}
		}

		if (ignoreEmpty) {
			HashSet<String> keys = new HashSet<>(result.keySet());
			for (String key : keys) {
				if (result.get(key).size() == 0) {
					result.remove(key);
				}
			}
		}
		return result;
	}

	public static Map<String, List<String>> toMap(HttpServletRequest request, boolean ignoreCase, boolean ignoreEmptyValue) {
		return toMap(request, ignoreCase, Collections.<String>emptyList(), ignoreEmptyValue, Collections.<String>emptyList());
	}

	// Main toMap with HttpServletRequest
	public static Map<String, List<String>> toMap(HttpServletRequest request, boolean ignoreCase, List<String> ignoreParams, boolean ignoreEmptyValue, List<String> ignoreValues) {
		Map<String, List<String>> result = ignoreCase ?
			new TreeMap<>(String.CASE_INSENSITIVE_ORDER) :
			new HashMap<>();

		if (ignoreCase) {
			ignoreParams = new AdroitList<>(ignoreParams)
				.setComparator(String.CASE_INSENSITIVE_ORDER);

			ignoreValues = new AdroitList<>(ignoreValues)
				.setComparator(String.CASE_INSENSITIVE_ORDER);
		}

		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String param = enumeration.nextElement();
			String[] parameterValues = request.getParameterValues(param);

			if (ignoreParams.contains(param)) {
				continue;
			}

			List<String> values = ignoreCase ?
				new AdroitList<>(String.CASE_INSENSITIVE_ORDER) :
				new ArrayList<>();

			for (String paramValue : parameterValues) {
				if (ignoreValues.contains(paramValue)) {
					continue;
				}

				if (ignoreEmptyValue) {
					if (!paramValue.isEmpty()) {
						values.add(paramValue);
					}
				} else {
					values.add(paramValue);
				}
			}

			if (values.size() > 0) {
				if (result.containsKey(param)) {
					result.get(param).addAll(values);
				} else {
					result.put(param, values);
				}
			}
		}
		return result;
	}

	// ---------------

	public static Set<String> toSet(boolean ignoreCase) {
		return toSet(getRequestParameters(), ignoreCase);
	}

	public static Set<String> toSet(IRequestParameters parameters, boolean ignoreCase) {
		Set<String> result = ignoreCase ?
			new TreeSet<>(String.CASE_INSENSITIVE_ORDER) :
			new HashSet<>();

		for (String param : parameters.getParameterNames()) {
			result.add(param);
		}
		return result;
	}

	// ---------------

	public static List<String> listOf(String param, boolean ignoreCase) {
		return listOf(getRequestParameters(), param, ignoreCase);
	}

	public static List<String> listOf(IRequestParameters parameters, String param, boolean ignoreCase) {
		List<String> result = ignoreCase ?
			new AdroitList<>(String.CASE_INSENSITIVE_ORDER) :
			new ArrayList<>();

		List<StringValue> parameterValues = parameters.getParameterValues(param);
		if (parameterValues != null && parameterValues.size() > 0) {
			for (StringValue parameterValue : parameterValues) {
				if (!parameterValue.isEmpty()) {
					result.add(parameterValue.toString());
				}
			}
		}

		return result;
	}

	// ---------------

	public static String getStringOfResource(String resourceKey, String defaultValue) {
		return getStringOfResource(resourceKey, Model.of(defaultValue));
	}

	public static String getStringOfResource(String resourceKey, IModel<String> defaultValue) {
		return Application.get()
			.getResourceSettings()
			.getLocalizer()
			.getString(resourceKey, null, null, null, null, defaultValue);
	}

	// ---------------

	public static boolean isWebSocketRequest(RequestCycle cycle) {
		IWebSocketRequestHandler webSocketRequestHandler = cycle.find(IWebSocketRequestHandler.class);
		return webSocketRequestHandler != null;
	}

	public static boolean isWebSocketResponse(RequestCycle cycle) {
		Response response = cycle.getResponse();
		return response instanceof WebSocketResponse;
	}

	public static boolean isAjaxRequest(RequestCycle cycle) {
		AjaxRequestTarget target = cycle.find(AjaxRequestTarget.class);
		return target != null;
	}

	// ---------------

	public static WebSocketToken createWSToken(Component cmp) {
		return new WebSocketToken(
			Application.get().getApplicationKey(),
			WebSession.get().getId(),
			new PageIdKey(cmp.getPage().getPageId())
		);
	}

	public static boolean wsPush(WebSocketToken token, IWebSocketPushMessage message) {
		Application app = Application.get(token.getApplicationKey());

		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(app);
		IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
		IWebSocketConnection connection = registry.getConnection(app, token.getSessionId(), token.getPageKey());

		if (connection != null) {
			if (connection.isOpen()) {
				connection.sendMessage(message);
				return true;
			} else {
				logger.error("wsPush: WebSocketConnection Is Not Opened!");
			}
		} else {
			logger.error("wsPush: WebSocketConnection Not Found!");
		}
		return false;
	}

	public static void wsBroadcast(Application application, Object message) {
		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
		IWebSocketConnectionRegistry registry = webSocketSettings.getConnectionRegistry();
		WebSocketPushBroadcaster broadcaster = new WebSocketPushBroadcaster(registry);
		broadcaster.broadcastAll(application, new WebSocketBroadcastMessage(message));
	}

	// ---------------

	public static List<FeedbackMessage> collect(Component component, final boolean clearAfter) {

		/*
		The original solution:

		FeedbackMessagesModel feedbackMessagesModel = new FeedbackMessagesModel(this);
		List<FeedbackMessage> messages = feedbackMessagesModel.getObject();

		In the previous line, the main method is:
		return new FeedbackCollector(pageResolvingComponent.getPage()).collect(filter);

		So we must search all the page's component for FeedbackMessage!
		*/
		component = component.getPage();
		final List<FeedbackMessage> messages = new ArrayList<>();

		final IFeedbackMessageFilter filter = null;

		if (Session.exists()) {
			messages.addAll(Session.get().getFeedbackMessages().messages(filter));
			if (clearAfter) {
				Session.get().getFeedbackMessages().clear(filter);
			}
		}

		if (component != null && component.hasFeedbackMessage()) {
			messages.addAll(component.getFeedbackMessages().messages(filter));
			if (clearAfter) {
				component.getFeedbackMessages().clear(filter);
			}
		}

		/*
		There is "component = component.getPage();" in previous lines. So the following
		"component instanceof MarkupContainer" is always true, and so commented!
		*/
		if (component != null /*&& component instanceof MarkupContainer*/) {
			((MarkupContainer) component).visitChildren(new IVisitor<Component, Void>() {

				@Override
				public void component(Component object, IVisit<Void> visit) {
					if (object.hasFeedbackMessage()) {
						messages.addAll(object.getFeedbackMessages().messages(filter));
						if (clearAfter) {
							object.getFeedbackMessages().clear(filter);
						}
					}
				}
			});
		}

		return messages;
	}

	public static List<Serializable> collectAs(Component component, final boolean clearAfter) {
		List<FeedbackMessage> collect = collect(component, clearAfter);

		List<Serializable> result = new ArrayList<>();
		for (FeedbackMessage message : collect) {
			result.add(message.getMessage());
		}
		return result;
	}

	public static void copyToClipboard(String text, IPartialPageRequestHandler handler) {
		handler.appendJavaScript(String.format("wTools.copyToClipboard(\"%s\");", text));
	}

	public static String escape(String text, boolean escapeSpaces, boolean convertToHtmlUnicodeEscapes) {
		String str = Strings.escapeMarkup(text, escapeSpaces, convertToHtmlUnicodeEscapes).toString();
		str = str.replaceAll("[\n]", "<br/>");
		str = str.replaceAll("[\r]", "");
		return str;
	}

	// ------------------------------

	private static IRequestParameters getRequestParameters() {
		return RequestCycle.get().getRequest().getRequestParameters();
	}
}
