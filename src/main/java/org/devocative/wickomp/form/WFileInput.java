package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WModel;

import java.util.List;

public class WFileInput extends WLabeledFormInputPanel<List<FileUpload>> {
	private static final long serialVersionUID = 7408468025385906990L;

	private FileUploadField fileUploadField;

	// ------------------------------

	public WFileInput(String id) {
		this(id, new WModel<List<FileUpload>>());
	}

	private WFileInput(String id, IModel<List<FileUpload>> model) {
		super(id, model);

		fileUploadField = new FileUploadField("file");
		add(fileUploadField);
	}

	// ------------------------------

	public FileUpload getFileUpload() {
		return fileUploadField.getFileUpload();
	}

	@Override
	public void convertInput() {
		setConvertedInput(fileUploadField.getConvertedInput());
	}
}
