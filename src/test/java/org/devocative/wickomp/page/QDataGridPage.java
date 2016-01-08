package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.data.WDataSource;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.html.icon.FontAwesome;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.qgrid.OQGrid;
import org.devocative.wickomp.qgrid.WQDataGrid;
import org.devocative.wickomp.qgrid.column.OQColumn;
import org.devocative.wickomp.qgrid.column.OQPropertyColumn;
import org.devocative.wickomp.qgrid.toolbar.OQExportExcelButton;
import org.devocative.wickomp.vo.PersonVO;

import java.util.ArrayList;
import java.util.List;

public class QDataGridPage extends BasePage {
	private List<PersonVO> list;
	private WQDataGrid<PersonVO> grid;

	public QDataGridPage() {
		list = PersonVO.list();

		List<OQColumn<PersonVO>> columns = new ArrayList<>();
		columns.add(new OQPropertyColumn<PersonVO>(new Model<>("Col01"), "col01"));
		columns.add(new OQPropertyColumn<PersonVO>(new Model<>("Col02"), "col02"));
		columns.add(new OQPropertyColumn<PersonVO>(new Model<>("Col03"), "col03"));

		OQGrid<PersonVO> oGrid = new OQGrid<>();
		oGrid
			.setColumns(columns)
			.addToolbarButton(new OQExportExcelButton<PersonVO>(new FontAwesome("file-excel-o", "green", new Model<>("Export to excel")), "Export.xlsx", 1000))
			.setWidth(OSize.percent(100))
		;
		add(grid = new WQDataGrid<>("grid1", oGrid, new WDataSource<PersonVO>() {
			@Override
			public List<PersonVO> list(long first, long size, List<WSortField> sortFields) {
				int start = (int) ((first - 1) * size);
				int end = (int) (first * size);
				return list.subList(start, Math.min(end, list.size()));
			}

			@Override
			public long count() {
				return list.size();
			}

			@Override
			public IModel<PersonVO> model(PersonVO object) {
				return new Model<>(object);
			}
		}.setEnabled(false)));


		add(new AjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				grid.getDataSource().setEnabled(true);
				grid.loadData(target);
//				target.appendJavaScript("alert('hi');");
			}
		});
	}
}
