var lastOpenedSelList = null;

var closeHandler_SelList = function (event) {
	if (lastOpenedSelList != null) {
		lastOpenedSelList.css("display", "none");
		lastOpenedSelList.css("visibility", "hidden");
		lastOpenedSelList = null;
	}
};

$(document).mouseup(closeHandler_SelList);
$(document).focusin(closeHandler_SelList);

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

	if (slChoices.find("input[type='checkbox']").size() == 0) {
		slSelectAll.css("display", "none");
	}

	slTitle.focusin(function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slOpener).position().top + $(slOpener).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;
		preventEvent(event)
	});

	slDropDown.bind('focusin mouseup', function (event) {
		preventEvent(event);
	});

	slOpener.click(function (event) {
		closeHandler_SelList(event);
		slDropDown.css({
			"display": "inline",
			"visibility": "visible",
			"top": $(slOpener).position().top + $(slOpener).outerHeight(true)
		});
		lastOpenedSelList = slDropDown;
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
		slChoices.find("input[type='radio']").bind('change', function (event) {
			slTitle.val(slChoices.find('label[for="' + event.target.id + '"]:first').html());
		});

		slChoices.find("input[type='checkbox']").bind('change', function (event) {
			var nos = slDropDown.find('input:checked').size();
			if (nos > 0)
				slTitle.val(nos + " " + noOfSelectionLabel);
			else
				slTitle.val(selectLabel);
		});

		slSelectAll.click(function () {
			var checkboxes = slDropDown.find("tr:visible").find("input[type='checkbox']").prop("checked", true);
			slTitle.val(checkboxes.size() + " " + noOfSelectionLabel);
		});

		slClear.click(function () {
			slDropDown.find("input[type='checkbox']").prop("checked", false);
			slDropDown.find("input[type='radio']").prop("checked", false);
			slTitle.val(selectLabel);
		});

		slShowFiltered.click(function () {
			if (!$(this).attr("clicked")) {
				slChoices.find("input:not(:checked)").parentsUntil("tr").parent().css("display", "none");
				$(this).attr("clicked", "t");
			} else {
				$(this).attr("clicked", null);
				slChoices.find("input:not(:checked)").parentsUntil("tr").parent().css("display", "");
			}
		});
	}
}

function preventEvent(event) {
	if (event.stopPropagation)
		event.stopPropagation();
	else
		event.cancelBubble = true;
}