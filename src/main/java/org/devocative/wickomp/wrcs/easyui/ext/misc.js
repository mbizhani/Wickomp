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

function handleSelectionIndicator(gridId, selectionHandler) {
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
		butts.push({
			width: 25,
			iconCls: "fa fa-paper-plane-o",
			handler: function () {
				var selData = grid.datagrid('getSelections');
				selectionHandler(selData);
			}
		});
	}

	grid.datagrid('getPager').pagination({
		buttons: butts
	});
}