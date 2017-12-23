(function ($) {
	$(window).on("beforeunload", function (e) {
		return "Are you sure?";
	});

	$.fn.wTerminal = function (options) {
		var defaults = {
			cursorBlink: true,
			//screenKeys: false,
			//useStyle: true,
			convertEol: true,
			rows: 35,
			cols: 100
		};

		var ctx = {
			PREFIX: "W.W_TERMINAL:",
			term: null,
			target: $(this),
			ctid: "",
			charWidth: 0,
			charHeight: 0,
			size: null,

			init: function (opt) {
				wLog.info("wTerminal Init: ", opt);

				ctx.ctid = opt["clientTermId"];
				ctx.charWidth = opt["charWidth"];
				ctx.charHeight = opt["charHeight"];

				if (!Wicket && !Wicket.Event && !Wicket.Event.subscribe) {
					wLog.error("No Wicket.Event.subscribe!");
					alert("No Wicket.Event.subscribe!");
					return;
				}

				ctx.term = new Terminal(defaults);
				ctx.term.open(ctx.target[0]);
				ctx.size = ctx.calculateRowsCols();
				ctx.term.resize(ctx.size.cols, ctx.size.rows);
				ctx.term.write("Connecting ...\n");

				try {
					ctx.term.textarea.onpaste = function (e) {
						wLog.debug("wTerminal: term.textarea.onpaste");

						if (e.clipboardData && e.clipboardData.getData) {
							var clipData = e.clipboardData.getData("text/plain");
							if (clipData) {
								wLog.debug("wTerminal: term.textarea.onpaste -> event.clipboardData", clipData);
								ctx.send("key", clipData);
							}
						}
					};
				} catch (err) {
					wLog.error("wTerminal: can't assign onpaste event", err);
				}

				Wicket.Event.subscribe("/websocket/message", function (jqEvent, message) {
					if (message && message.startsWith(ctx.PREFIX)) {
						try {
							var parse = JSON.parse(message.substring(ctx.PREFIX.length));
							if (parse && parse["ctid"] && parse.ctid == ctx.ctid) {
								ctx.term.write(parse.text);
							}
						} catch (e) {
							wLog.error("wTerminal: response handling problem: ", e);
						}
					}
				});

				Wicket.Event.subscribe("/websocket/closed", function (jqEvent, message) {
					wLog.warn("wTerminal: websocket/closed!", message);
					ctx.term.write("\nERR: WebSocket is Disconnected!\n");
				});

				Wicket.Event.subscribe("/websocket/error", function (jqEvent, message) {
					wLog.error("wTerminal: websocket/error!", message);
				});

				var isSpecialKey = false;

				ctx.term.on("keydown", function (ev) {
					isSpecialKey = true;
					//wLog.debug('keydown', ev);
				});

				ctx.term.on("key", function (key, ev) {
					//wLog.debug('key', ev);

					if (isSpecialKey) {
						ctx.send("specialKey", "" + ev.keyCode);
					} else {
						ctx.send("key", key);
					}
					isSpecialKey = false;
				});

				new ResizeSensor(ctx.target, function () {
					wLog.debug("wTerminal.target.resize");
					ctx.size = ctx.calculateRowsCols();
					ctx.term.resize(ctx.size.cols, ctx.size.rows);
					ctx.send("resize", JSON.stringify(ctx.size));
				});

				ctx.send("init", JSON.stringify(ctx.size));
			},

			calculateRowsCols: function () {
				var result = {};

				var width = ctx.target.width() - 10;
				result["cols"] = Math.floor(width / ctx.charWidth);
				if (result["cols"] == 0) {
					result["cols"] = defaults.cols;
				}

				var height = (ctx.target.height() > 0 ? ctx.target.height() : ctx.target.parent().height()) - 10;
				result["rows"] = Math.floor(height / ctx.charHeight);
				if (result["rows"] == 0) {
					result["rows"] = defaults.rows;
				}

				wLog.debug("wTerminal.calculateRowsCols: ", width, height, result);
				return result;
			},

			send: function (cmd, value) {
				var msg = {
					ctid: ctx.ctid,
					cmd: cmd
				};
				if (value) {
					msg["value"] = value;
				}
				Wicket.WebSocket.send(ctx.PREFIX + JSON.stringify(msg));
			}
		};

		ctx.init(options);
		return $(this);
	}
})(jQuery);
