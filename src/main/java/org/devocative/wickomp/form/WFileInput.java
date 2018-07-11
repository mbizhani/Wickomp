package org.devocative.wickomp.form;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.devocative.wickomp.WLabeledFormInputPanel;
import org.devocative.wickomp.WModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WFileInput extends WLabeledFormInputPanel<Collection<FileUpload>> {
	private static final long serialVersionUID = 7408468025385906990L;

	private FileUploadField singleFile;
	private MultiFileUploadField multipleFiles;

	private final List<FileUpload> uploads = new ArrayList<>();
	private int max = -1; // UNLIMITED
	private boolean multiple = false;

	// ------------------------------

	public WFileInput(String id) {
		this(id, new WModel<>());
	}

	private WFileInput(String id, IModel<Collection<FileUpload>> model) {
		super(id, model);
	}

	// ------------------------------

	public WFileInput setMax(int max) {
		this.max = max;
		return this;
	}

	public WFileInput setMultiple(boolean multiple) {
		this.multiple = multiple;
		return this;
	}

	public List<FileUpload> getFileUpload() {
		if (!multiple) {
			uploads.clear();
			uploads.add(singleFile.getFileUpload());
		}
		return uploads;
	}

	@Override
	public void convertInput() {
		setConvertedInput(multiple ? multipleFiles.getConvertedInput() : singleFile.getConvertedInput());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		multipleFiles = new MultiFileUploadField("multipleFiles", new PropertyModel<>(this, "uploads"), max, true);
		multipleFiles.setVisible(multiple);

		singleFile = new FileUploadField("singleFile", new WModel<>());
		singleFile.setVisible(!multiple);

		add(multipleFiles);
		add(singleFile);
	}
}
