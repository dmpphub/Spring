/** ********************************************************************************
 * @author  Gobinath A Created on Sep 24, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Sep 24, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vag.pfm.constants.PfmConstants;


/**
 * The Class SessionInfo.
 *
 * @author  Gobinath A
 */
public final class SessionInfo {

	/** The Constant sessionInfo. */
    private static final Map<String, HttpSession> sessionInfo = new HashMap<String, HttpSession>();

    /** The Constant userInfo. */
    private static final HashMap<String, String> userInfo = new HashMap<String, String>();

    /**
     * Put session.
     *
     * @param session the session
     */
    public static void putSession(HttpSession session) {
        sessionInfo.put(session.getId(), session);
    }

    /**
     * Gets the session.
     *
     * @param id the id
     * @return the session
     */
    public static HttpSession getSession(String id) {
        return sessionInfo.get(id);
    }

    /**
     * Destroy session.
     *
     * @param session the session
     */
    public static void destroySession(HttpSession session) {
        sessionInfo.remove(session.getId());
    }

    /**
     * Checks if is valid session.
     *
     * @param id the id
     * @return true, if is valid session
     */
    public static boolean isValidSession(String id) {
        return sessionInfo.containsKey(id);
    }

    /**
     * Gets the user info.
     *
     * @return the user info
     */
    public static HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    /**
     * Checks if is user already logged.
     *
     * @param userName the user name
     * @return true, if is user already logged
     */
    public static boolean isUserAlreadyLogged(String userName) {
        if (userName != null)
            return userInfo.containsKey(userName.toUpperCase());
        return false;
    }

    /**
     * Kill session.
     *
     * @param newSession the new session
     */
    public static void killAndReplaceSession(HttpSession newSession) {
        String userName = (String) newSession.getAttribute(PfmConstants.LOGGED_USER_NAME);
        String newSessionId = newSession.getId();
        if (userName != null && userInfo.containsKey(userName)) {
            String sessionId = userInfo.remove(userName);
            sessionInfo.get(sessionId).invalidate();
            userInfo.put(userName, newSessionId);
            sessionInfo.put(newSessionId, newSession);
        }
    }
}
