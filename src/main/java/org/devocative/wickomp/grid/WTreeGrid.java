package org.devocative.wickomp.grid;

import org.apache.wicket.core.util.lang.PropertyResolver;
import org.devocative.wickomp.JsonUtil;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.RObjectList;
import org.devocative.wickomp.data.WTreeGridDataSource;
import org.devocative.wickomp.grid.column.OColumn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WTreeGrid<T> extends WBaseGrid<T> {
	private static final String PARENT_ID_PROPERTY = "_parentId";
	private static final String STATE_PROPERTY = "state";

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
		List<T> listByParent = dataSource.listByParent(id, sortFieldList);
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
	protected RObjectList createRObjectList(List<T> data) {
		pageData.clear();

		RObjectList objectList = new RObjectList();
		convertBeansToRObjects(data, objectList);

		if (options.getParentIdField() != null) {
			Set<Serializable> parentIds;
			do {
				parentIds = new HashSet<>();
				for (T bean : data) {
					Serializable beanId = (Serializable) PropertyResolver.getValue(options.getIdField(), bean);
					RObject rObject = objectList.getRObject(beanId.toString());
					if (dataSource.hasChildren(bean)) {
						rObject.addProperty(STATE_PROPERTY, "closed");
					}

					Serializable parentId = (Serializable) PropertyResolver.getValue(options.getParentIdField(), bean);
					if (parentId != null) {
						rObject.addProperty(PARENT_ID_PROPERTY, parentId.toString());

						if (!objectList.hasRObject(parentId.toString())) {
							parentIds.add(parentId);
						}
					}
				}

				if (parentIds.size() > 0) {
					data = dataSource.listByIds(parentIds, sortFieldList);
					convertBeansToRObjects(data, objectList);
				}
			} while (parentIds.size() > 0);

			for (RObject rObject : objectList.getValue()) {
				String parentId = rObject.getProperty(PARENT_ID_PROPERTY);
				if (parentId != null) {
					objectList.getRObject(parentId).removeProperty(STATE_PROPERTY);
				}
			}
		}

		return objectList;
	}
}
