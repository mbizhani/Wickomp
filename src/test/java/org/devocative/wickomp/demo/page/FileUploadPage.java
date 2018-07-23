package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.WPanel;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.form.WAjaxButton;
import org.devocative.wickomp.form.WFileInput;
import org.devocative.wickomp.form.WSelectionInput;
import org.devocative.wickomp.html.WAjaxLink;
import org.devocative.wickomp.html.window.WModalWindow;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUploadPage extends BasePage {
	private static final long serialVersionUID = 72561542424271224L;

	private WFileInput file;
	private FeedbackPanel feedback;
	private WModalWindow window;

	public FileUploadPage() {
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(window = new WModalWindow("window"));

		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);

		file = new WFileInput("file");
		file
			.setMultiple(true)
			.setLabel(new Model<>("The File"));

		Form<Void> form = new Form<>("form");
		form.add(file);
		form.add(new WAjaxButton("submit") {
			private static final long serialVersionUID = -1647265116936158433L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				checkSubmit2("WAjaxButton.onSubmit", target, feedback, file);
			}
		});
		add(form);

		add(new WAjaxLink("openModal") {
			private static final long serialVersionUID = 4733205279081870831L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				window.setContent(new FileUploadPanel(window.getContentId()));
				window.show(target);
			}
		});
	}

	// ------------------------------

	private void checkSubmit(String disc, AjaxRequestTarget target, FeedbackPanel feedback, FileUploadField... files) {
		if (files != null) {
			for (FileUploadField file : files) {
				FileUpload upload = file.getFileUpload();
				if (upload == null) {
					info("No file uploaded");
				} else {
					info("File-Name: " + upload.getClientFileName() + " File-Size: " + upload.getSize());
				}

				System.out.printf("disc = %s, file = %s \n", disc, upload != null);
			}
		}

		target.add(feedback);
	}

	private void checkSubmit2(String disc, AjaxRequestTarget target, FeedbackPanel feedback, WFileInput... files) {
		if (files != null) {
			for (WFileInput file : files) {
				List<FileUpload> uploads = file.getFileUpload();
				if (uploads == null) {
					info("No file uploaded");
				} else {
					StringBuilder builder = new StringBuilder();
					uploads.forEach(upload ->
						builder
							.append("File, name: ")
							.append(upload.getClientFileName())
							.append(", size: ").append(upload.getSize())
					);
					info(builder.toString());
				}

				System.out.printf("disc = %s, file = %s \n", disc, uploads != null);
			}
		}

		target.add(feedback);
	}

	// ------------------------------

	private class FileUploadPanel extends WPanel {
		private static final long serialVersionUID = 4137292751516592638L;

		private WModalWindow window;
		private WFileInput fileInPanel1, fileInPanel2;
		private FeedbackPanel feedbackInPanel;
		private WSelectionInput selectionInput;

		private Map<String, Object> map = new HashMap<>();

		public FileUploadPanel(String id) {
			super(id);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			add(window = new WModalWindow("window"));

			feedbackInPanel = new FeedbackPanel("feedbackInPanel");
			feedbackInPanel.setOutputMarkupId(true);
			add(feedbackInPanel);

			fileInPanel1 = new WFileInput("fileInPanel1");
			//fileInPanel1.setRequired(true);

			fileInPanel2 = new WFileInput("fileInPanel2");
			fileInPanel2
				.setMultiple(true)
				.setRequired(true);

			selectionInput = new WSelectionInput("selection", Arrays.asList("A", "B", "C"), true);
			selectionInput.setRequired(true);

			Form<Map<String, Object>> form = new Form<>("form", new CompoundPropertyModel<>(map));
			form.add(fileInPanel1);
			form.add(fileInPanel2);
			form.add(selectionInput);
			form.add(new WAjaxButton("submit") {
				private static final long serialVersionUID = -1647265116936158433L;

				@Override
				protected void onSubmit(AjaxRequestTarget target) {
					checkSubmit2("FileUploadPanel.onSubmit", target, feedbackInPanel, fileInPanel1, fileInPanel2);
					System.out.println("map = " + map);
				}

				@Override
				protected void onError(AjaxRequestTarget target, List<Serializable> errors) {
					for (Serializable error : errors) {
						error(error);
					}
					target.add(feedbackInPanel);
				}
			});
			add(form);

			add(new WAjaxLink("modal") {
				private static final long serialVersionUID = -2574064555313584695L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					window.setContent(new FileUploadPanel(window.getContentId()));
					window.show(target);
				}
			});
		}
	}
}
