package org.devocative.wickomp.opt;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Options implements Serializable {
	private static final long serialVersionUID = -6165854618833150124L;
}
