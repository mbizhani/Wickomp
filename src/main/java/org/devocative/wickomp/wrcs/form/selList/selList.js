var lastOpenedSelList = null;

var closeHandler_SelList = function(event) {
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
	var selListOpener = selListPanel.find(".selListOpener:first");
	var selListTitle = selListPanel.find(".selListTitle:first");
	var selListDropDown = selListPanel.find(".selListDropDown:first");
	var selListFilter = selListPanel.find(".selListFilter:first");
	var selListSNone = selListPanel.find(".selListSNone:first");
	var selListSAll = selListPanel.find(".selListSAll:first");

	var selectedRadio = selListDropDown.find("input[type='radio'][checked]");
	var selectedCheckboxSize = selListDropDown.find("input[type='checkbox'][checked]").size();
	selListDropDown.attr("nos", selectedCheckboxSize);
	if (selectedRadio.size() > 0) {
		selListTitle.val(selListDropDown.find('label[for="' + selectedRadio.attr("id") + '"]:first').html());
	} else if (selectedCheckboxSize > 0) {
		selListTitle.val(selectedCheckboxSize + " " + noOfSelectionLabel);
	} else {
		selListTitle.val(selectLabel);
	}

	if(selListDropDown.find("input[type='checkbox']").size()==0) {
		selListSAll.css("display", "none");
	}

	selListTitle.focusin(function(event) {
		closeHandler_SelList(event);
		selListDropDown.css("display", "inline");
		selListDropDown.css("visibility", "visible");
		lastOpenedSelList = selListDropDown;
		preventEvent(event)
	});

	selListDropDown.bind('focusin mouseup', function(event) {
		preventEvent(event);
	});

	selListOpener.click(function(event) {
		closeHandler_SelList(event);
		selListDropDown.css("display", "inline");
		selListDropDown.css("visibility", "visible");
		lastOpenedSelList = selListDropDown;
	});

	selListFilter.keydown(function(event) {
		if (event.keyCode == 13) {
			var filterTextInputValue = event.target.value;
			selListDropDown.find("label").each(function() {
				if (filterTextInputValue.length > 0 && $(this).html().indexOf(filterTextInputValue) < 0)
					$(this).parentsUntil("tr").parent().css("display", "none");
				else
					$(this).parentsUntil("tr").parent().css("display", "");
			});
			event.preventDefault();
		}
	});

	if (isEnabled) {
		selListDropDown.find("input[type='radio']").each(function() {
			$(this).change(function(event) {
				selListTitle.val(selListDropDown.find('label[for="' + event.target.id + '"]:first').html());
			});
		});

		selListDropDown.find("input[type='checkbox']").each(function() {
			$(this).change(function(event) {
				var nos = selListDropDown.attr("nos");
				if (event.target.checked)
					nos++;
				else
					nos--;
				selListDropDown.attr("nos", nos);
				if (nos > 0)
					selListTitle.val(nos + " " + noOfSelectionLabel);
				else
					selListTitle.val(selectLabel);
			});
		});

		selListSAll.click(function() {
			var checkboxes = selListDropDown.find("input[type='checkbox']");
			checkboxes.each(function() {
				$(this).prop("checked", true);
			});
			selListTitle.val(checkboxes.size() + " " + noOfSelectionLabel);
			selListDropDown.attr("nos", checkboxes.size());
		});

		selListSNone.click(function() {
			selListDropDown.find("input[type='checkbox']").each(function() {
				$(this).prop("checked", false);
			});
			selListDropDown.find("input[type='radio']").each(function() {
				$(this).prop("checked", false);
			});
			selListTitle.val(selectLabel);
			selListDropDown.attr("nos", 0);
		});
	}
}


function preventEvent(event) {
	if (event.stopPropagation)
		event.stopPropagation();
	else
		event.cancelBubble = true;
}