package org.devocative.wickomp.async;

import java.io.Serializable;

class AsyncResult {
	private AsyncToken token;
	private Serializable result;
	private Exception error;

	public AsyncToken getToken() {
		return token;
	}

	public AsyncResult setToken(AsyncToken token) {
		this.token = token;
		return this;
	}

	public Serializable getResult() {
		return result;
	}

	public AsyncResult setResult(Serializable result) {
		this.result = result;
		return this;
	}

	public Exception getError() {
		return error;
	}

	public AsyncResult setError(Exception error) {
		this.error = error;
		return this;
	}

	@Override
	public String toString() {
		return "AsyncResult{" +
			"token=" + token +
			", result=" + result +
			'}';
	}
}
