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