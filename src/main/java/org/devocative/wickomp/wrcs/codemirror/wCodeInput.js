(function ($) {
	$.fn.wCodeInput = function (settings) {
		var defaults = {
			autofocus: true,
			indentWithTabs: true,
			lineNumbers: true,
			lineWrapping: true,
			matchBrackets: true,
			readOnly: false,
			resizable: true,
			smartIndent: true,

			// ---------------

			enabled: true,
			submitSelection: false
		};

		var cm = {
			editor: null,
			textAreaInput: null,

			init: function (textAreaInput, settings) {
				var opt = $.extend({}, defaults, settings);
				if (!opt.enabled) {
					opt.readOnly = true;
				}

				cm.textAreaInput = textAreaInput;
				cm.editor = CodeMirror.fromTextArea(cm.textAreaInput[0], opt);
				cm.editor.on("change", function () {
					cm.editor.save();
				});

				if (opt.submitSelection) {
					wLog.info("wCodeInput.submitSelection");

					cm.editor.on("cursorActivity", function (instance) {
						if (instance.somethingSelected()) {
							cm.textAreaInput.val(instance.getSelection());
						} else {
							cm.editor.save();
						}
					});
				}

				if (!opt.enabled) {
					cm.textAreaInput.parent().find("div.CodeMirror-scroll").css("background-color", "#eeeeee");
				}

				if (opt.width || opt.height) {
					cm.editor.setSize(opt.width, opt.height);
				}

				if (opt.resizable) {
					cm.textAreaInput.siblings(".CodeMirror").resizable({handles: "s"});
				}
			}
		};

		if (settings == "clear") {
			$(this).data("cm").setValue("");
		} else {
			cm.init($(this), settings);
			$(this).data("cm", cm.editor);
		}
		return this;
	};
})(jQuery);