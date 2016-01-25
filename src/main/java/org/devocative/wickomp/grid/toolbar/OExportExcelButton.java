package org.devocative.wickomp.grid.toolbar;

import org.apache.wicket.request.IRequestParameters;
import org.devocative.adroit.ExcelExporter;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OExportExcelButton<T> extends OButton<T> {
	private String fileName;
	private Integer maxRowsCount;
	private HTMLBase html;

	public OExportExcelButton(HTMLBase html, String fileName, Integer maxRowsCount) {
		this.html = html;
		this.fileName = fileName;
		this.maxRowsCount = maxRowsCount;
	}

	@Override
	public String getHTMLContent(WGridInfo<T> gridInfo) {
		return String.format("<a class=\"easyui-linkbutton\" plain=\"true\" href=\"%s\">%s</a>", getCallbackURL(), html.toString());
	}

	@Override
	public void onClick(final WGridInfo<T> gridInfo, IRequestParameters parameters) {
		sendResource(new OutputStreamResource("application/excel", fileName) {
			@Override
			protected void handleStream(OutputStream stream) throws IOException {
				List<String> columnsTitle = new ArrayList<>();

				for (OColumn<T> column : gridInfo.getOptions().getColumns().getList()) {
					if (column instanceof OPropertyColumn) {
						columnsTitle.add(column.getTitle());
					}
				}

				ExcelExporter exporter = new ExcelExporter(fileName);
				exporter.setColumnsHeader(columnsTitle);

				List<T> rawData = gridInfo.getDataSource().list(1, maxRowsCount, gridInfo.getSortFieldList());
				for (int rowNo = 0; rowNo < rawData.size(); rowNo++) {
					T bean = rawData.get(rowNo);
					List<String> rowResult = new ArrayList<>();
					List<OColumn<T>> columns = gridInfo.getOptions().getColumns().getList();
					for (int colNo = 0; colNo < columns.size(); colNo++) {
						OColumn<T> column = columns.get(colNo);
						if (column instanceof OPropertyColumn) {
							String cellValue = column.onCellRender(bean, String.valueOf(rowNo)) ?
								column.cellValue(bean, String.valueOf(rowNo), colNo, null) :
								"";
							rowResult.add(cellValue);
						}
					}
					exporter.addRowData(rowResult);
				}

				exporter.generate(stream);
			}
		});
	}
}
