(function ($) {
	$.fn.codemirror = function (settings) {
		var defaults = {
			autofocus: true,
			indentWithTabs: true,
			lineNumbers: true,
			matchBrackets: true,
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
			}
		};
		cm.init($(this), settings);
	};
})(jQuery);