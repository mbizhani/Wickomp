(function ($) {
	var wTabbedPanelDefaults = {
		globalHotkeyEnabled: false,
		changeTabKeyCode: 191,
		closeTabKeyCode: 190
	};

	$.fn.wTabbedPanel = function (cmdOrOpts, options) {

		var ctx = {
			opt: null,
			target: $(this),

			init: function (opts) {
				ctx.opt = $.extend({}, wTabbedPanelDefaults, opts);
				wLog.debug("wTabbedPanel: init", ctx.opt);

				ctx.opt["onBeforeClose"] = function (title, index) {
					wLog.debug("wTabbedPanel: onBeforeClose: t=" + title + ", i=" + index);

					//this section is copied from its doc on the site
					$.messager.confirm(wMsg.warning, wMsg.wTabbedPanel.sureToClose + '"' + title + '"?', function (r) {
						if (r) {
							var opts = ctx.target.tabs("options");
							var bc = opts.onBeforeClose;
							opts.onBeforeClose = function () {
							};
							ctx.target.tabs("close", index);
							opts.onBeforeClose = bc;
						}
					});
					return false;
				};

				ctx.opt["onClose"] = function (title, index) {
					wLog.info("wTabbedPanel: onClose: t=" + title + ", i=" + index);

					if (ctx.opt["url"]) {
						wLog.debug("wTabbedPanel: onClose.callServer");
						Wicket.Ajax.get({u: ctx.opt["url"] + "&index=" + index});
					} else {
						wLog.error("wTabbedPanel: no url for calling server on tab close");
					}
				};

				ctx.target.tabs(ctx.opt);

				if (ctx.opt.globalHotkeyEnabled) {
					$(window).keydown(function (e) {
						if (e.altKey) {
							if (!e.shiftKey && e.keyCode == ctx.opt.changeTabKeyCode) {
								ctx.selectNextTab();
								e.preventDefault();
							} else if (e.shiftKey && e.keyCode == ctx.opt.changeTabKeyCode) {
								ctx.selectPrevTab();
								e.preventDefault();
							} else if (e.keyCode == ctx.opt.closeTabKeyCode) {
								ctx.closeCurrentTab();
								e.preventDefault();
							}
						}
					});
				}
			},

			add: function (opts) {
				wLog.debug("wTabbedPanel: add");

				var item = $("<div>");
				item.attr("id", opts["htmlId"]);

				ctx.target.tabs("add", {
					title: opts["title"] ? opts["title"] : "[?]",
					content: item,
					closable: opts["closable"] ? opts["closable"] : false
				});
			},

			selectNextTab: function () {
				var length = ctx.target.tabs("tabs").length;
				var currentTabIdx = ctx.target.tabs("getTabIndex", ctx.target.tabs("getSelected"));
				var nextIdx = (currentTabIdx + 1) % length;
				ctx.target.tabs("select", nextIdx);
				wLog.debug("Select Next Tab: " + nextIdx);
			},

			selectPrevTab: function () {
				var length = ctx.target.tabs("tabs").length;
				var currentTabIdx = ctx.target.tabs("getTabIndex", ctx.target.tabs("getSelected"));
				var prevIdx = (length + currentTabIdx - 1) % length;
				ctx.target.tabs("select", prevIdx);
				wLog.debug("Select Prev Tab: " + prevIdx);
			},

			closeCurrentTab: function () {
				var currTab = ctx.target.tabs("getSelected");
				var currentTabIdx = ctx.target.tabs("getTabIndex", currTab);
				wLog.debug("Close Curr Tab: " + currentTabIdx, currTab.panel("options").tab);
				if (currTab.panel("options").closable) {
					ctx.target.tabs("close", currentTabIdx);
				}
			}
		};

		if (!cmdOrOpts && !options) {
			ctx.init();
		} else if (typeof(cmdOrOpts) === "object") {
			ctx.init(cmdOrOpts);
		} else if (typeof(cmdOrOpts) === "string") {
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