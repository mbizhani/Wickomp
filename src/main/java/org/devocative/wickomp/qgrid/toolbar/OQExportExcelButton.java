package org.devocative.wickomp.qgrid.toolbar;

import org.apache.wicket.request.IRequestParameters;
import org.devocative.adroit.ExcelExporter;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.qgrid.column.OQColumn;
import org.devocative.wickomp.qgrid.column.OQPropertyColumn;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OQExportExcelButton<T> extends OQButton<T> {
	private String fileName;
	private Integer maxRowsCount;
	private HTMLBase html;

	public OQExportExcelButton(HTMLBase html, String fileName, Integer maxRowsCount) {
		this.html = html;
		this.fileName = fileName;
		this.maxRowsCount = maxRowsCount;
	}

	@Override
	public String getHTMLContent(WQGridInfo<T> gridInfo) {
		return String.format("<a class=\"easyui-linkbutton\" plain=\"true\" href=\"%s\">%s</a>", getCallbackURL(), html.toString());
	}

	@Override
	public void onClick(final WQGridInfo<T> gridInfo, IRequestParameters parameters) {
		sendResource(new OutputStreamResource("application/excel", fileName) {
			@Override
			protected void handleStream(OutputStream stream) throws IOException {
				List<String> columnsTitle = new ArrayList<>();

				for (OQColumn<T> column : gridInfo.getOptions().getColumns()) {
					if (column instanceof OQPropertyColumn) {
						columnsTitle.add(column.getText());
					}
				}

				ExcelExporter exporter = new ExcelExporter(fileName);
				exporter.setColumnsHeader(columnsTitle);

				List<T> rawData = gridInfo.getDataSource().list(1, maxRowsCount, gridInfo.getSortFieldList());
				for (int rowNo = 0; rowNo < rawData.size(); rowNo++) {
					T bean = rawData.get(rowNo);
					List<String> rowResult = new ArrayList<>();
					List<OQColumn<T>> columns = gridInfo.getOptions().getColumns();
					for (int colNo = 0; colNo < columns.size(); colNo++) {
						OQColumn<T> column = columns.get(colNo);
						if (column instanceof OQPropertyColumn) {
							String cellValue = column.onCellRender(bean, rowNo) ?
								column.cellValue(bean, rowNo, colNo, null) :
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
