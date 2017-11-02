(function ($) {
	var wBaseGridDefaults = {
		autoRowHeight: false,
		loadMsg: '...',
		multiSort: false,
		pagination: true,
		rownumbers: true,
		singleSelect: true,
		scrollOnSelect: false, // since 1.5.2
		striped: true,

		// --------------- custom fields

		asyncLoadingEnabled: true,
		columnReorder: true,
		callbackOnColumnReorder: false,
		selectionIndicator: false,
		selectionDblClick: true,

		// ------------------------------

		loadFilter: function (data) {
			wLog.debug('wBaseGridDefaults.loadFilter', data);

			if (data.error) {
				wTools.show({
					title: '<i class="fa fa-exclamation-triangle" style="color:#aa1111"></i>',
					msg: data.error
				});
			} else if (data.rows && data.rows.length == 0) {
				var noResultMessage = $(this).datagrid('options')['noResultMessage'];
				if (noResultMessage) {
					wTools.show({
						title: '<i class="fa fa-exclamation-triangle" style="color:#aa1111"></i>',
						msg: noResultMessage,
						timeout: 1500,
						showType: 'slide'
					});
				}
			}

			$(this).datagrid('loaded');
			$('#' + $(this).attr('id') + '-tb').css('visibility', 'visible');

			return data;
		},

		onLoadSuccess: function (data) {
			wLog.debug('wBaseGridDefaults.onLoadSuccess');

			wBaseGridDefaults.initSelection($(this));

			if ($(this).data('action') == 'reset') {
				$(this).datagrid('gotoPage', $(this).data('pageNum'));
				$(this).datagrid('options')['url'] = $(this).data('url');
				$(this).data('action', 'none');
			}

			wBaseGridDefaults.updateColumnsTooltip($(this), $(this).datagrid("getColumnFields"));
			wBaseGridDefaults.updateColumnsTooltip($(this), $(this).datagrid("getColumnFields", true));
		},

		onLoadError: function () {
			wLog.debug('wBaseGridDefaults.onLoadError');
			if ($(this).datagrid('options')['asyncLoadingEnabled']) {
				$(this).datagrid('loading');
			}
		},

		onSelect: function (data) {
			wBaseGridDefaults.selectionChanged($(this));
		},

		onUnselect: function (data) {
			wBaseGridDefaults.selectionChanged($(this));
		},

		onSelectAll: function (data) {
			wBaseGridDefaults.selectionChanged($(this));
		},

		onUnselectAll: function (data) {
			wBaseGridDefaults.selectionChanged($(this));
		},

		// --------------- columns-ext

		onDropColumn: function (toField, fromField, point) {
			wLog.debug('wBaseGridDefaults.onDropColumn', toField, fromField, point, $(this).datagrid('getColumnFields'));
			var options = $(this).datagrid('options');
			if (options['url'] && options['callbackOnColumnReorder']) {
				var cols = $(this).datagrid('getColumnFields');
				var colName = cols[0];
				for (var i = 1; i < cols.length; i++) {
					colName += "," + cols[i];
				}
				Wicket.Ajax.get({u: options['url'] + '&$cr=' + colName});
			}
		},

		// --------------- custom methods

		onInit: function (options) {
			wLog.debug('wBaseGridDefaults.onInit');

			var grid = $(this);
			var gridId = options["gridId"];
			if (gridId) {
				window.addEventListener("GridLoading", function (e) {
					if (e["targetGrid"] && e["targetGrid"] == gridId) {
						grid.datagrid("loading");
					}
				});
				window.addEventListener("GridLoaded", function (e) {
					if (e["targetGrid"] && e["targetGrid"] == gridId) {
						grid.datagrid("loaded");
					}
				});
			}

			wBaseGridDefaults.initColumns($(this), $(this).datagrid("getColumnFields"));
			wBaseGridDefaults.initColumns($(this), $(this).datagrid("getColumnFields", true));

			wBaseGridDefaults.initSelection($(this));

			if (options['reorderColumns']) {
				$(this).datagrid('reorderColumns', options['reorderColumns']);
			}

			if (options['columnReorder']) {
				$(this).datagrid('columnMoving');
			}

			if (options['pagingBarLayout']) {
				var pageOpt = {layout: options['pagingBarLayout'].slice(0)}; //clone array
				$(this).datagrid('getPager').pagination(pageOpt);
			}

			var toolbarId = $(this).attr("id") + "-tb";
			$('#' + toolbarId).find('.w-grid-tbar-but').linkbutton({plain: true});
		},

		initColumns: function (grid, columns) {
			for (var i = 0; i < columns.length; i++) {
				wBaseGridDefaults.initColumn(grid, columns[i], grid.datagrid("getColumnOption", columns[i]));
			}
		},

		initColumn: function (grid, colName, columnOpt) {
			if (columnOpt["style"] || columnOpt["styleClass"]) {
				columnOpt["styler"] = function (value, row, index) {
					var r = {};
					if (columnOpt["style"]) {
						r["style"] = columnOpt["style"];
					}

					if (columnOpt["styleClass"]) {
						r["class"] = columnOpt["styleClass"];
					}

					return r;
				}
			}

			/*if (columnOpt["format"]) {
			 var gridOpt = grid.datagrid("options");
			 if (gridOpt["formatters"] && gridOpt["formatters"][columnOpt["format"]]) {
			 columnOpt["formatter"] = function (value, row, index) {
			 return gridOpt["formatters"][columnOpt["format"]](value, row, index);
			 }
			 }
			 }*/
		},

		initSelection: function (grid) {
			//NOTE: a bug in onLoadSuccess, pager is reset to default!
			var pagingBarLayout = grid.datagrid('options')['pagingBarLayout'];
			if (pagingBarLayout) {
				var pageOpt = {layout: pagingBarLayout.slice(0)}; //clone array
				grid.datagrid('getPager').pagination(pageOpt);
			}

			var selectionIndicator = grid.datagrid('options')['selectionIndicator'];
			var selectionHandler = grid.datagrid('options')['selectionJSHandler'];

			if (!selectionIndicator && !selectionHandler) {
				wLog.debug('wGrid: no selection indicator & handler');
				return;
			}

			var enableDblClickSelection = grid.datagrid('options')['selectionDblClick'];

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
				},
				{
					width: 25,
					iconCls: 'fa fa-eraser',
					handler: function () {
						grid.datagrid('clearSelections');
					}
				}
			];

			if (selectionHandler) {
				if (!grid.datagrid('options').singleSelect) {
					butts.push({
						width: 25,
						iconCls: 'fa fa-bars',
						handler: function () {
							grid.datagrid('selectAll');
						}
					});
				}

				if (enableDblClickSelection) {
					if (grid.datagrid('options')['treeField']) {
						grid.datagrid('options').onDblClickRow = function (row) {
							var arr = [];
							arr.push(row);
							wBaseGridDefaults.handleSelection(grid, selectionHandler, arr, true);
						};
					} else {
						grid.datagrid('options').onDblClickRow = function (index, row) {
							var arr = [];
							arr.push(row);
							wBaseGridDefaults.handleSelection(grid, selectionHandler, arr, true);
						};
					}
				}

				butts.push({
					width: 25,
					iconCls: 'fa fa-paper-plane-o',
					handler: function () {
						wBaseGridDefaults.handleSelection(grid, selectionHandler, grid.datagrid('getSelections'), true);
					}
				});

				if (WickompDebugEnabled) {
					butts.push({
						width: 25,
						iconCls: 'fa fa-bug',
						handler: function () {
							wBaseGridDefaults.handleSelection(grid, wBaseGridDefaults.handleDebug, grid.datagrid('getSelections'), false);
						}
					})
				}
			}

			wBaseGridDefaults.updateButtonsOfPager(grid, butts);
			wLog.debug('wBaseGridDefaults.initSelection: butts', butts);
		},

		selectionChanged: function (grid) {
			var butts = grid.datagrid('getPager').pagination('options')['buttons'];
			if (butts) {
				butts[0].text = grid.datagrid('getSelections').length.toString();

				wBaseGridDefaults.updateButtonsOfPager(grid, butts);
			}
		},

		updateButtonsOfPager: function (grid, butts) {
			wLog.debug('wBaseGridDefaults.updateButtonsOfPager: no of selection but text=', butts[0].text);

			grid.datagrid('getPager').pagination({
				buttons: butts
			});

			grid.datagrid('getPager').pagination().find('span.fa').closest('a.l-btn')
				.each(function () {
					var cls = $(this).find('span.fa').attr('class');
					var title = "";
					if (cls.indexOf('fa-check-square-o') > -1) {
						title = wMsg.wGrid.pager.showAllSelections;
					} else if (cls.indexOf('fa-eraser') > -1) {
						title = wMsg.wGrid.pager.deselectAll;
					} else if (cls.indexOf('fa-bars') > -1) {
						title = wMsg.wGrid.pager.selectAll;
					} else if (cls.indexOf('fa-paper-plane-o') > -1) {
						title = wMsg.wGrid.pager.sendSelections;
					} else if (cls.indexOf('fa-bug') > -1) {
						title = wMsg.wGrid.pager.debugSelections;
					}
					$(this).attr('title', title);
				});
		},

		handleSelection: function (grid, selectionHandler, selData, alertOnError) {
			if (selData.length > 100) {
				$.messager.alert(wMsg.error, wMsg.wGrid.selectionLimit);
				return;
			}

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
		},

		handleDebug: function (kvList) {
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
		},

		updateColumnsTooltip: function (grid, columns) {
			for (var i = 0; i < columns.length; i++) {
				wBaseGridDefaults.updateColumnTooltip(grid, columns[i]);
			}
		},

		updateColumnTooltip: function (grid, columnName) {
			if (grid.datagrid("getColumnOption", columnName)["showAsTooltip"]) {
				grid.datagrid('getPanel').find("td[field=" + columnName + "] > div.datagrid-cell").tooltip({
					content: function () {
						return $(this).html();
					},
					onShow: function () {
						$(this).tooltip('tip').css({
							backgroundColor: '#FFFFE0',
							borderColor: '#555555',
							boxShadow: '1px 1px 3px #292929',
							padding: '5px'
						});
					}
				});
			}
		}
	};

	$.fn.wDataGrid = function (cmdOrOpts, options) {
		var gridSpecific = {};

		if (!$(this).data("inited")) {
			var extOpt = $.extend({}, gridSpecific, wBaseGridDefaults, cmdOrOpts);
			wLog.debug('wDataGrid init', extOpt);
			var datagrid = $(this).datagrid(extOpt);
			extOpt.onInit.call(this, extOpt);
			$(this).data("inited", true);
			return datagrid
		} else {
			if (cmdOrOpts == 'updateColumns') {
				$(this).data('url', $(this).datagrid('options')['url']);
				$(this).data('pageNum', $(this).datagrid('getPager').pagination('options').pageNumber);
				$(this).data('action', 'reset');
				$(this).datagrid('options')['url'] = '';
				return $(this).datagrid(options);
			} else if (cmdOrOpts == 'resetData') {
				return $(this).datagrid('load');
			} else if (cmdOrOpts == 'updateUrl') {
				$(this).datagrid('options')['url'] = options;
				return $(this);
			} else {
				return $(this).datagrid(cmdOrOpts, options);
			}
		}
	};

	$.fn.wTreeGrid = function (cmdOrOpts, options) {
		var treeGridSpecific = {
			animate: false,

			onBeforeLoad: function (row, param) {
				if (!row) {
					param.id = '';
				}
			},

			onBeforeExpand: function (row) {
				wLog.debug('treeGridSpecific.onBeforeExpand', row);
			},

			onExpand: function (row) {
				wLog.debug('treeGridSpecific.onExpand', row);
			}

		};

		if (!$(this).data("inited")) {
			var extOpt = $.extend({}, treeGridSpecific, wBaseGridDefaults, cmdOrOpts);
			wLog.debug('wTreeGrid init', extOpt);
			var treegrid = $(this).treegrid(extOpt);
			extOpt.onInit.call(this, extOpt);
			$(this).data("inited", true);
			return treegrid;
		} else {
			if (cmdOrOpts == 'resetData') {
				return $(this).treegrid('load');
			} else if (cmdOrOpts == 'updateUrl') {
				$(this).treegrid('options')['url'] = options;
				return $(this);
			} else {
				return $(this).treegrid(cmdOrOpts, options);
			}
		}
	}
})(jQuery);

// TODO: the following must be merged to the upper part!

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
