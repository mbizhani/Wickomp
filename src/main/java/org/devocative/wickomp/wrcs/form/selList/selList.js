var lastOpenedSelList = null;

var closeHandler_SelList = function (event) {
	if (lastOpenedSelList != null) {
		lastOpenedSelList.css({
			"display": "none",
			"visibility": "hidden"
		});
		lastOpenedSelList = null;
	}
};

// keydown and click on other side of page results to hide popup
$(document).on("click keydown", closeHandler_SelList);

function handleAllSelList(selListPanelId, isEnabled, selectLabel, noOfSelectionLabel) {
	var selListPanel = $("#" + selListPanelId);
	var slOpener = selListPanel.find(".slOpener:first");
	var slTitle = selListPanel.find(".slTitle:first");
	var slDropDown = selListPanel.find(".slDropDown:first");
	var slChoices = selListPanel.find(".slChoices:first");
	var slFilter = selListPanel.find(".slFilter:first");
	var slClear = selListPanel.find(".slClear:first");
	var slSelectAll = selListPanel.find(".slSelectAll:first");
	var slShowFiltered = selListPanel.find(".slShowFiltered:first");
	var slClearFilter = selListPanel.find(".fa-times:first");

	// NOTE: the parent of popup-div must have relative position, so on parent scroll, popup-div also moves up/down!
	selListPanel.css("position", "relative");

	if (slChoices.find("input[type='checkbox']").size() == 0) {
		slSelectAll.css("display", "none");
	}

	slTitle.on("focus click", function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slOpener).position().top + $(slOpener).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;

		preventBubbling(event);
	});

	slDropDown.on("click keydown", function (event) {
		preventBubbling(event);
	});

	slOpener.click(function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slOpener).position().top + $(slOpener).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;

		preventBubbling(event);
	});

	slOpener.keydown(function (event) {
		preventBubbling(event);
	});

	slFilter.keydown(function (event) {
		if (event.keyCode == 13) {
			var filterTextInputValue = event.target.value;
			slChoices.find("label:not(:contains('" + filterTextInputValue + "'))").parentsUntil("tr").parent().css("display", "none");
			slChoices.find("label:contains('" + filterTextInputValue + "')").parentsUntil("tr").parent().css("display", "");
			event.preventDefault();
		}
	});

	slClearFilter.click(function () {
		slFilter.val('');
		slChoices.find("tr").css("display", "");
	});

	if (isEnabled) {
		slChoices
			.find("input[type='radio']")
			.on('change click', function (event) {
				slTitle.val(slChoices.find('label[for="' + event.target.id + '"]:first').html());
				closeHandler_SelList(event);
			});

		slChoices
			.find("input[type='checkbox']")
			.on('change', function (event) {
				var nos = slDropDown.find('input:checked').size();
				if (nos > 0) {
					slTitle.val(nos + " " + noOfSelectionLabel);
				} else {
					slTitle.val(selectLabel);
				}
			});

		slSelectAll.click(function (event) {
			var checkboxes = slDropDown.find("tr:visible").find("input[type='checkbox']").prop("checked", true);
			slTitle.val(checkboxes.size() + " " + noOfSelectionLabel);

			preventBubbling(event);
			event.preventDefault();
		});

		slClear.click(function (event) {
			slDropDown.find("input[type='checkbox']").prop("checked", false);
			slDropDown.find("input[type='radio']").prop("checked", false);
			slTitle.val(selectLabel);

			preventBubbling(event);
			event.preventDefault();
		});

		slShowFiltered.click(function (event) {
			if (!$(this).attr("clicked")) {
				slChoices.find("input:not(:checked)").parentsUntil("tr").parent().css("display", "none");
				$(this).attr("clicked", "t");
			} else {
				$(this).attr("clicked", null);
				slChoices.find("input:not(:checked)").parentsUntil("tr").parent().css("display", "");
			}

			preventBubbling(event);
			event.preventDefault();
		});
	}
}

function preventBubbling(event) {
	if (event.stopPropagation) {
		event.stopPropagation();
	} else {
		event.cancelBubble = true;
	}

	if (event.preventBubble) {
		event.preventBubble();
	}
}