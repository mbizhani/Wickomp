package org.devocative.wickomp.form.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devocative.wickomp.opt.Options;

import java.util.HashMap;
import java.util.Map;

public class OCode extends Options {
	private static final long serialVersionUID = 711452574514745355L;

	private Boolean autofocus = true;
	private Object hintOptions;
	private Boolean indentWithTabs = true;
	private Boolean lineNumbers = true;
	private Boolean matchBrackets = true;
	private OCodeMode mode;
	private Boolean readOnly; //default is false
	private Boolean smartIndent = true;

	private Boolean showMatchingBrackets = true;

	// ------------------------------ CONSTRUCTORS

	public OCode(OCodeMode mode) {
		this.mode = mode;
	}

	// ------------------------------ ACCESSORS

	public Boolean getAutofocus() {
		return autofocus;
	}

	public OCode setAutofocus(Boolean autofocus) {
		this.autofocus = autofocus;
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

	public Boolean getMatchBrackets() {
		return matchBrackets;
	}

	public OCode setMatchBrackets(Boolean matchBrackets) {
		this.matchBrackets = matchBrackets;
		return this;
	}

	public OCodeMode getMode() {
		return mode;
	}

	public OCode setMode(OCodeMode mode) {
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

	public Boolean getSmartIndent() {
		return smartIndent;
	}

	public OCode setSmartIndent(Boolean smartIndent) {
		this.smartIndent = smartIndent;
		return this;
	}

	// ------------------------------

	public Object getExtraKeys() {
		if(mode.isHasHint()) {
			Map<String, String> map = new HashMap<>();
			map.put("Ctrl-Space", "autocomplete");
			return map;
		}
		return null;
	}

	// ------------------------------

	@JsonIgnore
	public Boolean getShowMatchingBrackets() {
		return showMatchingBrackets;
	}

	public OCode setShowMatchingBrackets(Boolean showMatchingBrackets) {
		this.showMatchingBrackets = showMatchingBrackets;
		return this;
	}
}
