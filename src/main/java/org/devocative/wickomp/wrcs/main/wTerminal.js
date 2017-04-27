(function ($) {
	$.fn.wTerminal = function (ctid) {
		var defaults = {
			cursorBlink: true,
			//screenKeys: false,
			//useStyle: true,
			convertEol: true,
			rows: 40,
			cols: 100
		};

		var ctx = {
			term: null,
			target: $(this),
			ctid: '',

			init: function (ctid) {
				wLog.info('wTerminal Init: ', ctid);
				ctx.ctid = ctid;

				if (!Wicket && !Wicket.Event && !Wicket.Event.subscribe) {
					wLog.error('No Wicket.Event.subscribe!');
					alert('No Wicket.Event.subscribe!');
					return;
				}

				ctx.term = new Terminal(defaults);
				ctx.term.open(ctx.target[0]);
				ctx.term.write("Connecting ...\n");

				try {
					ctx.term.textarea.onpaste = function (e) {
						wLog.debug('wTerminal: term.textarea.onpaste');

						if (e.clipboardData && e.clipboardData.getData) {
							var clipData = e.clipboardData.getData('text/plain');
							if (clipData) {
								wLog.debug('wTerminal: term.textarea.onpaste -> event.clipboardData', clipData);
								ctx.send('key', clipData);
							}
						}
					};
				} catch (err) {
					wLog.error("wTerminal: can't assign onpaste event", err);
				}

				Wicket.Event.subscribe("/websocket/message", function (jqEvent, message) {
					var parse = JSON.parse(message);
					if (parse && parse['ctid'] && parse.ctid == ctx.ctid) {
						ctx.term.write(parse.text);
					}
				});

				Wicket.Event.subscribe("/websocket/closed", function (jqEvent, message) {
					wLog.warn('wTerminal: websocket/closed!', message);
					ctx.term.write("\nERR: WebSocket is Disconnected!\n");
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

					if (isSpecialKey) {
						ctx.send('specialKey', '' + ev.keyCode);
					} else {
						ctx.send('key', key);
					}
					isSpecialKey = false;
				});

				ctx.send('init', null);
			},

			resize: function () {
				var width = Math.floor(ctx.target.innerWidth() - 8);
				var height = Math.floor(ctx.target.innerHeight());
				wLog.debug("wTerminal.resize: ", width, height);
				ctx.term.resize(Math.floor(width / 8), Math.floor(height / 18));
			},

			send: function (cmd, value) {
				var msg = {
					ctid: ctx.ctid,
					cmd: cmd
				};
				if (value) {
					msg['value'] = value;
				}
				Wicket.WebSocket.send("W.W_TERMINAL:" + JSON.stringify(msg));
			}
		};

		ctx.init(ctid);
		return $(this);
	}
})(jQuery);
