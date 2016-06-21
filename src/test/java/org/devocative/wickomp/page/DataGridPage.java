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
import org.devocative.wickomp.grid.toolbar.OExportExcelButton;
import org.devocative.wickomp.grid.toolbar.OGridGroupingButton;
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

public class DataGridPage extends BasePage implements IAsyncResponseHandler {
	private List<PersonVO> list;
	private AsyncBehavior asyncBehavior;
	WDataGrid<PersonVO> grid2;

	public DataGridPage() {
		list = PersonVO.list();

		OColumnList<PersonVO> columns = new OColumnList<>();
		columns
			//.add(new OCheckboxColumn<PersonVO>())
			.add(new OPropertyColumn<PersonVO>(new Model<>("Col01"), "col01").setHasFooter(true))

			.add(new OAjaxLinkColumn<PersonVO>(new Model<>("Col 02"), "col02") {
				@Override
				public void onClick(AjaxRequestTarget target, IModel<PersonVO> rowData) {
					target.appendJavaScript(String.format("alert(\"%s\");", rowData.getObject()));
				}
			}.setSortable(true))

			.add(new OLinkColumn<PersonVO>(new Model<>("Col 03"), "col03") {
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
				.setFormatter(ODateFormatter.prDateTime()))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Income"), "income")
				.setFormatter(ONumberFormatter.integer())
				.setHasFooter(true))
			.add(new OPropertyColumn<PersonVO>(new Model<>("Alive"), "alive")
				.setFormatter(OBooleanFormatter.bool()))
		;

		add(asyncBehavior = new AsyncBehavior(this));

		visibleGrid(columns);

		enabledGrid(columns);
	}

	@Override
	public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
		Map<String, Object> map = (Map<String, Object>) result;
		//if (grid2.getPageNum() == 3) {
		//	grid2.pushError(handler, new RuntimeException("DataGridPage: Page 3 Error"));
		//} else {
		grid2.pushData(handler, (List) map.get("list"), (int) map.get("count"), (List) map.get("footer"));
		//}
	}

	@Override
	public void onAsyncError(String handlerId, IPartialPageRequestHandler handler, Exception error) {
		grid2.pushError(handler, error);
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
			.addToolbarButton(new OGridGroupingButton<PersonVO>())
		;
		grid2Opt.setHeight(OSize.fixed(400));

		add(grid2 = new WDataGrid<>("grid2", grid2Opt, new IGridAsyncDataSource<PersonVO>() {

			@Override
			public void list(long first, long size, List<WSortField> sortFields) {
				asyncBehavior.sendAsyncRequest("GRID_PAGER",
					ObjectBuilder
						.<String, Object>createDefaultMap()
						.put("first", first)
						.put("size", size)
						.get()
				);
			}

			/*@Override
			public long count() {
				return list.size();
			}*/

			@Override
			public IModel<PersonVO> model(PersonVO object) {
				return new Model<>(object);
			}

		}));

		grid2.setEnabled(false);

		add(new AjaxLink("enableGrid2") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				grid2.setEnabled(true);
				grid2.loadData(target);
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
			.addToolbarButton(new OExportExcelButton<PersonVO>(new FontAwesome("file-excel-o", new Model<>("Export to excel")).setColor("green"), "Export.xlsx", 1000))
			.addToolbarButton(new OGridGroupingButton<PersonVO>());
		grid1Opt.setHeight(OSize.fixed(400));

		final WDataGrid<PersonVO> grid1;
		add(grid1 = new WDataGrid<>("grid1", grid1Opt, new IGridDataSource<PersonVO>() {
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

		}));
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
			@Override
			public void onClick(AjaxRequestTarget target) {
				grid1.makeVisible(target);
			}
		});
	}

}
