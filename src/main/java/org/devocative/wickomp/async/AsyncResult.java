package org.devocative.wickomp.async;

public class AsyncResult {
	private AsyncToken token;
	private Object result;
	private Exception error;

	public AsyncToken getToken() {
		return token;
	}

	public AsyncResult setToken(AsyncToken token) {
		this.token = token;
		return this;
	}

	public Object getResult() {
		return result;
	}

	public AsyncResult setResult(Object result) {
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
