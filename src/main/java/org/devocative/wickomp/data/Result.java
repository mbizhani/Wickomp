package org.devocative.wickomp.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Result implements Serializable {
}
