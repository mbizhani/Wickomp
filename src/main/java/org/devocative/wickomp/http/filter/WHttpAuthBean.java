package org.devocative.wickomp.http.filter;

import org.devocative.adroit.StringEncryptorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WHttpAuthBean {
	private static final Logger logger = LoggerFactory.getLogger(WHttpAuthBean.class);

	private final Map<String, String> authRqValues = new HashMap<>();
	private final WAuthMethod authMethod;

	// ------------------------------ CONSTRUCTORS

	public WHttpAuthBean() {
		authMethod = WAuthMethod.CUSTOM;
	}

	public WHttpAuthBean(String headerString) {
		WAuthMethod sentAuthMethod = WAuthMethod.UNKNOWN;

		if (headerString != null && headerString.trim().length() > 0) {
			int authMethodEndIdx = headerString.indexOf(" ");
			String method = headerString.substring(0, authMethodEndIdx).trim().toUpperCase();

			try {
				sentAuthMethod = WAuthMethod.valueOf(method);
			} catch (IllegalArgumentException e) {
				logger.error("WHttpAuthBean: Invalid HTTP method=[{}] header=[{}]", method, headerString);
			}
			authMethod = sentAuthMethod;

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
		} else {
			authMethod = sentAuthMethod;
		}
	}

	// ------------------------------ ACCESSORS

	public WAuthMethod getAuthMethod() {
		return authMethod;
	}

	public String getUsername() {
		return authRqValues.get("username");
	}

	void setUsername(String username) {
		authRqValues.put("username", username);
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
