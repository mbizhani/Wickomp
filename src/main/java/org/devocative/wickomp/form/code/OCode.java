package org.devocative.wickomp.form.code;

import org.devocative.wickomp.opt.Options;

import java.util.HashMap;
import java.util.Map;

public class OCode extends Options {
	private Boolean autofocus = true;
	private Object hintOptions;
	private Boolean indentWithTabs = true;
	private Boolean lineNumbers = true;
	private Boolean matchBrackets = true;
	private OCodeMode mode;
	private Boolean smartIndent = true;

	public OCode(OCodeMode mode) {
		this.mode = mode;
	}

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

	public Boolean getSmartIndent() {
		return smartIndent;
	}

	public OCode setSmartIndent(Boolean smartIndent) {
		this.smartIndent = smartIndent;
		return this;
	}



	public Object getExtraKeys() {
		if(mode.isHasHint()) {
			Map<String, String> map = new HashMap<>();
			map.put("Ctrl-Space", "autocomplete");
			return map;
		}
		return null;
	}
}
