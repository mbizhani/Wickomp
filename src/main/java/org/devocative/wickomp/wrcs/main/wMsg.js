var _wMsgBundle = {};

_wMsgBundle['fa'] = {
	info: 'اطلاع',
	warning: 'اخطار',
	error: 'خطا',

	wGrid: {
		selectionLimit:'تا 100 رکورد امکان ارسال است',
		pager: {
			showAllSelections:'انتخاب شده(ها)',
			deselectAll:'حذف تمامی انتخاب شده(ها)',
			selectAll:'انتخاب رکوردهای صفحه',
			sendSelections:'ارسال',
			debugSelections:'نمایش آنچه ارسال می شود'
		}
	},

	wTabbedPanel: {
		sureToClose: 'آیا شما مطمئنید به بستن\n'
	}
};

_wMsgBundle['en'] = {
	info: 'Info',
	warning: 'Warning',
	error: 'Error',

	wGrid: {
		selectionLimit:'Only maximum 100 records to send',
		pager: {
			showAllSelections:'Show selection(s)',
			deselectAll:'Deselect all',
			selectAll:'Select all records in this page',
			sendSelections:'Send',
			debugSelections:'Show sent selections (for debug)'
		}
	},

	wTabbedPanel: {
		sureToClose: 'Are you sure to close\n'
	}
};

function wSetLocale(locale) {
	if(_wMsgBundle[locale]) {
		return _wMsgBundle[locale];
	} else {
		wLog.error('Unavailable locale: ', locale);
		return _wMsgBundle['en'];
	}
}

var wMsg = wSetLocale('fa');