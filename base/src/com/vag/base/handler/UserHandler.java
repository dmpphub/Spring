/**
 * 
 */
package com.vag.base.handler;

import java.util.Date;

import com.vag.base.dao.UserDAO;
import com.vag.base.eo.UserEO;
import com.vag.base.vo.UserVO;
import com.vag.pfm.constants.PfmConstants;
import com.vag.pfm.exception.GlobalAppException;
import com.vag.pfm.security.SaltUtils;
import com.vag.pfm.utils.DateUtils;
import com.vag.pfm.utils.SpringBeansUtils;

/**
 * @author GOBINATH A
 *
 */
public class UserHandler {

	public boolean saveUser(UserVO userVO, int userId) throws GlobalAppException {
		boolean isUserCreated = false;
		String password = PfmConstants.EMPTY_STRING;
		String encryptPassword = PfmConstants.EMPTY_STRING;
		UserDAO userDAO = null;
		UserEO userEO = null;
		try {
			userDAO = new UserDAO();
			
			if (userVO.getUserId() == 0) {
				password = userVO.getUserName().toUpperCase() + userVO.getPassword();
				encryptPassword = SaltUtils.encrypt(password);
				userVO.setPassword(encryptPassword);
				userVO.setCreatedBy(userId);
				userVO.setCreatedOn(DateUtils.convertUtilDateToTimestamp(new Date()));
			}
			userVO.setLastUpdatedBy(userId);
			userVO.setLastUpdatedOn(DateUtils.convertUtilDateToTimestamp(new Date()));
			
			userEO = (UserEO) SpringBeansUtils.populateVOToEO(userVO, new UserEO());
			
			userEO = userDAO.saveOrUpdate(userEO);
			userVO = (UserVO) SpringBeansUtils.populateEOToVO(userEO, userVO, false);
			
			if (userEO.getUserId() > 0) {
				isUserCreated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GlobalAppException(this.getClass().getName(), "saveUser", e.getMessage(), e);
		}
		return isUserCreated;
	}

}
