package org.devocative.wickomp.grid;

import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.core.util.lang.PropertyResolver;
import org.devocative.wickomp.WebUtil;
import org.devocative.wickomp.data.RObject;
import org.devocative.wickomp.data.RObjectList;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WTreeGrid<T> extends WBaseGrid<T> {
	private static final long serialVersionUID = -1687156339596538069L;

	private static final String PARENT_ID_PROPERTY = "_parentId";
	private static final String STATE_PROPERTY = "state";

	// ------------------------------

	private OTreeGrid<T> options;
	private ITreeGridDataSource<T> treeGridDataSource;
	private ITreeGridAsyncDataSource<T> treeGridAsyncDataSource;
	private boolean assertParentNotFound = true;

	// ------------------------------

	// Main Constructor 1
	public WTreeGrid(String id, OTreeGrid<T> options, ITreeGridDataSource<T> treeGridDataSource) {
		super(id, options, treeGridDataSource);

		this.options = options;
		this.treeGridDataSource = treeGridDataSource;
	}

	// Main Constructor 2
	public WTreeGrid(String id, OTreeGrid<T> options, ITreeGridAsyncDataSource<T> treeGridAsyncDataSource) {
		super(id, options, treeGridAsyncDataSource);

		this.options = options;
		this.treeGridAsyncDataSource = treeGridAsyncDataSource;
	}

	//---------------- PUBLIC METHODS


	public WTreeGrid<T> setAssertParentNotFound(boolean assertParentNotFound) {
		this.assertParentNotFound = assertParentNotFound;
		return this;
	}

	public WTreeGrid<T> pushChildren(IPartialPageRequestHandler handler, String parentId, List<T> children) {
		RObjectList subRow = new RObjectList();
		convertBeansToRObjects(children, subRow);

		String script = String.format("$('#%s').%s('append', {parent:'%s', data:%s});",
			getMarkupId(), getJQueryFunction(), parentId, WebUtil.toJson(subRow));

		logger.debug("WTreeGrid.pushChildren: {}", script);

		handler.appendJavaScript(script);
		return this;
	}

	//---------------- PROTECTED METHODS

	@Override
	protected void onInitialize() {
		super.onInitialize();

		if (options.getIdField() == null || options.getTreeField() == null || options.getParentIdField() == null) {
			throw new RuntimeException("'idField', 'parentIdField', and 'treeField' are required for WTreeGrid: componentId=" + getId());
		}
	}

	@Override
	protected String getJQueryFunction() {
		return "wTreeGrid";
	}

	@Override
	protected void handleRowsById(String id) {

		if (treeGridDataSource != null) {
			List<T> listByParent = treeGridDataSource.listByParent(id, sortFieldList);

			RObjectList subRow = new RObjectList();
			convertBeansToRObjects(listByParent, subRow);
			sendJSONResponse(WebUtil.toJson(subRow));
		} else {
			treeGridAsyncDataSource.asyncListByParent(id, sortFieldList);
			sendJSONResponse("");
		}

	}

	@Override
	protected void onAfterBeanToRObject(T bean, RObject rObject) {
		if (treeGridDataSource != null) {
			if (treeGridDataSource.hasChildren(bean)) {
				rObject.addProperty(STATE_PROPERTY, "closed");
			}
		} else {
			if (treeGridAsyncDataSource.hasChildren(bean)) {
				rObject.addProperty(STATE_PROPERTY, "closed");
			}
		}

		Serializable parentId = (Serializable) PropertyResolver.getValue(options.getParentIdField(), bean);
		if (parentId != null) {
			rObject.addProperty(PARENT_ID_PROPERTY, parentId.toString());
		}
	}

	@Override
	protected RObjectList createRObjectList(List<T> data) {
		pageData.clear();

		RObjectList rObjectList = new RObjectList();
		convertBeansToRObjects(data, rObjectList);

		if (treeGridDataSource != null) {
			Set<Serializable> parentIds;
			do {
				parentIds = new HashSet<>();
				for (T bean : data) {
					Serializable parentId = (Serializable) PropertyResolver.getValue(options.getParentIdField(), bean);
					if (parentId != null) {
						if (!rObjectList.hasRObject(parentId.toString())) {
							parentIds.add(parentId);
						}
					}
				}

				if (parentIds.size() > 0) {
					data = treeGridDataSource.listByIds(parentIds, sortFieldList);
					if (data.size() < parentIds.size()) {
						logger.warn("WTreeGrid -> finding parents -> missing some parent(s) = {} (parent ids = {}) ",
							parentIds.size() - data.size(), parentIds);
					}
					convertBeansToRObjects(data, rObjectList);
				}
			} while (parentIds.size() > 0);
		}

		for (RObject rObject : rObjectList.getValue()) {
			Object parentId = rObject.getProperty(PARENT_ID_PROPERTY);
			if (parentId != null) {
				String parentIdStr = parentId.toString();
				if (rObjectList.hasRObject(parentIdStr)) {
					rObjectList.getRObject(parentIdStr).removeProperty(STATE_PROPERTY);
				} else {
					logger.error("WTreeGrid (id={}) parent not found: ", getId(), rObject);
					if (assertParentNotFound) {
						throw new WParentNotFoundException("Parent not found: row = \n" + rObject);
					}
				}
			}
		}

		return rObjectList;
	}
}
