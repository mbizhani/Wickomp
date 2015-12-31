var gridDefaultView;

function changeGridGroupField(select, gridId) {
    var opt = $('#' + gridId).datagrid('options');

    if (!gridDefaultView) {
        gridDefaultView = opt.view;
    }

    if (select.value != "") {
        opt.view = groupview;
        opt.groupField = select.value;
        opt.groupFormatter = function (value, rows) {
            return value;
        };
        $('#' + gridId).datagrid('sort', {sortName: select.value, sortOrder: 'desc'});
    } else {
        opt.view = gridDefaultView;
        opt.groupField = '';
        $('#' + gridId).datagrid('reload');
    }
}