package org.devocative.wickomp.demo.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.sql.result.RowVO;
import org.devocative.wickomp.WModel;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.async.IAsyncResponse;
import org.devocative.wickomp.async.WebSocketDelayedResponse;
import org.devocative.wickomp.async.WebSocketToken;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.service.DbService;
import org.devocative.wickomp.form.WAjaxButton;
import org.devocative.wickomp.form.code.OCode;
import org.devocative.wickomp.form.code.OCodeMode;
import org.devocative.wickomp.form.code.WCodeInput;
import org.devocative.wickomp.grid.*;
import org.devocative.wickomp.opt.OSize;

import java.util.Arrays;
import java.util.List;

public class SqlEditorPage extends BasePage implements IGridAsyncDataSource<RowVO>, IAsyncResponse {
	private static final long serialVersionUID = -978809088812121353L;

	private WCodeInput sql;
	private WDataGrid<RowVO> grid;
	private WebSocketToken token;

	public SqlEditorPage() {
		sql = new WCodeInput("sql", new Model<>("select * from t_person"), new OCode(OCodeMode.SQL).setSubmitSelection(true));
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
		form.add(new WebMarkupContainer("clear").add(new AttributeModifier("onclick", sql.getClearJSCall())));
		add(form);

		OGrid<RowVO> oGrid = new OGrid<>();
		oGrid
			.setPagingBarLayout(Arrays.asList(OPagingButtons.first, OPagingButtons.prev, OPagingButtons.next))
			.setNoResultMessage("No result")
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
	protected void onBeforeRender() {
		super.onBeforeRender();

		token = WebUtil.createWSToken(this);
	}

	@Override
	public void asyncList(long pageIndex, long pageSize, List<WSortField> sortFields) {
		System.out.println("###> SqlEditorPage.asyncList");

		String query = sql.getModelObject();

		new Thread() {
			@Override
			public void run() {
				/*try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				List<RowVO> execute = DbService.execute(query, pageIndex, pageSize);
				WebUtil.wsPush(token, new WebSocketDelayedResponse(SqlEditorPage.this, execute));
			}
		}.start();
	}

	@Override
	public IModel<RowVO> model(RowVO object) {
		return new WModel<>(object);
	}

	@Override
	public void onAsyncResult(IPartialPageRequestHandler handler, Object result) {
		List<RowVO> execute = (List<RowVO>) result;
		grid.pushData(handler, execute);
	}

	@Override
	public void onAsyncError(IPartialPageRequestHandler handler, Exception e) {
		grid.pushError(handler, e);
	}
}
