package org.devocative.wickomp;

import org.apache.wicket.Component;

public class WDefaults {
	private static IExceptionToMessageHandler exceptionToMessageHandler = new IExceptionToMessageHandler() {
		@Override
		public String handleMessage(Component component, Exception e) {
			if (e.getMessage() != null) {
				return component.getString(e.getMessage(), null, e.getMessage());
			}
			return "[Error(?)]";
		}
	};

	public static IExceptionToMessageHandler getExceptionToMessageHandler() {
		return exceptionToMessageHandler;
	}

	public static void setExceptionToMessageHandler(IExceptionToMessageHandler exceptionToMessageHandler) {
		WDefaults.exceptionToMessageHandler = exceptionToMessageHandler;
	}
}
