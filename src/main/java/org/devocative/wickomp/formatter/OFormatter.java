package org.devocative.wickomp.formatter;

import java.io.Serializable;

public interface OFormatter extends Serializable {
	String format(Object value);
}
