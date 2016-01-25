package org.devocative.wickomp.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.devocative.wickomp.BasePage;
import org.devocative.wickomp.WModel;
import org.devocative.wickomp.data.WSortField;
import org.devocative.wickomp.data.WTreeGridDataSource;
import org.devocative.wickomp.grid.OTreeGrid;
import org.devocative.wickomp.grid.WTreeGrid;
import org.devocative.wickomp.grid.column.OColumnList;
import org.devocative.wickomp.grid.column.OPropertyColumn;
import org.devocative.wickomp.grid.column.link.OAjaxLinkColumn;
import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.vo.EmployeeVO;

import java.util.ArrayList;
import java.util.List;

public class TreeGridPage extends BasePage {
	private List<EmployeeVO> list;

	public TreeGridPage() {
		list = EmployeeVO.list();

		OColumnList<EmployeeVO> columnList = new OColumnList<>();
		columnList
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Id"), "id"))
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Name"), "name"))
			.add(new OPropertyColumn<EmployeeVO>(new Model<>("Age"), "age"))
			.add(new OAjaxLinkColumn<EmployeeVO>(new Model<>("Ageeee"), "age") {
				@Override
				public void onClick(AjaxRequestTarget target, IModel<EmployeeVO> rowData) {
					target.appendJavaScript(String.format("alert('%s');", rowData.getObject().getName()));
				}

				@Override
				public boolean onCellRender(EmployeeVO bean, String id) {
					int rowNo = Integer.parseInt(id);
					return rowNo % 2 == 0;
				}
			})
		;

		OTreeGrid<EmployeeVO> treeGrid = new OTreeGrid<>();
		treeGrid
			.setTreeField("name")
			.setIdField("id")
			.setColumns(columnList)
			.setHeight(OSize.fixed(300));

		add(new WTreeGrid<>("treegrid", treeGrid, new WTreeGridDataSource<EmployeeVO>() {
			@Override
			public List<EmployeeVO> listByParent(String parentId) {
				List<EmployeeVO> result = new ArrayList<>();
				for (int i = 1; i < 6; i++) {
					EmployeeVO emp = new EmployeeVO();
					emp.setId(parentId + "." + i);
					emp.setName("E" + parentId + "." + i);
					emp.setAge((int) (Math.random() * 50));
					result.add(emp);
				}
				return result;
			}

			@Override
			public boolean hasChildren(EmployeeVO bean) {
				return !bean.getId().contains(".");
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
				return new WModel<EmployeeVO>(object);
			}
		}));
	}
}
