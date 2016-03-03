package org.devocative.wickomp;

import org.apache.wicket.Component;

import java.io.Serializable;

public interface IExceptionToMessageHandler extends Serializable {

	String handleMessage(Component component, Exception e);

	IExceptionToMessageHandler DEFAULT = new IExceptionToMessageHandler() {
		@Override
		public String handleMessage(Component component, Exception e) {
			return component.getString(e.getMessage(), null, e.getMessage());
		}
	};
}
