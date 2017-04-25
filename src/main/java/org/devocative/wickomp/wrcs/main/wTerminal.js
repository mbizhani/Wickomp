(function ($) {
	$.fn.wTerminal = function (cmdOrOpts, options) {
		var defaults = {
			cursorBlink: true,
			//screenKeys: false,
			//useStyle: true,
			convertEol: true,
			rows: 30,
			cols: 100
		};

		var ctx = {
			term: null,
			target: $(this),

			init: function (options) {
				wLog.info('wTerminal Init: ', options);

				if (!Wicket && !Wicket.Event && !Wicket.Event.subscribe) {
					wLog.error('No Wicket.Event.subscribe!');
					alert('No Wicket.Event.subscribe!');
					return;
				}

				ctx.term = new Terminal(options);
				ctx.term.open(ctx.target[0]);
				ctx.term.write("Connecting ...\n");

				try {
					ctx.term.textarea.onpaste = function (e) {
						wLog.debug('wTerminal: term.textarea.onpaste');

						if (e.clipboardData && e.clipboardData.getData) {
							var clipData = e.clipboardData.getData('text/plain');
							if (clipData) {
								wLog.debug('wTerminal: term.textarea.onpaste -> event.clipboardData', clipData);
								Wicket.WebSocket.send("key:" + clipData);
							}
						}
					};
				} catch (err) {
					wLog.error("wTerminal: can't assign onpaste event", err);
				}

				Wicket.Event.subscribe("/websocket/open", function (jqEvent, message) {
					wLog.info('wTerminal: websocket/open!, ' + message);
					ctx.term.write("WebSocket connected!\n");
				});

				Wicket.Event.subscribe("/websocket/message", function (jqEvent, message) {
					ctx.term.write(message);
				});

				Wicket.Event.subscribe("/websocket/closed", function (jqEvent, message) {
					wLog.warn('wTerminal: websocket/closed!', message);
					ctx.term.write("\nWebSocket to server is disconnected!\n");
				});

				Wicket.Event.subscribe("/websocket/error", function (jqEvent, message) {
					wLog.error('wTerminal: websocket/error!', message);
				});

				var isSpecialKey = false;
				ctx.term.on('keydown', function (ev) {
					isSpecialKey = true;
					//wLog.debug('keydown', ev);
				});

				ctx.term.on('key', function (key, ev) {
					//wLog.debug('key', ev);

					var msg;
					if (isSpecialKey) {
						msg = "specialKey:" + ev.keyCode;
					} else {
						msg = "key:" + key;
					}
					isSpecialKey = false;

					if (msg) {
						Wicket.WebSocket.send(msg);
					}
				});
			},

			resize: function () {
				var width = Math.floor(ctx.target.innerWidth() - 8);
				var height = Math.floor(ctx.target.innerHeight());
				wLog.debug("wTerminal.resize: ", width, height);
				ctx.term.resize(Math.floor(width / 8), Math.floor(height / 18));
			}
		};

		if (!cmdOrOpts && !options) {
			ctx.init(defaults);
		} else if (typeof(cmdOrOpts) === 'object') {
			var opt = $.extend({}, defaults, cmdOrOpts);
			ctx.init(opt);
		} else if (typeof(cmdOrOpts) === 'string') {
			switch (cmdOrOpts) {
				default:
					throw "wTerminal: Invalid Command [" + cmdOrOpts + "]";
			}
		}

		return $(this);
	}
})(jQuery);
