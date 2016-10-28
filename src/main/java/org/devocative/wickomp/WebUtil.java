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
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.api.IWebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.WebSocketResponse;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

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

	public static Map<String, List<String>> toMap(IRequestParameters parameters, boolean lowercaseParam, boolean ignoreEmpty) {
		Map<String, List<String>> result = new HashMap<>();
		for (String param : parameters.getParameterNames()) {
			List<String> values = new ArrayList<>();

			for (StringValue stringValue : parameters.getParameterValues(param)) {
				if (ignoreEmpty) {
					if (!stringValue.isNull() && !stringValue.isEmpty()) {
						values.add(stringValue.toString());
					}
				} else {
					values.add(stringValue.toString());
				}
			}

			if (values.size() > 0) {
				if (lowercaseParam) {
					param = param.toLowerCase();
				}

				if (result.containsKey(param)) {
					result.get(param).addAll(values);
				} else {
					result.put(param, values);
				}
			}
		}
		return result;
	}

	public static Map<String, List<String>> toMap(String paramsAsUrl, boolean lowercaseParam, boolean ignoreEmpty) {
		Map<String, List<String>> result = new HashMap<>();

		String[] paramValueArr = paramsAsUrl.split("[&]");

		for (String paramValue : paramValueArr) {
			int i = paramValue.indexOf('=');
			if (i > 0) {
				String param = paramValue.substring(0, i).trim();
				String value = paramValue.substring(i + 1).trim();

				if (lowercaseParam) {
					param = param.toLowerCase();
				}

				if (!result.containsKey(param)) {
					result.put(param, new ArrayList<String>());
				}

				List<String> values = result.get(param);
				if (ignoreEmpty || !value.isEmpty()) {
					values.add(value);
				}
			}
		}
		return result;
	}

	public static Set<String> toSet(IRequestParameters parameters, boolean lowercaseParam) {
		Set<String> result = new HashSet<>();
		for (String param : parameters.getParameterNames()) {
			result.add(lowercaseParam ? param.toLowerCase() : param);
		}
		return result;
	}

	public static String getStringOfResource(String resourceKey, String defaultValue) {
		return getStringOfResource(resourceKey, Model.of(defaultValue));
	}

	public static String getStringOfResource(String resourceKey, IModel<String> defaultValue) {
		return Application.get()
			.getResourceSettings()
			.getLocalizer()
			.getString(resourceKey, null, null, null, null, defaultValue);
	}

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
}
