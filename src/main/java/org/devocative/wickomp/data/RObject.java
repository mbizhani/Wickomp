package org.devocative.wickomp.data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class RObject extends Result {
	private Map<String, String> properties = new HashMap<String, String>();

	public RObject addProperty(String property, String value) {
		properties.put(property, value);
		return this;
	}

	public RObject removeProperty(String property) {
		properties.remove(property);
		return this;
	}

	public String getProperty(String property) {
		return properties.get(property);
	}

	@JsonValue
	public Map<String, String> getValue() {
		return properties;
	}
}
