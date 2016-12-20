function initClientSearchableList(selListPanelId) {
	var selListPanel = $("#" + selListPanelId);
	var slTable = selListPanel.find(".w-input:first");
	var slOpener = selListPanel.find(".slOpener:first");
	var slTitle = selListPanel.find(".slTitle:first");
	var slDropDown = selListPanel.find(".slDropDown:first");

	// NOTE: the parent of popup-div must have relative position, so on parent scroll, popup-div also moves up/down!
	selListPanel.css("position", "relative");

	slTitle.on("focus click", function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slTable).position().top + $(slTable).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;

		preventEvent(event)
	});

	slDropDown.on("click keydown", function (event) {
		preventEvent(event);
	});

	slOpener.click(function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slTable).position().top + $(slTable).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;

		preventEvent(event);
	});

	slOpener.keydown(function (event) {
		preventEvent(event);
	});

	//overwrite the CSS 120px value
	slTitle.css("width", "100px");

	selListPanel.closest("form").on("reset", function () {
		//console.log("ClientSearch: Reset", slTitle.data("oldContent"));
		slDropDown.html(slTitle.data("oldContent"));

		slDropDown.find("input").on('change', function (event) {
			var nos = slDropDown.find('input:checked').size();
			slTitle.val(nos);
		});
	});
}

function handleClientSearchableList(modalWindowId, inputName, holderTableId, titleId, rows, multipleSelection) {
	var holder = $('#' + holderTableId);
	var title = $('#' + titleId);

	if (rows) {
		holder.empty();
		for (var r = 0; r < rows.length; r++) {
			var input;
			if (multipleSelection) {
				input = $('<input id="' + inputName + r + '" type="checkbox" name="' + inputName + '" value="' + rows[r]["key"] + '" checked/>');
			} else {
				if (r == 0) {
					input = $('<input id="' + inputName + r + '" type="radio" name="' + inputName + '" value="' + rows[r]["key"] + '" checked/>');
				} else {
					input = $('<input id="' + inputName + r + '" type="radio" name="' + inputName + '" value="' + rows[r]["key"] + '"/>');
				}
			}
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
		holder.find("input").on('change', function (event) {
			var nos = holder.find('input:checked').size();
			title.val(nos);
		});

		title.val(multipleSelection ? rows.length : 1);
	}

	if (title.data("oldContent") == undefined) {
		//console.log("ClientSearch: First!");
		title.data("oldContent", holder[0].outerHTML);
	}
	if (modalWindowId) {
		$('#' + modalWindowId).window('close');
	}
}