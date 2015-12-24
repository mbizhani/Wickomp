package com.devocative.wickomp;

import com.devocative.wickomp.vo.PersonVO;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.data.DataSource;
import org.devocative.wickomp.data.SortField;
import org.devocative.wickomp.grid.OGrid;
import org.devocative.wickomp.grid.WDataGrid;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;
import org.devocative.wickomp.grid.toolbar.OExportExcelButton;
import org.devocative.wickomp.html.icon.FontAwesome;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HomePage extends WebPage implements DataSource<PersonVO> {
	private static final long serialVersionUID = 1L;

	private List<PersonVO> list;

	public HomePage() {
		list = PersonVO.list();

		OColumnList<PersonVO> columns = new OColumnList<>();
		columns
			.add(new OLinkColumn<PersonVO>(new Model<>("Col01"), "col01") {
				@Override
				public void onClick(IModel<PersonVO> rowData) {
					System.out.println(rowData.getObject());
				}
			}.setSortable(true))

			.add(new OAjaxLinkColumn<PersonVO>(new Model<>("Col 02"), "col02") {
				@Override
				public void onClick(AjaxRequestTarget target, IModel<PersonVO> rowData) {
					target.appendJavaScript(String.format("alert(\"%s\");", rowData.getObject()));
				}
			}.setSortable(true))

			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 03"), "col03") {
				@Override
				public boolean onCellRender(PersonVO bean, int rowNo) {
					return rowNo % 2 == 0;
				}
			})

			.add(new OLinkColumn<PersonVO>(new Model<>(""), new FontAwesome("arrows").spin()) {
				@Override
				public void onClick(final IModel<PersonVO> rowData) {
					sendResource(new OutputStreamResource("text", rowData.getObject().getCol02() + ".txt") {
						@Override
						protected void handleStream(OutputStream stream) {
							try {
								stream.write(rowData.getObject().getCol03().getBytes());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			})

			.add(new OColumn<PersonVO>(new Model<>("Any")) {
				@Override
				public String cellValue(PersonVO bean, int rowNo, int colNo, String url) {
					return String.format("<a href=\"#\" onclick=\"alert('%s')\">ANY</a>", bean.getCol01());
				}
			})

			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 04"), "col04"))

			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 05"), "col05"))
		;

		OGrid<PersonVO> options = new OGrid<>();
		options
			.setColumns(columns)
			.setMultiSort(true)
			.addToolbarButton(new OExportExcelButton(new FontAwesome("file-excel-o", "green", new Model<>("Export to excel")), "Export", 1000));
		//options.setWidth(OSize.fixed(800));
		//options.setWidth(OSize.percent(100));
		add(new WDataGrid<>("grid", options, this));
	}

	@Override
	public List<PersonVO> list(long first, long size, List<SortField> sortFields) {
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
}
