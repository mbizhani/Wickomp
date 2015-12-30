package org.devocative.wickomp.grid.column;

import com.fasterxml.jackson.annotation.JsonValue;
import org.devocative.wickomp.opt.Options;

import java.util.ArrayList;
import java.util.List;

public class OColumnList<T> extends Options {
	private List<OColumn<T>> columns = new ArrayList<>();

	public OColumnList<T> add(OColumn<T> column) {
		columns.add(column);
		return this;
	}

	public List<OColumn<T>> getList() {
		return columns;
	}

	@JsonValue
	public List<List<OColumn<T>>> getValue() {
		ArrayList<List<OColumn<T>>> result = new ArrayList<>();
		result.add(columns);
		return result;
	}
}
