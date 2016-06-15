package org.devocative.wickomp;

import org.devocative.wickomp.http.filter.WAuthMethod;
import org.devocative.wickomp.http.filter.WBaseHttpAuthFilter;
import org.devocative.wickomp.http.filter.WHttpAuthBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class TestHttpBasicAuthFilter extends WBaseHttpAuthFilter {
	private static final Logger logger = LoggerFactory.getLogger(TestHttpBasicAuthFilter.class);

	private static final String USERNAME = "test";
	private static final String PASSWORD = "test";

	@Override
	protected String calculateNonceForDigest(WHttpAuthBean authBean) {
		logger.info("TestHttpBasicAuthFilter.calculateNonceForDigest");
		throw new RuntimeException("Digest Only!");
		//return "MyNonce";
	}

	@Override
	protected String getRealm(WHttpAuthBean authBean) {
		logger.info("TestHttpBasicAuthFilter.getRealm");
		return "MyRealm";
	}

	@Override
	protected String generateUserHashForDigest(WHttpAuthBean authBean) {
		logger.info("TestHttpBasicAuthFilter.generateUserHashForDigest");
		throw new RuntimeException("Digest Only!");
		//return "";
	}

	@Override
	protected boolean authenticateByPasswordForBasic(String username, String password) {
		logger.info("TestHttpBasicAuthFilter.authenticateByPasswordForBasic: username=[{}] password=[{}]", username, password);
		return USERNAME.equals(username) && PASSWORD.equals(password);
	}

	// ------------------------------

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		setDesiredAuthMethod(WAuthMethod.BASIC);
	}

	@Override
	public void destroy() {
	}
}
