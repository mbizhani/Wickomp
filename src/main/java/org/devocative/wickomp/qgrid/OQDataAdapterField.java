package org.devocative.wickomp.qgrid;

import org.devocative.wickomp.opt.Options;

class OQDataAdapterField extends Options {
	private String name;
	//private String type;

	public OQDataAdapterField() {
	}

	public OQDataAdapterField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public OQDataAdapterField setName(String name) {
		this.name = name;
		return this;
	}

	/*public String getType() {
		return "string";
	}*/

	/*public OQDataAdapterField setType(String type) {
		this.type = type;
		return this;
	}*/
}
