package org.devocative.wickomp.data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class RObject extends Result {
	private static final long serialVersionUID = -5062992658934621619L;

	private Map<String, Object> properties = new HashMap<>();

	// ------------------------------

	public RObject addProperty(String property, Object value) {
		properties.put(property, value);
		return this;
	}

	public RObject removeProperty(String property) {
		properties.remove(property);
		return this;
	}

	public Object getProperty(String property) {
		return properties.get(property);
	}

	// ---------------

	@JsonValue
	public Map<String, Object> getValue() {
		return properties;
	}

	// ---------------

	@Override
	public String toString() {
		return properties.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RObject)) return false;

		RObject rObject = (RObject) o;

		return !(properties != null ? !properties.equals(rObject.properties) : rObject.properties != null);

	}

	@Override
	public int hashCode() {
		return properties != null ? properties.hashCode() : 0;
	}
}
