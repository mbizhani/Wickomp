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
	slTitle.css("width", "100px");

	selListPanel.closest("form").bind("reset", function () {
		slDropDown.find("table:first").html(slTitle.data("oldContent"));
	});
}

function handleClientSearchableList(modalWindowId, inputName, holderTableId, titleId, rows) {
	var holder = $('#' + holderTableId);
	var title = $('#' + titleId);

	if (rows) {
		holder.empty();
		for (var r = 0; r < rows.length; r++) {
			var input = $('<input id="' + inputName + r + '" type="checkbox" name="' + inputName + '" value="' + rows[r]["key"] + '" checked/>');
			var span = $('<label for="' + inputName + r + '">' + rows[r]["value"] + '</label>');

			var td1 = $('<td></td>');
			td1.append(input);
			var td2 = $('<td></td>');
			td2.append(span);

			var tr = $('<tr></tr>');
			tr.append(td1);
			tr.append(td2);

			holder.append(tr);
		}
		title.val(rows.length);
	}

	if (title.data("oldContent") == undefined) {
		title.data("oldContent", holder.html());
	}
	if (modalWindowId) {
		$('#' + modalWindowId).window('close');
	}
}