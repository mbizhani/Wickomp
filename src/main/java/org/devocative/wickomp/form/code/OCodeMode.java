package org.devocative.wickomp.form.code;

public enum OCodeMode implements ICodeMode {
	GROOVY("text/x-groovy", "groovy", false),

	JAVA_SCRIPT("text/javascript", "javascript", false),
	JSON("application/json", "javascript", false),
	TYPE_SCRIPT("text/typescript", "javascript", false),

	MS_SQL("text/x-mssql", "sql", true),
	MY_SQL("text/x-mysql", "sql", true),
	PG_SQL("text/x-pgsql", "sql", true),
	PL_SQL("text/x-plsql", "sql", true),
	SQL("text/x-sql", "sql", true),

	SHELL("text/x-sh", "shell", false),
	XML("xml", "xml", true);

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
