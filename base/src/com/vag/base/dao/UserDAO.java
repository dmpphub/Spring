/**
 * 
 */
package com.vag.base.dao;

import com.vag.base.eo.UserEO;
import com.vag.pfm.db.hibernate.HibernateUtils;

/**
 * @author GOBINATH A
 *
 */
public class UserDAO {

	public UserEO saveOrUpdate(UserEO userEO) throws Exception {
		return HibernateUtils.getInstance().merge(userEO);
	}

}
