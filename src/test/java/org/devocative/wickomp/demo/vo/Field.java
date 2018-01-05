package org.devocative.wickomp.demo.vo;

import java.io.Serializable;

public class Field implements Serializable {
	private static final long serialVersionUID = -934057286621436596L;

	public enum Type {String, Integer, Real, Boolean, Date, SQL, RO}

	private String title;
	private String name;
	private Type type;

	public Field(String title, String name, Type type) {
		this.title = title;
		this.name = name;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}
}
