package org.jasig.cas.authentication;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 扩展自UsernamePasswordCredential,增加了验证码,参照RememberMeUsernamePasswordCredential
 * 
 * @author linzl
 *
 *         2016年11月7日
 */
public class UsernamePasswordCredentialExt extends UsernamePasswordCredential {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4209535951191251589L;

	/** 验证码 */
	@NotNull
	@Size(min = 1, message = "required.verifycode")
	private String verifycode;

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	/** Default constructor. */
	public UsernamePasswordCredentialExt() {
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final UsernamePasswordCredentialExt that = (UsernamePasswordCredentialExt) o;

		if (verifycode != null ? !verifycode.equals(that.verifycode) : that.verifycode != null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(super.hashCode()).append(verifycode).toHashCode();
	}

}
