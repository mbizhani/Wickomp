package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.sql.result.RowVO;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WModel;
import org.devocative.wickomp.form.WAjaxButton;
import org.devocative.wickomp.form.code.OCode;
import org.devocative.wickomp.form.code.OCodeMode;
import org.devocative.wickomp.form.code.WCodeInput;
import org.devocative.wickomp.grid.*;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.service.DbService;

import java.util.Arrays;
import java.util.List;

public class SqlEditorPage extends BasePage implements IGridDataSource<RowVO> {
	private static final long serialVersionUID = -978809088812121353L;

	private WCodeInput sql;
	private WDataGrid<RowVO> grid;

	public SqlEditorPage() {
		sql = new WCodeInput("sql", new Model<>("select * from t_person"), new OCode(OCodeMode.SQL));
		Form<Void> form = new Form<>("form");
		form.add(sql);
		form.add(new WAjaxButton("exec") {
			private static final long serialVersionUID = 1902167679817410777L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				grid.setEnabled(true);
				grid.loadData(target);
			}
		});
		add(form);

		OGrid<RowVO> oGrid = new OGrid<>();
		oGrid
			.setPagingBarLayout(Arrays.asList(OPagingButtons.first, OPagingButtons.prev, OPagingButtons.next))
			.setPageList(Arrays.asList(5))
			.setWidth(OSize.percent(100))
			.setHeight(OSize.fixed(200));

		grid = new WDataGrid<>("grid", oGrid, this);
		grid
			.setAutomaticColumns(true)
			.setIgnoreDataSourceCount(true)
			.setEnabled(false);
		add(grid);
	}

	@Override
	public List<RowVO> list(long pageIndex, long pageSize, List<WSortField> sortFields) {
		String query = sql.getModelObject();
		return DbService.execute(query, pageIndex, pageSize);
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public IModel<RowVO> model(RowVO object) {
		return new WModel<>(object);
	}
}
