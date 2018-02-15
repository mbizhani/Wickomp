package org.devocative.wickomp.data;

import com.fasterxml.jackson.annotation.JsonValue;
import org.devocative.wickomp.grid.WDuplicateKeyException;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RObjectList extends Result {
	private static final long serialVersionUID = 5486159742298202143L;

	private Map<String, RObject> result = new LinkedHashMap<>();

	// ------------------------------

	public RObjectList addRObject(String id, RObject rObject) {
		if (!result.containsKey(id)) {
			result.put(id, rObject);
		} else if (!rObject.equals(result.get(id))) {
			throw new WDuplicateKeyException(
				String.format(
					"Duplicate key in result:\n\tRECORD-1 = \n%s\n\tRECORD-2 = \n%s",
					result.get(id), rObject));
		}
		return this;
	}

	public RObject getRObject(String id) {
		return result.get(id);
	}

	public boolean hasRObject(String id) {
		return result.containsKey(id);
	}

	// ---------------

	@JsonValue
	public Collection<RObject> getValue() {
		return result.values();
	}
}
