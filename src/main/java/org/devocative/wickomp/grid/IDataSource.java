package org.devocative.wickomp.grid;

import org.apache.wicket.model.IModel;

import java.io.Serializable;

public interface IDataSource<T> extends Serializable {
	IModel<T> model(T object);
}
