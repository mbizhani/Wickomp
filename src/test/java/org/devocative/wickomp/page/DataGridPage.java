package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.async.AsyncBehavior;
import org.devocative.wickomp.async.IAsyncResponseHandler;
import org.devocative.wickomp.formatter.OBooleanFormatter;
import org.devocative.wickomp.formatter.ODateFormatter;
import org.devocative.wickomp.formatter.ONumberFormatter;
import org.devocative.wickomp.grid.*;
import org.devocative.wickomp.grid.column.OColumn;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OHiddenColumn;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.grid.column.link.OLinkColumn;
import org.devocative.wickomp.grid.toolbar.OAjaxLinkButton;
import org.devocative.wickomp.grid.toolbar.OExportExcelButton;
import org.devocative.wickomp.grid.toolbar.OGridGroupingButton;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.html.icon.FontAwesome;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.resource.OutputStreamResource;
import org.devocative.wickomp.vo.PersonVO;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataGridPage extends BasePage implements IAsyncResponseHandler, IGridDataSource<PersonVO>, IGridAsyncDataSource<PersonVO> {
	private static final long serialVersionUID = 7457034189424340046L;

	private List<PersonVO> list;
	private AsyncBehavior asyncBehavior;
	private WDataGrid<PersonVO> asyncDisabledGrid;

	public DataGridPage() {
		list = PersonVO.list();

		OColumnList<PersonVO> columns = new OColumnList<>();
		columns
			//.add(new OCheckboxColumn<PersonVO>())
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col01"), "col01").setHasFooter(true))

			.add(new OAjaxLinkColumn<PersonVO>(new Model<>("Col 02"), "col02") {
				private static final long serialVersionUID = -5779390659947204393L;

				@Override
				public void onClick(AjaxRequestTarget target, IModel<PersonVO> rowData) {
					target.appendJavaScript(String.format("alert(\"%s\");", rowData.getObject()));
				}
			}.setSortable(true))

			.add(new OAjaxLinkColumn<PersonVO>(new Model<>("Err"), new HTMLBase("Err")) {
				private static final long serialVersionUID = -6638861337384724243L;

				@Override
				public void onClick(AjaxRequestTarget target, IModel<PersonVO> rowData) {
					throw new RuntimeException("Oops!");
				}
			})

			.add(new OLinkColumn<PersonVO>(new Model<>("Col 03"), "col03") {
				private static final long serialVersionUID = -1345514997907642145L;

				@Override
				public void onClick(IModel<PersonVO> rowData) {
					System.out.println(rowData.getObject());
				}

				@Override
				public boolean onCellRender(PersonVO bean, String id) {
					try {
						int rowNo = Integer.parseInt(id);
						return rowNo % 2 == 0;
					} catch (NumberFormatException e) {
						return true;
					}
				}
			})

			.add(new OLinkColumn<PersonVO>(new Model<>(""), new FontAwesome("arrows").spin()) {
				private static final long serialVersionUID = -377768531033482382L;

				@Override
				public void onClick(final IModel<PersonVO> rowData) {
					sendResource(new OutputStreamResource("text", rowData.getObject().getCol02() + ".txt") {
						private static final long serialVersionUID = 5226293987632481972L;

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
			}.setField("cmd10")) // NOTE: it is ok for column with HTML content!

			.add(new OColumn<PersonVO>(new Model<>("Any")) {
				private static final long serialVersionUID = -7363343390885987603L;

				@Override
				public String cellValue(PersonVO bean, String id, int colNo, String url) {
					return String.format("<a href=\"#\" onclick=\"alert('%s')\">ANY</a>", bean.getCol01());
				}

				@Override
				public String footerCellValue(Object bean, int colNo, String url) {
					return null;
				}
			})

			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 04"), "col04"))

			.add(new OHiddenColumn<PersonVO>("col05"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Birth Date"), "birthDate")
				.setFormatter(ODateFormatter.prDateTime())
				.setStyle("direction:ltr;"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Income"), "income")
				.setFormatter(ONumberFormatter.integer())
				.setHasFooter(true)
				.setStyle("direction:ltr;"))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Alive"), "alive")
				.setFormatter(OBooleanFormatter.bool()))
		;

		add(asyncBehavior = new AsyncBehavior(this));

		activeGrid(columns);

		visibleGrid(columns);

		enabledGrid(columns);
	}

	@Override
	public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
		Map<String, Object> map = (Map<String, Object>) result;
		//if (grid2.getPageNum() == 3) {
		//	asyncDisabledGrid.pushError(handler, new RuntimeException("DataGridPage: Page 3 Error"));
		//} else {
		asyncDisabledGrid.pushData(handler, (List) map.get("list"), (int) map.get("count"), (List) map.get("footer"));
		//}
	}

	@Override
	public void onAsyncError(String handlerId, IPartialPageRequestHandler handler, Exception error) {
		asyncDisabledGrid.pushError(handler, error);
	}

	private void activeGrid(OColumnList<PersonVO> columns) {
		OGrid<PersonVO> grid2Opt = new OGrid<>();
		grid2Opt
			.setGroupStyle("background-color:#dddddd")
			.setIdField("col02")
			.setColumns(columns)
			.setMultiSort(true)
			.setSelectionIndicator(true)
			.setSelectionJSHandler("function(asd){alert(asd.toSource());}")
			.setShowFooter(true)
			.addToolbarButton(new OGridGroupingButton<PersonVO>(new FontAwesome("expand"), new FontAwesome("compress")))
		;
		grid2Opt.setHeight(OSize.fixed(400));

		add(new WDataGrid<>("activeAsyncGrid", grid2Opt, (IGridDataSource<PersonVO>) this));
	}

	private void enabledGrid(OColumnList<PersonVO> columns) {
		OGrid<PersonVO> grid2Opt = new OGrid<>();
		grid2Opt
			.setGroupStyle("background-color:#dddddd")
			.setIdField("col02")
			.setColumns(columns)
			.setMultiSort(true)
			.setSelectionIndicator(true)
			.setSelectionJSHandler("function(asd){alert(asd.toSource());}")
			.setShowFooter(true)
			.addToolbarButton(new OGridGroupingButton<PersonVO>(new FontAwesome("expand"), new FontAwesome("compress")))
		;
		grid2Opt.setHeight(OSize.fixed(400));

		add(asyncDisabledGrid = new WDataGrid<>("grid2", grid2Opt, (IGridAsyncDataSource<PersonVO>) this));

		asyncDisabledGrid
			.setHideToolbarFirstTime(false)
			.setEnabled(false)
		;

		add(new AjaxLink("enableGrid2") {
			private static final long serialVersionUID = 919206334855897779L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				asyncDisabledGrid.setEnabled(true);
				asyncDisabledGrid.loadData(target);
			}
		});
	}

	private void visibleGrid(OColumnList<PersonVO> columns) {
		OGrid<PersonVO> grid1Opt = new OGrid<>();
		grid1Opt
			.setColumns(columns)
			.setMultiSort(true)
			.setIdField("col02")
				// .setGroupField("col01")
			.setShowFooter(true)
			.addToolbarButton(new OExportExcelButton<>(new FontAwesome("file-excel-o", new Model<>("Export to excel")).setColor("green"), this))
			.addToolbarButton(new OAjaxLinkButton<PersonVO>(new HTMLBase("Ajax")) {
				private static final long serialVersionUID = -5973292922018513236L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					target.appendJavaScript("alert('Button');");
				}
			})
			.addToolbarButton(new OGridGroupingButton<PersonVO>(new FontAwesome("expand"), new FontAwesome("compress")));
		grid1Opt.setHeight(OSize.fixed(400));

		final WDataGrid<PersonVO> grid1;
		add(grid1 = new WDataGrid<>("grid1", grid1Opt, (IGridDataSource<PersonVO>) this));
		grid1.setVisible(false);
		grid1.setOutputMarkupPlaceholderTag(true);
		grid1.setFooterDataSource(new IGridFooterDataSource<PersonVO>() {
			@Override
			public List<?> footer(List<PersonVO> pagedData) {
				List list = new ArrayList<>();
				PersonVO agg = new PersonVO();
				agg.setCol01("Sum");
				agg.setIncome(1000L);
				list.add(agg);
				return list;
			}
		});

		add(new AjaxLink("showGrid") {
			private static final long serialVersionUID = 4096673362021657583L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				grid1.makeVisible(target);
			}
		});
	}

	// ------------------------------ IGridAsyncDataSource<PersonVO>

	@Override
	public void asyncList(long first, long size, List<WSortField> sortFields) {
		asyncBehavior.sendAsyncRequest("GRID_PAGER",
			ObjectBuilder
				.<String, Object>createDefaultMap()
				.put("first", first)
				.put("size", size)
				.get());
	}

	// ------------------------------ IGridDataSource<PersonVO>

	@Override
	public List<PersonVO> list(long first, long size, List<WSortField> sortFields) {
		if (first == 3) {
			throw new RuntimeException("Test Exception!");
		}
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
