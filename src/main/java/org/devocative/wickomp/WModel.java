package org.devocative.wickomp;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public class WModel<T> implements IModel<T> {
	private static final long serialVersionUID = 1L;

	private T object;

	public WModel() {
	}

	public WModel(T object) {
		setObject(object);
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public void setObject(T object) {
		if (object != null && !(object instanceof Serializable)) {
			throw new WicketRuntimeException("Model object must be Serializable");
		}
		this.object = object;
	}

	@Override
	public void detach() {
		if (object instanceof IDetachable) {
			((IDetachable) object).detach();
		}
	}
}
