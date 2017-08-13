package org.devocative.wickomp.html;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.wicket.Component;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
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

		options.setTitle(title).setMsg(message);
		return String.format("wTools.show(%s);", WebUtil.toJson(options));
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

	public static void show(Exception ex, IPartialPageRequestHandler handler) {
		show(ex, null, handler);
	}

	public static void show(Exception ex, Component cmp, IPartialPageRequestHandler handler) {
		String msg = ex.getMessage();
		if (WDefaults.getExceptionToMessageHandler() != null) {
			msg = WDefaults.getExceptionToMessageHandler().handleMessage(cmp, ex);
		}
		show(WebUtil.getStringOfResource("label.error", "Error"), msg, handler);
	}

	public static void show(String title, String message, IPartialPageRequestHandler handler) {
		show(title, message, new OMessager(), handler);
	}

	public static void show(String title, List<?> errors, IPartialPageRequestHandler handler) {
		show(title, errors, new OMessager(), handler);
	}

	public static void show(String title, List<?> errors, OMessager options, IPartialPageRequestHandler handler) {
		show(title, getHtml(errors), options, handler);
	}

	// Main Function
	public static void show(String title, String message, OMessager options, IPartialPageRequestHandler handler) {
		String sc = getScript(title, message, options);
		handler.appendJavaScript(sc);
	}

	// ------------------------------

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class OMessager {
		private Boolean draggable;
		private String height;
		private Boolean modal;
		private String msg;
		private Boolean resizable;
		private OAnimation showType;
		private Integer timeout;
		private String title;
		private String width;

		// ---------------

		public Boolean getDraggable() {
			return draggable;
		}

		public OMessager setDraggable(Boolean draggable) {
			this.draggable = draggable;
			return this;
		}

		public String getHeight() {
			return height;
		}

		public OMessager setHeight(String height) {
			this.height = height;
			return this;
		}

		public Boolean getModal() {
			return modal;
		}

		public OMessager setModal(Boolean modal) {
			this.modal = modal;
			return this;
		}

		public String getMsg() {
			return msg;
		}

		public OMessager setMsg(String msg) {
			this.msg = msg;
			return this;
		}

		public Boolean getResizable() {
			return resizable;
		}

		public OMessager setResizable(Boolean resizable) {
			this.resizable = resizable;
			return this;
		}

		public OAnimation getShowType() {
			return showType;
		}

		public OMessager setShowType(OAnimation showType) {
			this.showType = showType;
			return this;
		}

		public Integer getTimeout() {
			return timeout;
		}

		public OMessager setTimeout(Integer timeout) {
			this.timeout = timeout;
			return this;
		}

		public String getTitle() {
			return title;
		}

		public OMessager setTitle(String title) {
			this.title = title;
			return this;
		}

		public String getWidth() {
			return width;
		}

		public OMessager setWidth(String width) {
			this.width = width;
			return this;
		}
	}
}
