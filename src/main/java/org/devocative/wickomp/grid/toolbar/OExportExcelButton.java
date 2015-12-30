package org.devocative.wickomp.grid.toolbar;

import org.devocative.adroit.ExcelExporter;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OExportExcelButton<T extends Serializable> extends OButton<T> {
	private String fileName;
	private Integer maxRowsCount;

	public OExportExcelButton(HTMLBase html, String fileName, Integer maxRowsCount) {
		super(html);
		this.fileName = fileName;
		this.maxRowsCount = maxRowsCount;
	}

	public void onClick(final WGridInfo<T> gridInfo) {
		sendResource(new OutputStreamResource("application/excel", fileName) {
			@Override
			protected void handleStream(OutputStream stream) throws IOException {
				List<String> columnsTitle = new ArrayList<>();

				for (OColumn<T> column : gridInfo.getColumns()) {
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
					for (int colNo = 0; colNo < gridInfo.getColumns().size(); colNo++) {
						OColumn<T> column = gridInfo.getColumns().get(colNo);
						if (column instanceof OPropertyColumn) {
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
