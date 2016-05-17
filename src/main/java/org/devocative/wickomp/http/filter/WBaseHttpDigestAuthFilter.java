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
public abstract class WBaseHttpDigestAuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(WBaseHttpDigestAuthFilter.class);

	private enum EAuthResult {Ok, NoAuthHeader, NotDigest, InvalidNonce, InvalidUser, InvalidPassword}

	private boolean processAuth = true;

	public static final String RQ_AUTH_HEADER = "Authorization";
	public static final String RS_AUTH_HEADER = "WWW-Authenticate";

	@Override
	public void doFilter(ServletRequest srvRq, ServletResponse srvRsp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) srvRq;
		HttpServletResponse response = (HttpServletResponse) srvRsp;

		if (processAuth) {
			try {
				process(request, response, filterChain);
			} catch (Exception e) {
				logger.error("DigestAuthFilter: general error", e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "General Error");
			}
		} else {
			logger.warn("HttpDigestAuthFilter: Authentication Ignored!");
			filterChain.doFilter(request, response);
		}
	}

	// ------------------------------ ABSTRACT METHODS

	protected abstract String calculateNonce(WHttpAuthBean authBean);

	protected abstract String getRealm(WHttpAuthBean authBean);

	/**
	 * DigestUtils.md5Hex(userName + ":" + realm + ":" + password);
	 *
	 * @param authBean
	 * @return
	 */
	protected abstract String generateUserHash(WHttpAuthBean authBean);

	// ------------------------------ PROTECTED METHODS

	/**
	 * quality of protection
	 *
	 * @param authBean
	 * @return
	 */
	protected String getQop(WHttpAuthBean authBean) {
		return "auth";
	}

	protected void setProcessAuth(boolean processAuth) {
		this.processAuth = processAuth;
	}

	// ------------------------------ PRIVATE METHODS

	private void process(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String authRqHeader = request.getHeader(RQ_AUTH_HEADER);
		WHttpAuthBean authBean = new WHttpAuthBean(authRqHeader);

		EAuthResult authResult = authenticate(request, authBean);

		logger.info("DigestAuthFilter: RemoteAddr=[{}] User=[{}] AuthResult=[{}]",
			request.getRemoteAddr(), authBean.getUsername(), authResult);

		if (authResult == EAuthResult.Ok) {
			WHttpServletRequest rqWrap = new WHttpServletRequest(request);
			rqWrap
				.setAuthType("DIGEST")
				.setUserPrincipal(new WPrinciple(authBean.getUsername()));

			filterChain.doFilter(rqWrap, response);

		} else {
			String errorResultDesc = null;
			switch (authResult) {
				case NoAuthHeader:
					response.addHeader(RS_AUTH_HEADER, getRsAuthHeader(authBean));
					break;
				case NotDigest:
					errorResultDesc = "Only HTTP Digest authentication supported!";
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

		if (!"Digest".equals(authBean.getAuthMethod())) {
			return EAuthResult.NotDigest;
		}

		String ha1 = generateUserHash(authBean);

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
		String nonce = calculateNonce(authBean);
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
		String nonce = calculateNonce(authBean);
		String realm = getRealm(authBean);
		String qop = getQop(authBean);

		return String.format("Digest realm=\"%s\",qop=\"%s\",nonce=\"%s\",opaque=\"%s\"",
			realm, qop, nonce, getOpaque(realm, nonce));
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
