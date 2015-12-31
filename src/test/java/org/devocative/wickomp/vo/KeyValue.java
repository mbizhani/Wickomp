package org.devocative.wickomp.vo;

import java.io.Serializable;

public class KeyValue implements Serializable {
	private String key;
	private String value;

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue() != null ? getValue() : "?";
	}

	/*
	@Override
	public String toString() {
		return String.format("{key:%s, value:%s}", getKey(), getValue());
	}
*/
}
