package org.devocative.wickomp.form.code;

import org.devocative.wickomp.opt.OSize;
import org.devocative.wickomp.opt.Options;

import java.util.HashMap;
import java.util.Map;

public class OCode extends Options {
	private static final long serialVersionUID = 711452574514745355L;

	private Boolean autofocus; //JS Default: TRUE
	private OSize height;
	private Object hintOptions;
	private Boolean indentWithTabs; //JS Default: TRUE
	private Boolean lineNumbers; //JS Default: TRUE
	private Boolean lineWrapping; //JS Default: TRUE
	private Boolean matchBrackets; //JS Default: TRUE
	private ICodeMode mode;
	private Boolean readOnly; //JS Default: FALSE
	private Boolean resizable; //JS Default: TRUE
	private Boolean smartIndent; //JS Default: TRUE
	private String theme = "mbo";
	private OSize width;

	// ---------------

	private Boolean enabled; //JS Default: TRUE
	private Boolean submitSelection; //JS Default: FALSE

	// ------------------------------

	public OCode(OCodeMode mode) {
		setMode(mode);
	}

	// ------------------------------

	public Boolean getAutofocus() {
		return autofocus;
	}

	public OCode setAutofocus(Boolean autofocus) {
		this.autofocus = autofocus;
		return this;
	}

	public OSize getHeight() {
		return height;
	}

	public OCode setHeight(OSize height) {
		this.height = height;
		return this;
	}

	public Object getHintOptions() {
		return hintOptions;
	}

	public OCode setHintOptions(Object hintOptions) {
		this.hintOptions = hintOptions;
		return this;
	}

	public Boolean getIndentWithTabs() {
		return indentWithTabs;
	}

	public OCode setIndentWithTabs(Boolean indentWithTabs) {
		this.indentWithTabs = indentWithTabs;
		return this;
	}

	public Boolean getLineNumbers() {
		return lineNumbers;
	}

	public OCode setLineNumbers(Boolean lineNumbers) {
		this.lineNumbers = lineNumbers;
		return this;
	}

	public Boolean getLineWrapping() {
		return lineWrapping;
	}

	public OCode setLineWrapping(Boolean lineWrapping) {
		this.lineWrapping = lineWrapping;
		return this;
	}

	public Boolean getMatchBrackets() {
		return matchBrackets;
	}

	public OCode setMatchBrackets(Boolean matchBrackets) {
		this.matchBrackets = matchBrackets;
		return this;
	}

	public ICodeMode getMode() {
		return mode;
	}

	public OCode setMode(ICodeMode mode) {
		this.mode = mode;
		return this;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public OCode setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public Boolean getResizable() {
		return resizable;
	}

	public OCode setResizable(Boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public Boolean getSmartIndent() {
		return smartIndent;
	}

	public OCode setSmartIndent(Boolean smartIndent) {
		this.smartIndent = smartIndent;
		return this;
	}

	public String getTheme() {
		return theme;
	}

	public OCode setTheme(String theme) {
		this.theme = theme;
		return this;
	}

	public OSize getWidth() {
		return width;
	}

	public OCode setWidth(OSize width) {
		this.width = width;
		return this;
	}

	// ---------------

	public Object getExtraKeys() {
		if (mode.isHasHint()) {
			Map<String, String> map = new HashMap<>();
			map.put("Ctrl-Space", "autocomplete");
			return map;
		}
		return null;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	OCode setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Boolean getSubmitSelection() {
		return submitSelection;
	}

	public OCode setSubmitSelection(Boolean submitSelection) {
		this.submitSelection = submitSelection;
		return this;
	}
}
