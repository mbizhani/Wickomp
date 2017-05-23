package org.devocative.wickomp.form.code;

public enum OCodeMode {
	XML("xml", "xml", true),
	SQL("text/x-sql", "sql", true),
	PL_SQL("text/x-plsql", "sql", true),
	MS_SQL("text/x-mssql", "sql", true),
	MY_SQL("text/x-mysql", "sql", true),
	GROOVY("text/x-groovy", "groovy", false);

	private String mode;
	private String jsFile;
	private boolean hasHint;

	OCodeMode(String mode, String jsFile, boolean hasHint) {
		this.mode = mode;
		this.jsFile = jsFile;
		this.hasHint = hasHint;
	}

	public String getJsFile() {
		return jsFile;
	}

	public boolean isHasHint() {
		return hasHint;
	}

	@Override
	public String toString() {
		return mode;
	}
}
