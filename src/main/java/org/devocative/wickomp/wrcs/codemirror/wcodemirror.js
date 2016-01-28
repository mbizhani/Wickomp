(function ($) {
    $.fn.codemirror = function (settings) {
        var cm = {
            editor: null,
            textAreaInput: null,

            init: function (textAreaInput, settings) {
                cm.textAreaInput = textAreaInput;
                cm.editor = CodeMirror.fromTextArea(cm.textAreaInput[0], settings);
                cm.editor.on("change", function () {
                    cm.editor.save();
                });
            }
        };
        cm.init($(this), settings);
    };
})(jQuery);