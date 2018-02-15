$(window).keydown(function (e) {
	if (e.keyCode == 27) {
		$("div.messager-body")
			.parent()
			.find("a.panel-tool-close")
			.click();
	}
});

var wTools = {
	messagerShowDefaults: {
		draggable: true,
		height: '',
		modal: false,
		resizable: true,
		showType: 'show',
		style: {right: '', bottom: ''},
		timeout: 7000,
		width: ''
	},

	show: function (options) {
		try {
			var ext = $.extend({}, wTools.messagerShowDefaults, options);
			ext.msg = ext.msg
				.replace(/\t/g, '&nbsp;&nbsp;')
				.replace(/\r/g, '')
				.replace(/\n/g, '<br/>');
			$.messager.show(ext);
		} catch (e) {
			$.messager.alert("Error: show()", e);
		}
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
	},

	dispatch: function (eventType, attachment) {
		var ev = new CustomEvent(eventType);
		$.each(attachment, function (k, v) {
			ev[k] = v;
		});
		window.dispatchEvent(ev);
	}
};
