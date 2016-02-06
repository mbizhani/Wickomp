package org.devocative.wickomp.html;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WMessager {
	public enum ShowType {slide, fade, show}

	public static void show(String title, String message, AjaxRequestTarget target) {
		show(title, message, ShowType.show, target);
	}

	public static void show(String title, List<?> errors, AjaxRequestTarget target) {
		show(title, errors, ShowType.show, target);
	}

	public static void show(String title, List<?> errors, ShowType showType, AjaxRequestTarget target) {
		StringBuilder builder = new StringBuilder();
		builder.append("<ul>");
		for (Object error : errors) {
			builder.append("<li>").append(error).append("</li>");
		}
		builder.append("</ul>");
		show(title, builder.toString(), showType, target);
	}

	public static void show(String title, String message, ShowType showType, AjaxRequestTarget target) {
		message = message.replace('\'', '"');
		message = message.replaceAll("[\\r]", "");
		message = message.replaceAll("[\\n]", "<br/>");
		message = message.replaceAll("[\\t]", "&nbsp;&nbsp;");

		String sc = String.format(
				"$.messager.show({title:'%s',msg:'%s',showType:'%s',timeout:0,width:400,height:300,style:{right:'',bottom:''}});",
			title, message, showType);
		target.appendJavaScript(sc);
	}

	public static List<Serializable> collectMessages(Component component) {
		FeedbackMessagesModel feedbackMessagesModel = new FeedbackMessagesModel(component);
		List<FeedbackMessage> messages = feedbackMessagesModel.getObject();
		List<Serializable> errors = new ArrayList<>();
		if (messages.size() > 0) {
			for (FeedbackMessage message : messages) {
				errors.add(message.getMessage());
			}
		}
		return errors;
	}
}
