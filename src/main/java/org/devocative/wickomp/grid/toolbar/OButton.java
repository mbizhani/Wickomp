package org.devocative.wickomp.grid.toolbar;

import com.fasterxml.jackson.annotation.JsonRawValue;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.devocative.wickomp.data.DataSource;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.opt.OComponent;

import java.io.Serializable;
import java.util.List;

public abstract class OButton<T extends Serializable> extends OComponent {
	private HTMLBase html;
	private int index;
	private String url;

	public OButton(HTMLBase html) {
		this.html = html;
	}

	public String getText() {
		return html.toString();
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonRawValue
	public String getHandler() {
		StringBuilder builder =
			new StringBuilder("function(){")
				.append(String.format("window.location=\"%s&cn=%s&tp=bt\";", url, index))
				.append("}");
		return builder.toString();
	}

	public abstract void onClick(List<OColumn<T>> columns, DataSource<T> dataSource);

	protected void sendResource(IResource resource) {
		sendResource(resource, null);
	}

	protected void sendResource(IResource resource, PageParameters pageParameters) {
		RequestCycle cycle = RequestCycle.get();
		cycle.scheduleRequestHandlerAfterCurrent(new ResourceRequestHandler(resource, pageParameters));
	}
}
