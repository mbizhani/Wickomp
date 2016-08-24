package org.devocative.wickomp.data;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RObjectList extends Result {
	private static final long serialVersionUID = 5486159742298202143L;

	private Map<String, RObject> result = new LinkedHashMap<>();

	public RObjectList addRObject(String id, RObject rObject) {
		return addRObject(id, rObject, false);
	}

	public RObjectList addRObject(String id, RObject rObject, boolean force) {
		if (force || !result.containsKey(id)) {
			result.put(id, rObject);
		}
		return this;
	}

	public RObject getRObject(String id) {
		return result.get(id);
	}

	public boolean hasRObject(String id) {
		return result.containsKey(id);
	}

	@JsonValue
	public Collection<RObject> getValue() {
		return result.values();
	}
}
