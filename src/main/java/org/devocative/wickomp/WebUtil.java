package org.devocative.wickomp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static Map<String, List<String>> toMap(IRequestParameters parameters, boolean lowercaseParam) {
		Map<String, List<String>> result = new HashMap<>();
		for (String param : parameters.getParameterNames()) {
			List<String> values = new ArrayList<>();

			for (StringValue stringValue : parameters.getParameterValues(param)) {
				values.add(stringValue.toString());
			}

			if (lowercaseParam) {
				param = param.toLowerCase();
			}

			if (result.containsKey(param)) {
				result.get(param).addAll(values);
			} else {
				result.put(param, values);
			}
		}
		return result;
	}
}
