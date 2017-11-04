package org.devocative.wickomp.opt;

public interface IStyler<T> {
	OStyle doStyle(T bean, String id);
}
