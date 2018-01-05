package org.devocative.wickomp.demo.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.CalendarUtil;
import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.adroit.vo.DateFieldVO;
import org.devocative.wickomp.async.IAsyncResponse;
import org.devocative.wickomp.demo.BasePage;
import org.devocative.wickomp.demo.TaskBehavior;
import org.devocative.wickomp.demo.service.DataService;
import org.devocative.wickomp.demo.vo.PersonVO;
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
import org.devocative.wickomp.opt.IStyler;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.opt.OStyle;
import org.devocative.wickomp.resource.OutputStreamResource;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

public class DataGridPage extends BasePage implements IAsyncResponse, IGridDataSource<PersonVO>, IGridAsyncDataSource<PersonVO> {
	private static final long serialVersionUID = 7457034189424340046L;

	private List<PersonVO> list;
	private TaskBehavior taskBehavior;
	private WDataGrid<PersonVO> asyncDisabledGrid;

	// ------------------------------

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
			}.setConfirmMessage("R u sure?").setSortable(true))

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

			.add(new OPropertyColumn<PersonVO>(new Model<>("Col 04"), "col04").setShowAsTooltip(true))

			.add(new OHiddenColumn<>("col05"))

			.add(new OPropertyColumn<PersonVO>(new Model<>("Birth Date"), "v_birthDate", "birthDate")
					.setFormatter(ODateFormatter.prDateTime())
					.setStyle("direction:ltr;")
			)
			.add(new OHiddenColumn<PersonVO>("birthDate")
					.setFormatter(ODateFormatter.millis())
			)

			.add(new OPropertyColumn<PersonVO>(new Model<>("Income"), "v_income", "income")
					.setFormatter(ONumberFormatter.integer())
					.setHasFooter(true)
					.setWidth(OSize.fixed(50))
					.setStyle("direction:ltr;background-color:#dddddd; color:blue")
					.setCellStyler((IStyler<PersonVO> & Serializable) (bean, id) -> OStyle.style(bean.getIncome() < 0 ? "color:red" : null))
			)

			.add(new OHiddenColumn<>("income"))

			.add(new OPropertyColumn<PersonVO>(new Model<>("Expense"), "expense")
					.setFormatter(ONumberFormatter.real())
					.setHasFooter(true)
					.setStyle("direction:ltr;")
					.setCellStyler((IStyler<PersonVO> & Serializable) (bean, id) -> OStyle.css(bean.getExpense().longValue() > 100000 ? "high" : null))
			)

			.add(new OPropertyColumn<PersonVO>(new Model<>("Alive"), "alive")
				.setFormatter(OBooleanFormatter.bool()))
		;

		add(taskBehavior = new TaskBehavior(this)

		);

		activeGrid(columns);

		visibleGrid(columns);

		enabledGrid(columns);

	}

	// ------------------------------

	@Override
	public void onAsyncResult(IPartialPageRequestHandler handler, Object result) {
		Map<String, Object> map = (Map<String, Object>) result;
		//if (grid2.getPageNum() == 3) {
		//	asyncDisabledGrid.pushError(handler, new RuntimeException("DataGridPage: Page 3 Error"));
		//} else {
		asyncDisabledGrid.pushData(handler, (List) map.get("list"), (int) map.get("count"), (List) map.get("footer"));
		//}
	}

	@Override
	public void onAsyncError(IPartialPageRequestHandler handler, Exception error) {
		asyncDisabledGrid.pushError(handler, error);
	}

	// ------------------------------

	private void activeGrid(OColumnList<PersonVO> columns) {
		OGrid<PersonVO> grid2Opt = new OGrid<>();
		grid2Opt
			.setGroupStyle("background-color:#dddddd")
			.setIdField("col02")
			.setColumns(columns)
			.setMultiSort(true)
			.setSingleSelect(false)
				//.setSelectionIndicator(true)
				//.setSelectionJSHandler("function(rows){console.log(rows);}")
			.setShowFooter(true)
			.addToolbarButton(new OGridGroupingButton<>(new FontAwesome("expand"), new FontAwesome("compress")))
			.setColumnReorder(false) //NOTE
			.setGridId("activeAsyncGrid")
			.setRowStyler((IStyler<PersonVO> & Serializable) (bean, id) -> {
				Date base = CalendarUtil.toGregorian(new DateFieldVO(1360, 1, 1));
				if (bean.getBirthDate().getTime() < base.getTime()) {
					return OStyle.style("font-weight: bold;background-color:#FFD4D4");
				}
				return null;
			})
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
				//.setSelectionIndicator(true)
			.setSelectionJSHandler("function(asd){console.log(asd);}")
			.setShowFooter(true)
			.setAutoTooltip(false)
			.addToolbarButton(new OGridGroupingButton<>(new FontAwesome("expand"), new FontAwesome("compress")))
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
			.addToolbarButton(new OGridGroupingButton<>(new FontAwesome("expand"), new FontAwesome("compress")))
			.setReorderColumns(Arrays.asList("col02", "col01")) //NOTE
			.setCallbackOnColumnReorder(true) //NOTE
			.setRowStyler((IStyler<PersonVO> & Serializable) (bean, id) -> {
				Date base = CalendarUtil.toGregorian(new DateFieldVO(1360, 1, 1));
				if (bean.getBirthDate().getTime() < base.getTime()) {
					return OStyle.css("old");
				}
				return null;
			})

		;
		grid1Opt.setHeight(OSize.fixed(400));

		final WDataGrid<PersonVO> grid1;
		add(grid1 = new WDataGrid<PersonVO>("grid1", grid1Opt, (IGridDataSource<PersonVO>) this) {
			private static final long serialVersionUID = 2821125315479982045L;

			@Override
			protected void onColumnReorder(List<String> columns) {
				logger.info("Visible Grid Column Reorder: {}", columns);
			}
		});
		grid1.setVisible(false);
		grid1.setOutputMarkupPlaceholderTag(true);
		grid1.setFooterDataSource(pagedData -> {
			List list1 = new ArrayList<>();
			PersonVO agg = new PersonVO();
			agg.setCol01("Sum");
			agg.setIncome(1000L);
			list1.add(agg);
			return list1;
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
		Map<String, Object> map = ObjectBuilder
			.<String, Object>createDefaultMap()
			.put("first", first)
			.put("size", size)
			.get();
		DataService.processPerson(new DataService.RequestVO(taskBehavior, map));
	}

	// ------------------------------ IGridDataSource<PersonVO>

	@Override
	public List<PersonVO> list(long first, long size, List<WSortField> sortFields) {
		if (first == 4) {
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
