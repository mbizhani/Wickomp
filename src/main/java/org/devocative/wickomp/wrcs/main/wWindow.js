(function ($) {
	var wWindowDefaults = {
		closable: true,
		collapsible: false,
		inline: false,
		maximizable: false,
		minimizable: false,
		modal: false,

		openAnimation: 'fade',
		openDuration: 800,
		closeAnimation: 'fade',
		closeDuration: 800,

		onClose: function () {
			var callbackOnClose = $(this).window('options')['callbackOnClose'];
			var closeCallbackUrl = $(this).window('options')['closeCallbackUrl'];
			if (callbackOnClose && closeCallbackUrl) {
				wLog.debug('CloseCallbackUrl: ', closeCallbackUrl);
				Wicket.Ajax.get({u: closeCallbackUrl});
			}
		},

		// extra field
		callbackOnClose: false,
		closeOnEscape: true
	};

	$.fn.wWindow = function (cmdOrOpts, options) {
		if (typeof(cmdOrOpts) === 'object') {
			var extOpt = $.extend({}, wWindowDefaults, cmdOrOpts);
			wLog.debug('wWindow: init', extOpt);
			var window = $(this).window(extOpt);
			if (extOpt.closeOnEscape) {
				$(window)
					.attr('tabindex', '-1')
					.focus()
					.keydown(function (e) {
						if (e.which == 27)
							window.window('close');
					});
			} else {
				wLog.debug('wWindow: closeOnEscape = false');
			}
			return window;
		} else {
			return $(this).window(cmdOrOpts, options);
		}
	}

})(jQuery);