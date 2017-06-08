//package org.jasig.cas.web.flow;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang3.StringUtils;
//import org.jasig.cas.authentication.Credential;
//import org.jasig.cas.authentication.UsernamePasswordCredentialExt;
//import org.jasig.cas.authentication.VerifycodeException;
//import org.jasig.cas.web.support.WebUtils;
//import org.springframework.binding.message.MessageContext;
//import org.springframework.stereotype.Component;
//import org.springframework.webflow.core.collection.LocalAttributeMap;
//import org.springframework.webflow.execution.Event;
//import org.springframework.webflow.execution.RequestContext;
//
//import com.google.code.kaptcha.Constants;
//
//@Component("authenticationViaFormActionExt")
//public class AuthenticationViaFormActionExt extends AuthenticationViaFormAction {
//
//	public final Event submitExt(final RequestContext context, final Credential credential,
//			final MessageContext messageContext) {
//		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
//		HttpSession session = request.getSession();
//
//		UsernamePasswordCredentialExt upc = (UsernamePasswordCredentialExt) credential;
//		String captchacode = upc.getVerifycode();
//
//		if (StringUtils.isBlank(captchacode)) {
//			// populateErrorsInstance(new
//			// NullImgVerifyCodeAuthenticationException(), messageContext);
//			return newEvent(AbstractCasWebflowConfigurer.TRANSITION_ID_ERROR, new VerifycodeException());
//		}
//
//		if (StringUtils.equalsIgnoreCase((String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY), captchacode)) {
//			return super.submit(context, credential, messageContext);
//		}
//
//		return newEvent(AbstractCasWebflowConfigurer.TRANSITION_ID_ERROR, new VerifycodeException());
//		// populateErrorsInstance(new BadImgVerifyCodeAuthenticationException(),
//		// messageContext);
//	}
//
//	/**
//	 * New event based on the given id.
//	 *
//	 * @param id
//	 *            the id
//	 * @return the event
//	 */
//	private Event newEvent(final String id) {
//		return new Event(this, id);
//	}
//
//	/**
//	 * New event based on the id, which contains an error attribute referring to
//	 * the exception occurred.
//	 *
//	 * @param id
//	 *            the id
//	 * @param error
//	 *            the error
//	 * @return the event
//	 */
//	private Event newEvent(final String id, final Exception error) {
//		return new Event(this, id, new LocalAttributeMap("error", error));
//	}
//}
