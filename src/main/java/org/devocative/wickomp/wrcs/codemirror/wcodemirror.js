(function ($) {
	$.fn.wCodeMirror = function (settings) {
		var defaults = {
			autofocus: true,
			indentWithTabs: true,
			lineNumbers: true,
			lineWrapping: true,
			matchBrackets: true,
			resizable: true,
			smartIndent: true
		};

		var cm = {
			editor: null,
			textAreaInput: null,

			init: function (textAreaInput, settings) {
				var opt = $.extend({}, defaults, settings);
				cm.textAreaInput = textAreaInput;
				cm.editor = CodeMirror.fromTextArea(cm.textAreaInput[0], opt);
				cm.editor.on("change", function () {
					cm.editor.save();
				});

				if (opt.width || opt.height) {
					cm.editor.setSize(opt.width, opt.height);
				}

				if (opt.resizable) {
					cm.textAreaInput.siblings(".CodeMirror").resizable({handles: 's'});
				}
			}
		};
		cm.init($(this), settings);
	};
})(jQuery);