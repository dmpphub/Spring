package com.vag.pfm.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

// TODO: Auto-generated Javadoc
/**
 * The Class DisableUrlSessionFilter.
 *
 * @author ILJA
 */
public class DisableUrlSessionFilter implements Filter {

	/** The Constant MAX_SESSION_VALIDITY. */
	private static final long MAX_SESSION_VALIDITY = 15 * 60 * 1000l;

   
    /**
     * Do filter.
     *
     * @param request the request
     * @param response the response
     * @param chain the chain
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession();

        if (httpRequest.isRequestedSessionIdFromURL()) {
            if (session != null) {
                session.invalidate();
            }
        }

        try {
            session.setMaxInactiveInterval((int) MAX_SESSION_VALIDITY);
        } catch (IllegalStateException e) {

        }

        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(
                httpResponse) {
            public String encodeRedirectUrl(String url) {
                return url;
            }

            public String encodeRedirectURL(String url) {
                return url;
            }

            public String encodeUrl(String url) {
                return url;
            }

            public String encodeURL(String url) {
                return url;
            }
        };

        chain.doFilter(request, wrappedResponse);
    }

   
    /**
     * Inits the.
     *
     * @param config the config
     * @throws ServletException the servlet exception
     */
    public void init(FilterConfig config) throws ServletException {}
   
    /**
     * Destroy.
     */
    public void destroy(){}
    
}
