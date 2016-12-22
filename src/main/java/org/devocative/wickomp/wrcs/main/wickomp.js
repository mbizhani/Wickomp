var DEVELOPMENT_MODE = true;

function log() {
	if (DEVELOPMENT_MODE) {
		var args = Array.prototype.slice.call(arguments);
		console.log.apply(console, args);
	}
}

(function ($) {
	$.fn.wTabbedPanel = function (cmd, options) {
		var ctx = {
			opt: null,
			target: $(this),

			init: function (opts) {
				ctx.opt = opts;

				log('wTabbedPanel.init', ctx.opt);

				ctx.target.tabs({
					onBeforeClose: function (title, index) {
						log('onBeforeClose: t=' + title + ', i=' + index);

						//this section is copied from its doc on the site
						//TODO using I18N
						$.messager.confirm('?', 'Are you sure you want to close ' + title, function (r) {
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
					},
					onClose: function (title, index) {
						log('onClose: t=' + title + ', i=' + index);

						if (ctx.opt['url']) {
							log('\tonClose.callServer');
							Wicket.Ajax.get({u: ctx.opt['url'] + '&index=' + index});
						}
					}
				});
			},

			add: function (opts) {
				log('wTabbedPanel.add');

				var item = $('<div>');
				item.attr('id', opts['htmlId']);

				ctx.target.tabs('add', {
					title: opts['title'],
					content: item,
					closable: opts['closable'] ? opts['closable'] : false
				});
			}
		};

		if (!cmd && !options) {
			ctx.init(options);
		} else if (typeof(cmd) === 'object') {
			ctx.init(cmd);
		} else if (typeof(cmd) === 'string') {
			switch (cmd) {
				case "add":
					ctx.add(options);
					break;
				default:
					throw "Invalid Command: " + cmd;
			}
		}
	}
})(jQuery);