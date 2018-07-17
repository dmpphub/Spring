/**
 * 
 */
package com.vag.pfm.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.vag.pfm.exception.GlobalAppException;



/**
 * The Class GlobalExceptionController.
 *
 * @author  Gobinath A
 */
@ControllerAdvice
public class GlobalExceptionController {
	
	/**
	 * Handle global app exception.
	 *
	 * @param e the e
	 * @return the string
	 */
	@ExceptionHandler(value = GlobalAppException.class)
	public String handleGlobalAppException(Exception e) {
		System.out.println("Exception : "+e);
		return "GlobalAppException";
	}
	
	@ExceptionHandler(value = Exception.class)
	public String handleException(Exception e) {
		System.out.println("Exception : "+e);
		return "GlobalAppException";
	}

}
