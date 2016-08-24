package org.devocative.wickomp.grid;

import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.RObjectList;
import org.devocative.wickomp.data.Result;

import java.util.List;

class RGridPage extends Result {
	private static final long serialVersionUID = 1781193319558429155L;

	private RObjectList rows;

	private List<RObject> footer;

	private Long total;

	private String error;

	private Boolean async;

	public RObjectList getRows() {
		return rows;
	}

	public RGridPage setRows(RObjectList rows) {
		this.rows = rows;
		return this;
	}

	public List<RObject> getFooter() {
		return footer;
	}

	public RGridPage setFooter(List<RObject> footer) {
		this.footer = footer;
		return this;
	}

	public Long getTotal() {
		return total;
	}

	public RGridPage setTotal(Long total) {
		this.total = total;
		return this;
	}

	public String getError() {
		return error;
	}

	public RGridPage setError(String error) {
		this.error = error;
		return this;
	}

	public Boolean getAsync() {
		return async;
	}

	public RGridPage setAsync(Boolean async) {
		this.async = async;
		return this;
	}
}
