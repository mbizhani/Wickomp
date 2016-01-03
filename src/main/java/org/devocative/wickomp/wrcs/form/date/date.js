function handleDateEvents(divId, nowYear, nowMonth, nowDay) {
    var twoDigit = "20px";
    $('#' + divId + ' .yr').css("width", "30px").autoNumeric('init', {aSep: '', mDec: 0, vMax: 9999});
    $('#' + divId + ' .mo').css("width", twoDigit).autoNumeric('init', {aSep: '', mDec: '0', vMin: 0, vMax: 12});
    $('#' + divId + ' .dy').css("width", twoDigit).autoNumeric('init', {aSep: '', mDec: '0', vMin: 0, vMax: 31});
    $('#' + divId + ' .hr').css("width", twoDigit).autoNumeric('init', {aSep: '', mDec: '0', vMin: 0, vMax: 23});
    $('#' + divId + ' .mi').css("width", twoDigit).autoNumeric('init', {aSep: '', mDec: '0', vMin: 0, vMax: 59});
    $('#' + divId + ' .sc').css("width", twoDigit).autoNumeric('init', {aSep: '', mDec: '0', vMin: 0, vMax: 59});

    var div = $('#' + divId);
    var year = div.find(".yr");
    var month = div.find(".mo");
    var day = div.find(".dy");

    $('#' + divId + " input[type='text']")
        .css('text-align', 'center')
        .bind('keydown', function (event) {
            if (event.which == 32) {
                if ($(event.target).val() == '') {
                    year.val(nowYear);
                    month.val(nowMonth);
                    day.val(nowDay);
                } else {
                    year.val('');
                    month.val('');
                    day.val('');
                }
            }
        });
}