package org.devocative.wickomp.http.filter;

import org.devocative.adroit.StringEncryptorUtil;

import java.util.HashMap;
import java.util.Map;

public class WHttpAuthBean {
	private Map<String, String> authRqValues = new HashMap<String, String>();
	private WAuthMethod authMethod;

	// ------------------------------ CONSTRUCTORS

	public WHttpAuthBean(String headerString) {
		if (headerString != null && headerString.trim().length() > 0) {
			int authMethodEndIdx = headerString.indexOf(" ");
			String method = headerString.substring(0, authMethodEndIdx).trim().toUpperCase();

			try {
				authMethod = WAuthMethod.valueOf(method);
			} catch (IllegalArgumentException e) {
				authMethod = WAuthMethod.UNKNOWN;
			}

			String headerStringWithoutScheme = headerString.substring(authMethodEndIdx + 1).trim();

			switch (authMethod) {
				case BASIC:
					String usernamePass = StringEncryptorUtil.decodeBase64(headerStringWithoutScheme);
					String[] split = usernamePass.split(":");
					if (split.length == 2) {
						authRqValues.put("username", split[0]);
						authRqValues.put("password", split[1]);
					}
					break;

				case DIGEST:
					String keyValueArray[] = headerStringWithoutScheme.split(",");
					for (String keyValueStr : keyValueArray) {
						String[] keyValue = keyValueStr.split("=");
						if (keyValue.length == 2) {
							String key = keyValue[0].trim();
							String value = keyValue[1].replaceAll("\"", "").trim();
							authRqValues.put(key, value);
						}
					}
					break;
			}
		}
	}

	// ------------------------------ ACCESSORS

	public WAuthMethod getAuthMethod() {
		return authMethod;
	}

	public String getUsername() {
		return authRqValues.get("username");
	}

	public String getPassword() {
		return authRqValues.get("password");
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
