package org.devocative.wickomp.async;

import java.io.Serializable;
import java.util.UUID;

public class AsyncToken implements Serializable {
	private static final long serialVersionUID = -2444128118463747127L;

	private String id = UUID.randomUUID().toString();
	private String handlerId;

	public String getId() {
		return id;
	}

	public String getHandlerId() {
		return handlerId;
	}

	public AsyncToken setHandlerId(String handlerId) {
		this.handlerId = handlerId;
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
		return id;
	}
}
