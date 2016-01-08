package org.devocative.wickomp.qgrid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.devocative.wickomp.opt.Options;
import org.devocative.wickomp.qgrid.column.OQColumn;

import java.util.ArrayList;
import java.util.List;

class OQDataAdapter<T> extends Options {
	private List<OQDataAdapterField> dataFields;
	private String url;
	private String gridHTMLId;

	// ----------------------- ACCESSORS

	public String getUrl() {
		return url;
	}

	public OQDataAdapter setUrl(String url) {
		this.url = url;
		return this;
	}

	// ----------------------- METHODS

	public String getDataType() {
		return "json";
	}

	public List<OQDataAdapterField> getDataFields() {
		return dataFields;
	}

	public Boolean getCache() {
		return false;
	}

	public String getRoot() {
		return "rows";
	}

	@JsonRawValue
	public String getSort() {
		return String.format("function(){$('#%s').jqxGrid('updatebounddata', 'sort');}", gridHTMLId);
	}

	@JsonRawValue
	@JsonProperty("beforeprocessing")
	public String getBeforeProcessing() {
		return "function(data){this.totalrecords = data.total;}";
	}

	public void createDataFields(List<OQColumn<T>> columns) {
		dataFields = new ArrayList<>();
		for (OQColumn column : columns) {
			dataFields.add(new OQDataAdapterField(column.getDataField()));
		}
	}

	public void setGridHTMLId(String gridHTMLId) {
		this.gridHTMLId = gridHTMLId;
	}
}
