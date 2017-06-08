package org.jasig.cas.support.pac4j.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.CasProtocolConstants;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationSystemSupport;
import org.jasig.cas.authentication.DefaultAuthenticationSystemSupport;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.web.support.WebUtils;
import org.pac4j.core.client.Clients;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * This class represents an action to put at the beginning of the webflow.
 * <p>
 * Before any authentication, redirection urls are computed for the different
 * clients defined as well as the theme, locale, method and service are saved
 * into the web session.
 * </p>
 * After authentication, appropriate information are expected on this callback
 * url to finish the authentication process with the provider.
 * 
 * @author Jerome Leleu
 * @since 3.5.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component("thirdLoignAction")
public final class ThirdLoignAction extends AbstractAction {
	/**
	 * The logger.
	 */
	private final transient Logger logger = LoggerFactory.getLogger(ThirdLoignAction.class);

	/**
	 * The clients used for authentication.
	 */
	@NotNull
	@Autowired
	@Qualifier("builtClients")
	private Clients clients;

	@NotNull
	@Autowired(required = false)
	@Qualifier("defaultAuthenticationSystemSupport")
	private AuthenticationSystemSupport authenticationSystemSupport = new DefaultAuthenticationSystemSupport();

	/**
	 * The service for CAS authentication.
	 */
	@NotNull
	@Autowired
	private CentralAuthenticationService centralAuthenticationService;

	static {
		ProfileHelper.setKeepRawData(true);
	}

	/**
	 * Build the ThirdLoignAction.
	 */
	public ThirdLoignAction() {
	}

	@Override
	protected Event doExecute(final RequestContext context) throws Exception {
		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
		final HttpSession session = request.getSession();

		// web context
		final WebContext webContext = new J2EContext(request, response);

		// get client
		final String clientName = request.getParameter(this.clients.getClientNameParameter());

		// it's an authentication
		if (StringUtils.isNotBlank(clientName)) {
			 
		}

		// no or aborted authentication : go to login page
		prepareForLoginPage(context);
		return error();
	}

	/**
	 * Prepare the data for the login page.
	 *
	 * @param context
	 *            The current webflow context
	 */
	protected void prepareForLoginPage(final RequestContext context) {
		final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		final HttpServletResponse response = WebUtils.getHttpServletResponse(context);
		final HttpSession session = request.getSession();

		// web context
		final WebContext webContext = new J2EContext(request, response);

		// save parameters in web session
		final WebApplicationService service = WebUtils.getService(context);
		logger.debug("save service: {}", service);
		session.setAttribute(CasProtocolConstants.PARAMETER_SERVICE, service);
		saveRequestParameter(request, session, ThemeChangeInterceptor.DEFAULT_PARAM_NAME);
		saveRequestParameter(request, session, LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
		saveRequestParameter(request, session, CasProtocolConstants.PARAMETER_METHOD);
	}

	/**
	 * Restore an attribute in web session as an attribute in request.
	 *
	 * @param request
	 *            The HTTP request
	 * @param session
	 *            The HTTP session
	 * @param name
	 *            The name of the parameter
	 */
	private void restoreRequestAttribute(final HttpServletRequest request, final HttpSession session,
			final String name) {
		final String value = (String) session.getAttribute(name);
		request.setAttribute(name, value);
	}

	/**
	 * Save a request parameter in the web session.
	 *
	 * @param request
	 *            The HTTP request
	 * @param session
	 *            The HTTP session
	 * @param name
	 *            The name of the parameter
	 */
	private void saveRequestParameter(final HttpServletRequest request, final HttpSession session, final String name) {
		final String value = request.getParameter(name);
		if (value != null) {
			session.setAttribute(name, value);
		}
	}

	public Clients getClients() {
		return clients;
	}

	public void setClients(final Clients clients) {
		this.clients = clients;
	}

	public CentralAuthenticationService getCentralAuthenticationService() {
		return centralAuthenticationService;
	}

	public void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public AuthenticationSystemSupport getAuthenticationSystemSupport() {
		return authenticationSystemSupport;
	}
}
