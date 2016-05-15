package org.devocative.wickomp.http.filter;

import java.util.HashMap;
import java.util.Map;

public class WHttpAuthBean {
	private Map<String, String> authRqValues = new HashMap<String, String>();
	private String authMethod;

	// ------------------------------ CONSTRUCTORS

	public WHttpAuthBean(String headerString) {
		if (headerString != null && headerString.trim().length() > 0) {
			int authMethodEndIdx = headerString.indexOf(" ");
			authMethod = headerString.substring(0, authMethodEndIdx).trim();

			String headerStringWithoutScheme = headerString.substring(authMethodEndIdx + 1).trim();
			String keyValueArray[] = headerStringWithoutScheme.split(",");
			for (String keyValueStr : keyValueArray) {
				String[] keyValue = keyValueStr.split("=");
				if (keyValue.length == 2) {
					String key = keyValue[0].trim();
					String value = keyValue[1].replaceAll("\"", "").trim();
					authRqValues.put(key, value);
				}
			}
		}
	}

	// ------------------------------ ACCESSORS

	public String getUsername() {
		return authRqValues.get("username");
	}

	public String getAuthMethod() {
		return authMethod;
	}

	public String getQop() {
		return authRqValues.get("qop");
	}

	public String getUri() {
		return authRqValues.get("uri");
	}

	public String getNonceCount() {
		return authRqValues.get("nc");
	}

	public String getServerNonce() {
		return authRqValues.get("nonce");
	}

	public String getClientNonce() {
		return authRqValues.get("cnonce");
	}

	public String getClientResponse() {
		return authRqValues.get("response");
	}

	// ------------------------------ METHODS

	public boolean hasRequestAuth() {
		return authRqValues.size() > 0;
	}

	public boolean hasQop() {
		String qop = getQop();
		return qop != null && qop.trim().length() > 0;
	}
}
