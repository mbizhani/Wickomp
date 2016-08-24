package org.devocative.wickomp;

import org.apache.wicket.Component;

public class WDefaults {
	private static IExceptionToMessageHandler exceptionToMessageHandler = new IExceptionToMessageHandler() {
		private static final long serialVersionUID = 4280611531970060923L;

		@Override
		public String handleMessage(Component component, Exception e) {
			if (e.getMessage() != null) {
				if (component == null) {
					return WebUtil.getStringOfResource(e.getMessage(), e.getMessage());
				} else {
					return component.getString(e.getMessage(), null, e.getMessage());
				}
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
