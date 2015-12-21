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
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;

import java.util.List;

public class HomePage extends WebPage implements DataSource<PersonVO> {
	private static final long serialVersionUID = 1L;

	private List<PersonVO> list;

	public HomePage() {
		list = PersonVO.list();

		OColumnList<PersonVO> columns = new OColumnList<PersonVO>();
		columns
				.add(new OLinkColumn<PersonVO>(new Model<String>("Col01"), "col01") {
					@Override
					public void onClick(IModel<PersonVO> rowData) {
						System.out.println(rowData.getObject());
					}
				}.setSortable(true))
				.add(new OAjaxLinkColumn<PersonVO>(new Model<String>("Col 02"), "col02") {
					@Override
					public void onClick(AjaxRequestTarget target, IModel<PersonVO> rowData) {
						target.appendJavaScript(String.format("alert(\"%s\");", rowData.getObject()));
					}
				}.setSortable(true))
				.add(new OPropertyColumn<PersonVO>(new Model<String>("Col 03"), "col03") {
					@Override
					public boolean onCellRender(PersonVO bean, int rowNo) {
						return rowNo % 2 == 0;
					}
				})

/*
				.add(new OLinkColumn<PersonVO>(new Model<String>(""), new HTMLBase("DL")) {
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
*/
				.add(new OPropertyColumn<PersonVO>(new Model<String>("Col 04"), "col04"))
				.add(new OPropertyColumn<PersonVO>(new Model<String>("Col 05"), "col05"));

		OGrid<PersonVO> options1 = new OGrid<PersonVO>();
		options1
				.setColumns(columns)
				.setMultiSort(true);
		//options1.setWidth(OSize.fixed(800));
		//options1.setWidth(OSize.percent(100));
		add(new WDataGrid<PersonVO>("grid1", options1, this));

		/*OGrid<PersonVO> options2 = new OGrid<PersonVO>();
				options2.setColumns(columns);
		add(new WDataGrid<PersonVO>("grid2", options2, this));*/
	}

	@Override
	public List<PersonVO> list(long first, long size, List<SortField> sortFields) {
		int start = (int) (first * size);
		int end = (int) ((first + 1) * size);
		return list.subList(start, Math.min(end, list.size()));
	}

	@Override
	public long count() {
		return list.size();
	}

	@Override
	public IModel<PersonVO> model(PersonVO object) {
		return new Model<PersonVO>(object);
	}
}
