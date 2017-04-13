(function ($) {
	$.fn.wTabbedPanel = function (cmdOrOpts, options) {
		var ctx = {
			opt: null,
			target: $(this),

			init: function (opts) {
				ctx.opt = opts ? opts : {};
				wLog.debug('wTabbedPanel: init', ctx.opt);

				ctx.opt['onBeforeClose'] = function (title, index) {
					wLog.debug('wTabbedPanel: onBeforeClose: t=' + title + ', i=' + index);

					//this section is copied from its doc on the site
					$.messager.confirm(wMsg.warning, wMsg.wTabbedPanel.sureToClose + '"' + title + '"?', function (r) {
						if (r) {
							var opts = ctx.target.tabs('options');
							var bc = opts.onBeforeClose;
							opts.onBeforeClose = function () {
							};
							ctx.target.tabs('close', index);
							opts.onBeforeClose = bc;
						}
					});
					return false;
				};

				ctx.opt['onClose'] = function (title, index) {
					wLog.info('wTabbedPanel: onClose: t=' + title + ', i=' + index);

					if (ctx.opt['url']) {
						wLog.debug('wTabbedPanel: onClose.callServer');
						Wicket.Ajax.get({u: ctx.opt['url'] + '&index=' + index});
					} else {
						wLog.error('wTabbedPanel: no url for calling server on tab close');
					}
				};

				ctx.target.tabs(ctx.opt);
			},

			add: function (opts) {
				wLog.debug('wTabbedPanel: add');

				var item = $('<div>');
				item.attr('id', opts['htmlId']);

				ctx.target.tabs('add', {
					title: opts['title'] ? opts['title'] : '[?]',
					content: item,
					closable: opts['closable'] ? opts['closable'] : false
				});
			}
		};

		if (!cmdOrOpts && !options) {
			ctx.init();
		} else if (typeof(cmdOrOpts) === 'object') {
			ctx.init(cmdOrOpts);
		} else if (typeof(cmdOrOpts) === 'string') {
			switch (cmdOrOpts) {
				case "add":
					ctx.add(options);
					break;
				default:
					throw "wTabbedPanel: Invalid Command [" + cmdOrOpts + "]";
			}
		}
	}
})(jQuery);