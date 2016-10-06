var gridDefaultView;

function changeGridGroupField(select, gridId) {
	var opt = $('#' + gridId).datagrid('options');

	if (!gridDefaultView) {
		gridDefaultView = opt.view;
	}

	if (select.value != "") {
		//TODO: checking !opt.groupStyler is based on unknown bug where opt.groupStyler is null using in Metis project!
		if (opt.groupStyle || !opt.groupStyler) {
			opt.groupStyler = function (value, rows) {
				return (opt.groupStyle) ? opt.groupStyle : '';
			};
		}
		opt.view = groupview;
		opt.groupField = select.value;
		opt.groupFormatter = function (value, rows) {
			//return value + " (" + rows.length + ")";
			return "<table><tr><td>" + value + " </td><td> #[</td><td>" + rows.length + "</td><td>]</td></tr></table>"
		};
		$('#' + gridId).datagrid('sort', {sortName: select.value, sortOrder: 'asc'});
	} else {
		opt.view = gridDefaultView;
		opt.groupField = '';
		$('#' + gridId).datagrid('reload');
	}
}

function expandAllGroups(gridId) {
	var grid = $('#' + gridId);
	if (grid.datagrid('options').view.groups) {
		var groupsCount = grid.datagrid('options').view.groups.length;
		for (var i = 0; i < groupsCount; i++) {
			grid.datagrid('expandGroup', i);
		}
	}
}

function collapseAllGroups(gridId) {
	var grid = $('#' + gridId);
	if (grid.datagrid('options').view.groups) {
		var groupsCount = grid.datagrid('options').view.groups.length;
		for (var i = 0; i < groupsCount; i++) {
			grid.datagrid('collapseGroup', i);
		}
	}
}

function handleLoaded(gridId, data) {
	if (data.error) {
		$.messager.alert('<i class="fa fa-exclamation-triangle" style="color:#aa1111"></i>', data.error);
	}

	$('#' + gridId).datagrid('loaded');

	return data;
}

function handleSelectionIndicator(gridId, selectionHandler, enableDblClickSelection) {
	var grid = $('#' + gridId);
	var noOfSelected = grid.datagrid('getSelections').length.toString();
	var butts = [
		{
			text: noOfSelected,
			width: 60,
			iconCls: "fa fa-check-square-o",
			handler: function () {
				var tb = '<table border="1" cellspacing="0"><tr>';
				var fields = grid.datagrid('getColumnFields');
				for (var i = 0; i < fields.length; i++) {
					tb += '<th>' + fields[i] + '</th>';
				}
				var selData = grid.datagrid('getSelections');
				for (var d = 0; d < selData.length; d++) {
					tb += '<tr>';
					for (var f = 0; f < fields.length; f++) {
						tb += '<td>' + selData[d][fields[f]] + '</td>';
					}
					tb += '</tr>';
				}
				tb += '</tr>';
				tb += '</table>';
				$.messager.show({
					title: '<i class="fa fa-check-square-o"></i>',
					msg: tb,
					timeout: 0,
					showType: 'show',
					width: 600,
					height: 400,
					style: {right: '', bottom: ''}
				});
			}
		}
	];

	if (selectionHandler) {
		if (enableDblClickSelection) {
			if (grid.datagrid('options')['treeField']) {
				grid.datagrid('options').onDblClickRow = function (row) {
					var arr = [];
					arr.push(row);
					handleSelection(grid, selectionHandler, arr, true);
				};
			} else {
				grid.datagrid('options').onDblClickRow = function (index, row) {
					var arr = [];
					arr.push(row);
					handleSelection(grid, selectionHandler, arr, true);
				};
			}
		}

		butts.push({
			width: 25,
			iconCls: "fa fa-paper-plane-o",
			handler: function () {
				handleSelection(grid, selectionHandler, grid.datagrid('getSelections'), true);
			}
		});

		if (WickompDebugEnabled) {
			butts.push({
				width: 25,
				iconCls: "fa fa-bug",
				handler: function () {
					handleSelection(grid, handleDebug, grid.datagrid('getSelections'), false);
				}
			})
		}
	}

	grid.datagrid('getPager').pagination({
		buttons: butts
	});
}

// private
function handleSelection(grid, selectionHandler, selData, alertOnError) {
	var idField = grid.datagrid('options')["returnField"];
	if (!idField) {
		idField = grid.datagrid('options')["idField"];
	}
	if (idField) {
		var titleField = grid.datagrid('options')["titleField"];
		if (!titleField) {
			titleField = idField;
		}
		var kvList = [];
		try {
			for (var r = 0; r < selData.length; r++) {
				if (alertOnError && !selData[r][idField]) {
					throw "Null value for '" + idField + "' column as return key in your '" + (r + 1) + "' selected row!";
				}
				var obj = {};
				obj["key"] = selData[r][idField];
				obj["value"] = selData[r][titleField];
				obj["row"] = selData[r];
				kvList.push(obj);
			}
			selectionHandler(kvList);
		} catch (e) {
			$.messager.alert('Error', e);
		}
	} else {
		$.messager.alert('Error', 'No idField for grid!');
	}
}

// private
function handleDebug(kvList) {
	if (kvList) {
		var result = $("<ol>");
		for (var i = 0; i < kvList.length; i++) {
			result.append("<li>" + JSON.stringify(kvList[i]) + "</li>");
		}
		$.messager.show({
			title: 'Rows',
			msg: "<div style='direction: ltr;text-align: left;'>" + result.html() + "</div>",
			showType: 'show',
			style: {right: '', bottom: ''},
			width: 400,
			height: 300,
			timeout: 0,
			resizable: true
		});
	}
}
