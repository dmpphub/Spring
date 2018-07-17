/** ********************************************************************************
 * @author  Gobinath A Created on Sep 24, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Sep 24, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.security;

import java.sql.Timestamp;

import com.vag.pfm.db.jdbc.JDBCUtils;


/**
 * The Class UserSessionUtils.
 *
 * @author  Gobinath A
 */
public final class UserSessionUtils {

	/**
	 * Update user session.
	 *
	 * @param sessionId the session id
	 */
	public static void updateUserSession(String sessionId) {
		int updatedCount = 0;
		StringBuffer updateQuery = new StringBuffer(50);
        try {
        	updateQuery.append(" UPDATE USER_SESSIONS ");
        	updateQuery.append(" SET SESSION_DESTROYED_TIME = ?, ACTIVE_FLAG = ?, ");
        	updateQuery.append(" SESSION_EXPIRED_REASON = ? WHERE SESSION_ID = ?  ");
            updatedCount =
                JDBCUtils.getInstance().update(updateQuery.toString(), new Object[] {new Timestamp(System.currentTimeMillis()), "N",
                    "SESSION_TIMED_OUT", sessionId});

            if (updatedCount > 0)
                System.out.println("UPDATED SUCCESSFULLY");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
