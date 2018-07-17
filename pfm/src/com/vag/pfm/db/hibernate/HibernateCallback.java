/**
 * REVISION         DATE        NAME     DESCRIPTION
 * 511.101       Aug 12, 2016	GOBI      Initial Code
 */

package com.vag.pfm.db.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * The Interface HibernateCallback.
 *
 * @author GOBINATH A
 */
public interface HibernateCallback {

	/**
	 * Execute.
	 * @param <T>
	 *
	 * @param session the session
	 * @return the object
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	Object execute(Session session) throws HibernateException, SQLException;

}
