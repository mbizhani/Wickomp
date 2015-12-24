package org.devocative.wickomp.resource;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;

import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputStreamResource extends AbstractResource {
	private String contentType;
	private String fileName;

	protected OutputStreamResource() {
		this(null, null);
	}

	protected OutputStreamResource(String contentType) {
		this(contentType, null);
	}

	protected OutputStreamResource(String contentType, String fileName) {
		this.contentType = contentType;
		this.fileName = fileName;
	}

	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		ResourceResponse resourceResponse = new ResourceResponse();
		resourceResponse.setContentDisposition(ContentDisposition.ATTACHMENT);
		resourceResponse.setContentType(contentType);
		resourceResponse.setFileName(fileName);
		resourceResponse.disableCaching();

		resourceResponse.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(Attributes attributes) throws IOException {
				OutputStream stream = attributes.getResponse().getOutputStream();
				handleStream(stream);
			}
		});
		return resourceResponse;
	}

	protected abstract void handleStream(OutputStream stream) throws IOException;
}
