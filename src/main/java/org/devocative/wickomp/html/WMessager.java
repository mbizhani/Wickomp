package org.devocative.wickomp.html;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.devocative.wickomp.WebUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class WMessager {
	public enum ShowType {slide, fade, show}

	// ---------------

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
			"$.messager.show({title:'%s',msg:'%s',showType:'%s',timeout:0,width:400,height:300,style:{right:'',bottom:''},draggable:%s,resizable:%s,modal:%s});",
			title, message, options.getShowType(), options.isDraggable(), options.isResizable(), options.isModal());
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
		private ShowType showType = ShowType.show;
		private boolean draggable = true;
		private boolean resizable = true;
		private boolean modal = false;

		public ShowType getShowType() {
			return showType;
		}

		public OMessager setShowType(ShowType showType) {
			this.showType = showType;
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
	}
}
