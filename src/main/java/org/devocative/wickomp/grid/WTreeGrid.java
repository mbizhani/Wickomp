package org.devocative.wickomp.grid;

import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.WTreeGridDataSource;
import org.devocative.wickomp.grid.column.OColumn;

import java.util.ArrayList;
import java.util.List;

public class WTreeGrid<T> extends WBaseGrid<T> {

	private WTreeGridDataSource<T> dataSource;
	private OTreeGrid<T> options;

	public WTreeGrid(String id, OTreeGrid<T> options, WTreeGridDataSource<T> dataSource) {
		super(id, options, dataSource);

		this.options = options;
		this.dataSource = dataSource;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (options.getIdField() == null || options.getTreeField() == null) {
			throw new RuntimeException("Both idField and treeField are required for WTreeGrid: componentId=" + getId());
		}
	}

	@Override
	protected String getJQueryFunction() {
		return "treegrid";
	}

	@Override
	protected void handleRowsById(String id) {
		List<RObject> subRow = new ArrayList<>();
		List<T> listByParent = dataSource.listByParent(id);
		for (T bean : listByParent) {
			RObject rObject = new RObject();
			List<OColumn<T>> columns = options.getColumns().getList();
			for (int colNo = 0; colNo < columns.size(); colNo++) {
				OColumn<T> column = columns.get(colNo);
				if (column.onCellRender(bean, id)) {
					String url = String.format("%s&id=%s&cn=%s&tp=cl", getCallbackURL(), id, colNo);
					rObject.addProperty(column.getField(), column.cellValue(bean, id, colNo, url));
				}
			}
			subRow.add(rObject);
		}
		sendJSONResponse(JsonUtil.toJson(subRow));
	}

	@Override
	protected void onBeanToRObject(T bean, RObject rObject) {
		if (dataSource != null) {
			if (dataSource.hasChildren(bean)) {
				rObject.addProperty("state", "closed");
			}
		}
	}
}
