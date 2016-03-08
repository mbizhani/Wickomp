package org.devocative.wickomp.async;

import java.io.Serializable;
import java.util.UUID;

public class AsyncToken implements Serializable {
	private String id = UUID.randomUUID().toString();
	private String handler;

	public String getId() {
		return id;
	}

	public String getHandler() {
		return handler;
	}

	public AsyncToken setHandler(String handler) {
		this.handler = handler;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AsyncToken)) return false;

		AsyncToken that = (AsyncToken) o;

		return !(id != null ? !id.equals(that.id) : that.id != null);

	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "id: " + id;
	}
}
