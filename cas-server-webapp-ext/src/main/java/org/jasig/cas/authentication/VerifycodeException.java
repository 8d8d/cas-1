package org.jasig.cas.authentication;

import javax.security.auth.login.AccountException;

/**
 * 验证码异常
 * 
 * @author linzl
 *
 *         2016年11月7日
 */
public class VerifycodeException extends AccountException {

	/** Serialization metadata. */
	private static final long serialVersionUID = 7487835035108753209L;

	private static final String CODE = "error.authentication.imgverifycode.bad";

	/**
	 * Instantiates a new account disabled exception.
	 */
	public VerifycodeException() {
	}

	/**
	 * Instantiates a new account disabled exception.
	 *
	 * @param msg
	 *            the msg
	 */
	public VerifycodeException(final String msg) {
		super(msg);
	}
}
