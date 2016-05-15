package org.devocative.wickomp.http.filter;

import java.security.Principal;

public class WPrinciple implements Principal {
	private String name;

	public WPrinciple(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public WPrinciple setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WPrinciple)) return false;

		WPrinciple that = (WPrinciple) o;

		return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

	}

	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}
}
