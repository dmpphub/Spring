/**
 * 
 */
package com.vag.base.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vag.base.handler.UserHandler;
import com.vag.base.vo.UserVO;
import com.vag.pfm.exception.GlobalAppException;

/**
 * 
 * @author GOBINATH A
 *
 */
@Controller
@RequestMapping("/base")
public class UserController {
	
	@RequestMapping(value = "/UserForm", method = RequestMethod.GET)
	public ModelAndView userForm() {
		return new ModelAndView("UserForm").addObject(new UserVO());
	}
	
	@RequestMapping(value = "/UserController", method = RequestMethod.POST)
	public ModelAndView userAction(@ModelAttribute("userVO")UserVO userVO, 
			HttpServletRequest request, HttpServletResponse response) throws GlobalAppException {
		int userId = 1;
		ModelAndView modelAndView = null;
		UserHandler userHandler = null;
		try {
			userHandler = new UserHandler();
			
			//userId = (int) WebUtils.getSessionAttribute(request, "LOGGEDUSERID");
			
			userHandler.saveUser(userVO, userId);
			modelAndView = new ModelAndView("UserForm","userVO",userVO);
		} catch (Exception e) {
			throw e;
		}
		return modelAndView;
	}

}
