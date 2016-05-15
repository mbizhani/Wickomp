package org.devocative.wickomp.http.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class WHttpServletRequest extends HttpServletRequestWrapper {
	private String authType;
	private Principal userPrincipal;

	public WHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getRemoteUser() {
		return userPrincipal != null ? userPrincipal.getName() : null;
	}

	@Override
	public String getAuthType() {
		return authType;
	}

	public WHttpServletRequest setAuthType(String authType) {
		this.authType = authType;
		return this;
	}

	@Override
	public Principal getUserPrincipal() {
		return userPrincipal;
	}

	public WHttpServletRequest setUserPrincipal(Principal userPrincipal) {
		this.userPrincipal = userPrincipal;
		return this;
	}
}
