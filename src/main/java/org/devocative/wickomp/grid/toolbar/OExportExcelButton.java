package org.devocative.wickomp.grid.toolbar;

import org.devocative.adroit.ExcelExporter;
import org.devocative.adroit.date.UniDate;
import org.devocative.wickomp.grid.IGridDataSource;
import org.devocative.wickomp.grid.WSortField;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OExportExcelButton<T> extends OLinkButton<T> {
	private static final long serialVersionUID = -8571985043735727439L;

	private IGridDataSource<T> dataSource;

	private List<WSortField> sortFieldList = new ArrayList<>();
	private String fileName = "export";
	private Integer maxRowsCount = 1000;
	private boolean showDateInName = true;
	private String dateFormat = "yyyyMMdd-HHmmss";

	// ------------------------------

	public OExportExcelButton(HTMLBase html, IGridDataSource<T> dataSource) {
		super(html);

		this.dataSource = dataSource;
	}

	// ------------------------------

	public OExportExcelButton<T> setSortFieldList(List<WSortField> sortFieldList) {
		this.sortFieldList = sortFieldList;
		return this;
	}

	public OExportExcelButton<T> setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public OExportExcelButton<T> setMaxRowsCount(Integer maxRowsCount) {
		this.maxRowsCount = maxRowsCount;
		return this;
	}

	public OExportExcelButton<T> setShowDateInName(boolean showDateInName) {
		this.showDateInName = showDateInName;
		return this;
	}

	public OExportExcelButton<T> setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	// ---------------

	@Override
	public void onClick() {
		String fname = fileName;

		if (!fname.endsWith(".xlsx")) {
			if (showDateInName) {
				fname += "_" + UniDate.now().format(dateFormat);
			}

			fname += ".xlsx";
		}

		sendResource(new OutputStreamResource("application/excel", fname) {
			private static final long serialVersionUID = 8680636627426356006L;

			@Override
			protected void handleStream(OutputStream stream) throws IOException {
				List<String> columnsTitle = getColumnList()
					.getVisibleColumns()
					.stream()
					.filter(column -> column instanceof OPropertyColumn)
					.map(OColumn::getTitle)
					.collect(Collectors.toList());

				ExcelExporter exporter = new ExcelExporter(fileName);
				exporter.setColumnsHeader(columnsTitle);

				List<T> rawData = dataSource.list(1, maxRowsCount, sortFieldList);
				for (int rowNo = 0; rowNo < rawData.size(); rowNo++) {
					T bean = rawData.get(rowNo);
					List<String> rowResult = new ArrayList<>();
					List<OColumn<T>> cols = getColumnList().getVisibleColumns();
					for (int colNo = 0; colNo < cols.size(); colNo++) {
						OColumn<T> column = cols.get(colNo);
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
