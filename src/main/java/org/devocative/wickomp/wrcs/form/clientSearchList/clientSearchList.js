function initClientSearchableList(selListPanelId) {
	var selListPanel = $("#" + selListPanelId);
	var slOpener = selListPanel.find(".slOpener:first");
	var slTitle = selListPanel.find(".slTitle:first");
	var slDropDown = selListPanel.find(".slDropDown:first");

	slTitle.focusin(function (event) {
		closeHandler_SelList(event);
		slDropDown.css("display", "inline");
		slDropDown.css("visibility", "visible");
		lastOpenedSelList = slDropDown;
		preventEvent(event)
	});

	slDropDown.bind('focusin mouseup', function (event) {
		preventEvent(event);
	});

	slOpener.click(function (event) {
		closeHandler_SelList(event);
		slDropDown.css("display", "inline");
		slDropDown.css("visibility", "visible");
		lastOpenedSelList = slDropDown;
	});

	//overwrite the CSS 120px value
	slTitle.css("width", "85px");
}

function handleClientSearchableList(modalWindowId, inputName, holderTableId, titleId, rows) {
	if (rows) {
		var holder = $('#' + holderTableId);
		var title = $('#' + titleId);
		holder.empty();
		for (var r = 0; r < rows.length; r++) {
			var input = $('<input type="hidden" name="' + inputName + '" value="' + rows[r]["key"] + '"/>');
			var span = $('<span>' + rows[r]["value"] + '</span>');

			var td1 = $('<td style="border-bottom: 2px solid #cccccc"></td>');
			td1.append(input);
			td1.append(span);

			var delAct = $('<i class="fa fa-times" style="color:red;cursor:pointer;"></i>');
			delAct.bind("click", function () {
				$(this).parentsUntil("tr").parent().remove();
				var count = title.val();
				title.val(count - 1);
			});
			var td2 = $('<td style="border-bottom: 2px solid #cccccc"></td>');
			td2.append(delAct);

			var tr = $('<tr></tr>');
			tr.append(td1);
			tr.append(td2);

			holder.append(tr);
		}
		title.val(rows.length);
	}
	$('#' + modalWindowId).window('close');
}