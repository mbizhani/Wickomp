package org.devocative.wickomp;

import org.apache.wicket.Component;

import java.io.Serializable;

public interface IExceptionToMessageHandler extends Serializable {

	String handleMessage(Component component, Throwable e);
}
