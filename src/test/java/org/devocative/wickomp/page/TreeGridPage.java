package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.adroit.obuilder.ObjectBuilder;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WModel;
import org.devocative.wickomp.async.AsyncBehavior;
import org.devocative.wickomp.async.IAsyncResponseHandler;
import org.devocative.wickomp.grid.*;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.html.HTMLBase;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.vo.EmployeeVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeGridPage extends BasePage implements IAsyncResponseHandler {
	private List<EmployeeVO> list;

	private AsyncBehavior asyncBehavior;
	private WTreeGrid<EmployeeVO> atreegrid;
	private OColumnList<EmployeeVO> columnList;

	public TreeGridPage() {
		list = EmployeeVO.list();

		add(asyncBehavior = new AsyncBehavior(this));

		columnList = new OColumnList<>();
		columnList
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Id"), "eid"))
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Name"), "name"))
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Age"), "age"))
			.add(new OAjaxLinkColumn<EmployeeVO>(new Model<>(""), new HTMLBase("x")) {
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
				//.setParentIdField("parentId")
			.setIdField("eid")
			.setColumns(columnList)
			.setHeight(OSize.fixed(400));

		add(atreegrid = new WTreeGrid<>("atreegrid", treeGrid, new ITreeGridAsyncDataSource<EmployeeVO>() {
			@Override
			public void list(long pageIndex, long pageSize, List<WSortField> list) {
				asyncBehavior.sendAsyncRequest("T_GRID_PAGER",
					ObjectBuilder
						.<String, Object>createDefaultMap()
						.put("first", pageIndex)
						.put("size", pageSize)
						.get()
				);
			}

			@Override
			public void listByParent(Serializable parentId, List<WSortField> list) {
				asyncBehavior.sendAsyncRequest("T_GRID_CHILDREN", parentId);
			}

			@Override
			public boolean hasChildren(EmployeeVO bean) {
				return !bean.getEid().contains(".");
			}

			@Override
			public IModel<EmployeeVO> model(EmployeeVO object) {
				return new WModel<>(object);
			}
		}));

		atreegrid.setEnabled(false);

		add(new AjaxLink("enableTGrid") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				atreegrid.setEnabled(true);
				atreegrid.loadData(target);
			}
		});
	}

	private void syncTreeGrid() {
		OTreeGrid<EmployeeVO> treeGrid = new OTreeGrid<>();
		treeGrid
			.setShowLines(true)
			.setTreeField("name")
				//.setParentIdField("parentId")
			.setIdField("eid")
			.setColumns(columnList)
			.setHeight(OSize.fixed(400));

		add(new WTreeGrid<>("treegrid", treeGrid, new ITreeGridDataSource<EmployeeVO>() {
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

	@Override
	public void onAsyncResult(String handlerId, IPartialPageRequestHandler handler, Serializable result) {
		if ("T_GRID_PAGER".equals(handlerId)) {
			Map<String, Object> map = (Map<String, Object>) result;
			atreegrid.pushData(handler, (List) map.get("list"), (int) map.get("count"));
		} else if ("T_GRID_CHILDREN".equals(handlerId)) {
			Map<String, Object> map = (Map<String, Object>) result;
			atreegrid.pushChildren(handler, (String) map.get("parentId"), (List) map.get("list"));
		}
	}

	@Override
	public void onAsyncError(String handlerId, IPartialPageRequestHandler handler, Exception error) {
	}
}
