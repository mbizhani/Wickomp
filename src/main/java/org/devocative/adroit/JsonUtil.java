package org.devocative.adroit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;

/*
 * Some useful annotations:
 *
 * @JsonInclude(JsonInclude.Include.NON_NULL)
 *
 * @JsonRawValue
 *
 * @JsonProperty("<as property name>")
 *
 * @JsonValue
 */
public class JsonUtil {
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
}
