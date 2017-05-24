var wTools = {
	messagerShowDefaults: {
		draggable: true,
		height: '',
		modal: false,
		resizable: true,
		showType: 'show',
		style: {right: '', bottom: ''},
		timeout: 0,
		width: ''
	},

	show: function (options) {
		var ext = $.extend({}, wTools.messagerShowDefaults, options);
		$.messager.show(ext);
	},

	copyToClipboard: function (text) {
		$.messager.alert({
			title: wMsg.info,
			msg: wMsg.copy2clip,
			fn: function () {
				var textArea = document.createElement("textarea");
				textArea.style.position = 'fixed';
				textArea.style.top = 0;
				textArea.style.left = 0;
				textArea.style.width = '2em';
				textArea.style.height = '2em';
				textArea.style.padding = 0;
				textArea.style.border = 'none';
				textArea.style.outline = 'none';
				textArea.style.boxShadow = 'none';
				textArea.style.background = 'transparent';
				textArea.value = text;
				document.body.appendChild(textArea);
				textArea.select();
				try {
					var successful = document.execCommand('copy');
					var msg = successful ? 'successful' : 'unsuccessful';
					wLog.info('Copying text to clipboard: ' + msg);
				} catch (err) {
					wLog.error('copy2clip', err);
				}
				document.body.removeChild(textArea);
			}
		});
	}
};