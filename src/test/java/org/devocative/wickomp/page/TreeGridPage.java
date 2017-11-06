package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.TaskBehavior;
import org.devocative.wickomp.WModel;
import org.devocative.wickomp.async.IAsyncResponse;
import org.devocative.wickomp.grid.*;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.opt.IStyler;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.opt.OStyle;
import org.devocative.wickomp.service.DataService;
import org.devocative.wickomp.vo.EmployeeVO;

import java.io.Serializable;
import java.util.*;

public class TreeGridPage extends BasePage implements IAsyncResponse, ITreeGridAsyncDataSource<EmployeeVO> {
	private static final long serialVersionUID = 4726580534261868437L;

	private List<EmployeeVO> list;

	private TaskBehavior taskBehavior;
	private WTreeGrid<EmployeeVO> asyncTreeGrid;
	private OColumnList<EmployeeVO> columnList;

	public TreeGridPage() {
		list = EmployeeVO.list();

		columnList = new OColumnList<>();
		columnList
			.add(new OPropertyColumn<>(new Model<>("Id"), "eid"))
			.add(new OPropertyColumn<>(new Model<>("Name"), "name"))
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Age"), "age")
				.setCellStyler((IStyler<EmployeeVO> & Serializable) (bean, id) ->
					OStyle.style(bean.getAge() % 3 == 0 ? "background-color:khaki" : null)))
			.add(new OAjaxLinkColumn<EmployeeVO>(new Model<>(""), new HTMLBase("x")) {
				private static final long serialVersionUID = -8145936659329291146L;

				@Override
				public void onClick(AjaxRequestTarget target, IModel<EmployeeVO> rowData) {
					target.appendJavaScript(String.format("alert('%s');", rowData.getObject().getName()));
				}

				@Override
				public boolean onCellRender(EmployeeVO bean, String id) {
					try {
						int rowNo = Integer.parseInt(id);
						return rowNo % 2 == 0;
					} catch (NumberFormatException e) {
						return false;
					}
				}
			})
		;

		syncTreeGrid();

		asyncTreeGrid();
	}

	private void asyncTreeGrid() {
		OTreeGrid<EmployeeVO> treeGrid = new OTreeGrid<>();
		treeGrid
			.setTreeField("name")
			.setParentIdField("parentId")
			.setSingleSelect(true)
			.setSelectionJSHandler("function(rows){console.log(rows);}")
			.setIdField("eid")
			.setColumns(columnList)
			.setHeight(OSize.fixed(400));

		asyncTreeGrid = new WTreeGrid<>("atreegrid", treeGrid, this);
		asyncTreeGrid.setEnabled(false);
		add(asyncTreeGrid);

		add(new AjaxLink("enableTGrid") {
			private static final long serialVersionUID = 5052194326107554173L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				asyncTreeGrid.setEnabled(true);
				asyncTreeGrid.loadData(target);
			}
		});

		taskBehavior = new TaskBehavior(this);
		add(taskBehavior);
	}

	private void syncTreeGrid() {
		OTreeGrid<EmployeeVO> treeGrid = new OTreeGrid<>();
		treeGrid
			.setAnimate(true)
			.setShowLines(true)
			.setTreeField("name")
			.setSelectionJSHandler("function(rows){console.log(rows);}")
			.setSingleSelect(false)
				//.setParentIdField("parentId")
			.setIdField("eid")
			.setColumns(columnList)
			.setPagingBarLayout(Arrays.asList(OPagingButtons.refresh))
			.setRowStyler((IStyler<EmployeeVO> & Serializable) (bean, id) -> OStyle.style(bean.getAge() > 30 ? "color:red" : "color:green"))
			.setHeight(OSize.fixed(400));

		add(new WTreeGrid<>("treegrid", treeGrid, new ITreeGridDataSource<EmployeeVO>() {
			private static final long serialVersionUID = 4913273886461477605L;

			@Override
			public List<EmployeeVO> listByParent(Serializable parentId, List<WSortField> sortFields) {
				List<EmployeeVO> result = new ArrayList<>();
				for (int i = 1; i < 26; i++) {
					EmployeeVO emp = new EmployeeVO();
					emp.setEid(parentId + "." + i);
					emp.setName("E" + parentId + "." + i);
					emp.setAge((int) (Math.random() * 50));
					result.add(emp);
				}
				return result;
			}

			@Override
			public List<EmployeeVO> listByIds(Set<Serializable> ids, List<WSortField> sortFields) {
				return null;
			}

			@Override
			public boolean hasChildren(EmployeeVO bean) {
				return !bean.getEid().contains(".");
			}

			@Override
			public List<EmployeeVO> list(long first, long size, List<WSortField> sortFields) {
				int start = (int) ((first - 1) * size);
				int end = (int) (first * size);
				return list.subList(start, Math.min(end, list.size()));
			}

			@Override
			public long count() {
				return list.size();
			}

			@Override
			public IModel<EmployeeVO> model(EmployeeVO object) {
				return new WModel<>(object);
			}
		}));
	}

	// ---------------

	@Override
	public void onAsyncResult(IPartialPageRequestHandler handler, Object result) {
		Map<String, Object> map = (Map<String, Object>) result;
		if (map.containsKey("parentId")) {
			asyncTreeGrid.pushChildren(handler, (String) map.get("parentId"), (List) map.get("list"));
		} else if (map.containsKey("list")) {
			asyncTreeGrid.pushData(handler, (List) map.get("list"), (int) map.get("count"));
		}
	}

	@Override
	public void onAsyncError(IPartialPageRequestHandler handler, Exception error) {
	}

	// ---------------

	@Override
	public void asyncList(long pageIndex, long pageSize, List<WSortField> list) {
		Map<String, Object> map = ObjectBuilder
			.<String, Object>createDefaultMap()
			.put("first", pageIndex)
			.put("size", pageSize)
			.get();
		DataService.processEmployee(new DataService.RequestVO(taskBehavior, map));
	}

	@Override
	public void asyncListByParent(Serializable parentId, List<WSortField> list) {
		DataService.processSubEmployee(new DataService.RequestVO(taskBehavior, parentId.toString()));
	}

	@Override
	public boolean hasChildren(EmployeeVO bean) {
		return !bean.getEid().contains(".");
	}

	@Override
	public IModel<EmployeeVO> model(EmployeeVO object) {
		return new WModel<>(object);
	}
}
