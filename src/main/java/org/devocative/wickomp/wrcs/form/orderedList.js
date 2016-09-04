function handleOrderedList(hiddenId, idDelimiter, srcSelectId, destSelectId, upImgId, downImgId) {
	var hid = $('#' + hiddenId);
	var src = $('#' + srcSelectId);
	var dest = $('#' + destSelectId);
	var up = $('#' + upImgId);
	var down = $('#' + downImgId);

	src.dblclick(function () {
		src.find('option:selected').remove().appendTo(dest);
		updateHiddenIds(hid, dest, idDelimiter);
	});

	dest.dblclick(function () {
		dest.find('option:selected').remove().appendTo(src);
		updateHiddenIds(hid, dest, idDelimiter);
	});

	up.click(function () {
		var opt = dest.find('option:selected');
		opt.insertBefore(opt.prev());
		updateHiddenIds(hid, dest, idDelimiter);
	});

	down.click(function () {
		var opt = dest.find('option:selected');
		opt.insertAfter(opt.next());
		updateHiddenIds(hid, dest, idDelimiter);
	});
}

function updateHiddenIds(hid, dest, idDelimiter) {
	var val = "";
	dest.find('option').each(function () {
		val += $(this).attr('value') + idDelimiter;
	});
	hid.attr('value', val);
}