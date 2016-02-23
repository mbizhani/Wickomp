function handleClientSearchableList(modalWindowId, inputName, holderTableId, rows) {
	if (rows) {
		var holder = $('#' + holderTableId);
		holder.empty();
		for (var r = 0; r < rows.length; r++) {
			var input = $('<input type="hidden" name="' + inputName + '" value="' + rows[r]["key"] + '"/>');
			var span = $('<span>' + rows[r]["value"] + '</span>');

			var td1 = $('<td></td>');
			td1.append(input);
			td1.append(span);

			var delAct = $('<i class="fa fa-times" style="color:red;cursor:pointer;"></i>');
			delAct.bind("click", function () {
				$(this).parentsUntil("tr").parent().remove();
			});
			var td2 = $('<td></td>');
			td2.append(delAct);

			var tr = $('<tr></tr>');
			tr.append(td1);
			tr.append(td2);

			holder.append(tr);
		}
	}
	$('#' + modalWindowId).window('close');
}