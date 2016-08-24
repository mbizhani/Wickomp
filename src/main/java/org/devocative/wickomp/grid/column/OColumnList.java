package org.devocative.wickomp.grid.column;

import com.fasterxml.jackson.annotation.JsonValue;
import org.devocative.wickomp.opt.Options;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OColumnList<T> extends Options {
	private static final long serialVersionUID = -5642305527886499710L;

	private List<OColumn<T>> visibleColumns = new ArrayList<>();
	private List<OColumn<T>> allColumns = new ArrayList<>();

	public OColumnList<T> add(OColumn<T> column) {
		if (column.isVisible()) {
			visibleColumns.add(column);
		}
		allColumns.add(column);
		return this;
	}

	@JsonValue
	public List<List<OColumn<T>>> getValue() {
		ArrayList<List<OColumn<T>>> result = new ArrayList<>();
		result.add(visibleColumns);
		return result;
	}

	public List<OColumn<T>> getVisibleColumns() {
		return visibleColumns;
	}

	public List<OColumn<T>> getAllColumns() {
		return allColumns;
	}

	public void validate() {
		Set<String> fields = new HashSet<>();
		for (OColumn<T> oColumn : allColumns) {
			if (fields.contains(oColumn.getField())) {
				throw new RuntimeException("Duplicate field in columns: " + oColumn.getField());
			}
			fields.add(oColumn.getField());
		}
	}
}
