package org.devocative.wickomp.html;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.devocative.wickomp.WDefaults;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.opt.OAnimation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class WMessager {
	public static String getScript(String title, String message) {
		return getScript(title, message, new OMessager());
	}

	// Main Function
	public static String getScript(String title, String message, OMessager options) {
		message = message.replaceAll("[\\\\]", "\\\\\\\\");
		message = message.replaceAll("'", "\\\\'");
		message = message.replaceAll("[\\r]", "");
		message = message.replaceAll("[\\n]", "<br/>");
		message = message.replaceAll("[\\t]", "&nbsp;&nbsp;");

		return String.format(
			"$.messager.show({title:'%s',msg:'%s',showType:'%s',timeout:%d,width:'%s',height:'%s',style:{right:'',bottom:''},draggable:%s,resizable:%s,modal:%s});",
			title, message, options.getShowType(), options.getTimeout(), options.getWidth(), options.getHeight(),
			options.isDraggable(), options.isResizable(), options.isModal());
	}

	public static void writeErrorsInAfterRender(Component component) {
		List<Serializable> errors = WebUtil.collectAs(component, true);
		if (errors.size() > 0) {
			String st = WMessager.getScript(component.getString("label.error", null, "Error"),
				WMessager.getHtml(errors));

			WebUtil.writeJQueryCall(st, true);
		}
	}

	// ---------------

	public static String getHtml(Collection<?> errors) {
		StringBuilder builder = new StringBuilder();
		builder.append("<ul>");
		for (Object error : errors) {
			builder.append("<li>").append(error).append("</li>");
		}
		builder.append("</ul>");
		return builder.toString();
	}

	// ---------------

	public static void show(Exception ex, AjaxRequestTarget target) {
		show(ex, null, target);
	}

	public static void show(Exception ex, Component cmp, AjaxRequestTarget target) {
		String msg = ex.getMessage();
		if (WDefaults.getExceptionToMessageHandler() != null) {
			msg = WDefaults.getExceptionToMessageHandler().handleMessage(cmp, ex);
		}
		show(WebUtil.getStringOfResource("label.error", "Error"), msg, target);
	}

	public static void show(String title, String message, AjaxRequestTarget target) {
		show(title, message, new OMessager(), target);
	}

	public static void show(String title, List<?> errors, AjaxRequestTarget target) {
		show(title, errors, new OMessager(), target);
	}

	public static void show(String title, List<?> errors, OMessager options, AjaxRequestTarget target) {
		show(title, getHtml(errors), options, target);
	}

	// Main Function
	public static void show(String title, String message, OMessager options, AjaxRequestTarget target) {
		String sc = getScript(title, message, options);
		target.appendJavaScript(sc);
	}

	// ------------------------------

	public static class OMessager {
		private OAnimation showType = OAnimation.show;
		private int timeout = 0;
		private boolean draggable = true;
		private boolean resizable = true;
		private boolean modal = false;
		private String width = "";
		private String height = "";

		public OAnimation getShowType() {
			return showType;
		}

		public OMessager setShowType(OAnimation showType) {
			this.showType = showType;
			return this;
		}

		public int getTimeout() {
			return timeout;
		}

		public OMessager setTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}

		public boolean isDraggable() {
			return draggable;
		}

		public OMessager setDraggable(boolean draggable) {
			this.draggable = draggable;
			return this;
		}

		public boolean isResizable() {
			return resizable;
		}

		public OMessager setResizable(boolean resizable) {
			this.resizable = resizable;
			return this;
		}

		public boolean isModal() {
			return modal;
		}

		public OMessager setModal(boolean modal) {
			this.modal = modal;
			return this;
		}

		public String getWidth() {
			return width;
		}

		public OMessager setWidth(String width) {
			this.width = width;
			return this;
		}

		public String getHeight() {
			return height;
		}

		public OMessager setHeight(String height) {
			this.height = height;
			return this;
		}
	}
}
