package org.devocative.wickomp.http.filter;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Based on: https://en.wikipedia.org/wiki/Digest_access_authentication
 */
public abstract class WBaseHttpAuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(WBaseHttpAuthFilter.class);
	private static final String RQ_AUTH_HEADER = "Authorization";
	private static final String RS_AUTH_HEADER = "WWW-Authenticate";

	// ------------------------------

	private enum EAuthResult {Ok, NoAuthHeader, NoAuthMethod, InvalidNonce, InvalidUser, InvalidPassword}

	// ------------------------------

	private boolean processAuth = true;
	private WAuthMethod desiredAuthMethod = WAuthMethod.BASIC;

	// ------------------------------

	@Override
	public void doFilter(ServletRequest srvRq, ServletResponse srvRsp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srvRq;
		HttpServletResponse response = (HttpServletResponse) srvRsp;

		if (processAuth) {
			try {
				process(request, response, filterChain);
			} catch (Exception e) {
				logger.error("HttpAuthFilter: general error", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "General Error");
			}
		} else {
			logger.warn("HttpAuthFilter: Authentication Ignored!");
			filterChain.doFilter(request, response);
		}
	}

	// ------------------------------

	public WAuthMethod getDesiredAuthMethod() {
		return desiredAuthMethod;
	}

	public void setDesiredAuthMethod(WAuthMethod desiredAuthMethod) {
		this.desiredAuthMethod = desiredAuthMethod;
	}

	// ------------------------------ ABSTRACT METHODS

	protected abstract String calculateNonceForDigest(WHttpAuthBean authBean);

	protected abstract String getRealm(WHttpAuthBean authBean);

	/**
	 * DigestUtils.md5Hex(userName + ":" + realm + ":" + password);
	 *
	 * param authBean
	 * return
	 */
	protected abstract String generateUserHashForDigest(WHttpAuthBean authBean);

	protected abstract boolean authenticateByPasswordForBasic(String username, String password);

	// ------------------------------ PROTECTED METHODS

	/**
	 * quality of protection
	 *
	 * param authBean
	 * return
	 */
	protected String getQop(WHttpAuthBean authBean) {
		return "auth";
	}

	protected boolean isProcessAuth() {
		return processAuth;
	}

	protected void setProcessAuth(boolean processAuth) {
		this.processAuth = processAuth;
	}

	protected void onBeforeChainAuthenticated(WHttpAuthBean authBean) {
	}

	protected void onAfterChainAuthenticated(WHttpAuthBean authBean) {
	}

	// ------------------------------ PRIVATE METHODS

	private void process(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String authRqHeader = request.getHeader(RQ_AUTH_HEADER);
		WHttpAuthBean authBean = new WHttpAuthBean(authRqHeader);

		EAuthResult authResult = authenticate(request, authBean);

		logger.info("HttpAuthFilter: User=[{}] RemoteAddress=[{}] AuthResult=[{}] AuthMethod=[{}]",
			authBean.getUsername(), request.getRemoteAddr(), authResult, authBean.getAuthMethod());

		if (authResult == EAuthResult.Ok) {
			WHttpServletRequest rqWrap = new WHttpServletRequest(request);
			rqWrap
				.setAuthType(authBean.getAuthMethod().name())
				.setUserPrincipal(new WPrinciple(authBean.getUsername()));

			onBeforeChainAuthenticated(authBean);

			filterChain.doFilter(rqWrap, response);

			onAfterChainAuthenticated(authBean);

		} else {
			String errorResultDesc = null;
			switch (authResult) {
				case NoAuthHeader:
					response.addHeader(RS_AUTH_HEADER, getRsAuthHeader(authBean));
					break;
				case NoAuthMethod:
					errorResultDesc = "Only HTTP Basic/Digest authentication supported!";
					break;
				case InvalidNonce:
					errorResultDesc = "Invalid login state. Retry!";
					response.addHeader(RS_AUTH_HEADER, getRsAuthHeader(authBean));
					break;
				case InvalidUser:
				case InvalidPassword:
					errorResultDesc = "Invalid username/password";
					response.addHeader(RS_AUTH_HEADER, getRsAuthHeader(authBean));
					break;
			}
			response.addHeader(RS_AUTH_HEADER, getRsAuthHeader(authBean));
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorResultDesc);
		}
	}

	private EAuthResult authenticate(HttpServletRequest request, WHttpAuthBean authBean) {
		if (!authBean.hasRequestAuth()) {
			return EAuthResult.NoAuthHeader;
		}

		switch (authBean.getAuthMethod()) {
			case BASIC:
				return authenticateBasic(authBean);
			case DIGEST:
				return authenticateDigest(request, authBean);
			default:
				return EAuthResult.NoAuthMethod;
		}
	}

	private EAuthResult authenticateBasic(WHttpAuthBean authBean) {
		if (authBean.getUsername() == null) {
			return EAuthResult.InvalidUser;
		}

		if (authenticateByPasswordForBasic(authBean.getUsername(), authBean.getPassword())) {
			return EAuthResult.Ok;
		}

		return EAuthResult.InvalidPassword;
	}

	private EAuthResult authenticateDigest(HttpServletRequest request, WHttpAuthBean authBean) {
		String ha1 = generateUserHashForDigest(authBean);

		if (ha1 == null) {
			return EAuthResult.InvalidUser;
		}

		String ha2;
		if ("auth-int".equals(authBean.getQop())) {
			String requestBodyMd5 = DigestUtils.md5Hex(readRequestBody(request));
			ha2 = DigestUtils.md5Hex(request.getMethod() + ":" + authBean.getUri() + ":" + requestBodyMd5);
		} else {
			ha2 = DigestUtils.md5Hex(request.getMethod() + ":" + authBean.getUri());
		}

		String serverResponse;
		String nonce = calculateNonceForDigest(authBean);
		if (authBean.hasQop()) {
			serverResponse = DigestUtils.md5Hex(
				ha1 + ":" +
					nonce + ":" +
					authBean.getNonceCount() + ":" +
					authBean.getClientNonce() + ":" +
					authBean.getQop() + ":" +
					ha2
			);
		} else {
			serverResponse = DigestUtils.md5Hex(ha1 + ":" + nonce + ":" + ha2);
		}

		if (serverResponse.equals(authBean.getClientResponse())) {
			return EAuthResult.Ok;
		} else {
			if (!nonce.equals(authBean.getServerNonce())) {
				return EAuthResult.InvalidNonce;
			} else {
				return EAuthResult.InvalidPassword;
			}
		}
	}

	private String getRsAuthHeader(WHttpAuthBean authBean) {
		switch (getDesiredAuthMethod()) {

			case BASIC:
				return "Basic";

			case DIGEST:
				String nonce = calculateNonceForDigest(authBean);
				String realm = getRealm(authBean);
				String qop = getQop(authBean);

				return String.format("Digest realm=\"%s\",qop=\"%s\",nonce=\"%s\",opaque=\"%s\"",
					realm, qop, nonce, getOpaque(realm, nonce));

			default:
				throw new RuntimeException("Invalid desired authentication method: " + getDesiredAuthMethod());
		}
	}

	private String getOpaque(String domain, String nonce) {
		return DigestUtils.md5Hex(domain + nonce);
	}

	private String readRequestBody(HttpServletRequest request) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		}
		return stringBuilder.toString();
	}

}
