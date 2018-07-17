package com.vag.pfm.web.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public final class AppSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		System.out.println("Session Created");
		System.out.println("SESSION ID::::::::::::::::::"+sessionEvent.getSession().getId());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		/*SessionInfo.destroySession(sessionEvent.getSession());
        SessionInfo.getUserInfo().remove(sessionEvent.getSession().getAttribute(PfmConstants.LOGGED_USER_NAME));
        System.out.println("CURRENT SESSION IS DESTROYED");
        UserSessionUtils.updateUserSession(sessionEvent.getSession().getId());*/
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		System.out.println("Session attributeAdded");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		System.out.println("Session attributeRemoved");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		System.out.println("Session attributeReplaced");
	}
}
