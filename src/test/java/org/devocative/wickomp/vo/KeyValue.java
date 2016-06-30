package org.devocative.wickomp.vo;

import java.io.Serializable;

public class KeyValue implements Serializable {
	private String key;
	private String value;

	public KeyValue(String key) {
		this(key, null);
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof KeyValue)) return false;

		KeyValue keyValue = (KeyValue) o;

		return !(getKey() != null ? !getKey().equals(keyValue.getKey()) : keyValue.getKey() != null);

	}

	@Override
	public int hashCode() {
		return getKey() != null ? getKey().hashCode() : 0;
	}

	/*
	@Override
	public String toString() {
		return String.format("{key:%s, value:%s}", getKey(), getValue());
	}
*/
}
