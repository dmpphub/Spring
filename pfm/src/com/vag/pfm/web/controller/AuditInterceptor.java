/**
 * 
 */
package com.vag.pfm.web.controller;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import com.vag.pfm.annotation.AppicationAudit;


/**
 * @author  Gobinath A
 *
 */
public class AuditInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1,
			Object handler) throws Exception {
		HandlerMethod hm = (HandlerMethod)handler; 
		Method method = hm.getMethod(); 
		if(method.getDeclaringClass().isAnnotationPresent(Controller.class)) {
			if(method.isAnnotationPresent(AppicationAudit.class)) {
				System.out.println("AuditInterceptor : "+ method.getAnnotation(AppicationAudit.class).value());
				request.setAttribute("STARTTIME",System.currentTimeMillis());
			}
		} 
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse arg1,
			Object handler, ModelAndView arg3) throws Exception {
		HandlerMethod hm = (HandlerMethod)handler;
		Method method = hm.getMethod();
		if(method.getDeclaringClass().isAnnotationPresent(Controller.class)) {
			if(method.isAnnotationPresent(AppicationAudit.class)) { 
				System.out.println("AuditInterceptor : "+method.getAnnotation(AppicationAudit.class).value()); 
				request.setAttribute("ENDTIME",System.currentTimeMillis());
			}
		 }
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse arg1, Object handler, Exception arg3)
			throws Exception {
		HandlerMethod hm = (HandlerMethod)handler;
		Method method = hm.getMethod();
		if(method.isAnnotationPresent(AppicationAudit.class)) {
			System.out.println("AuditInterceptor : "+method.getAnnotation(AppicationAudit.class).value());
			long timeMillis = ((Long)request.getAttribute("ENDTIME")-(Long)request.getAttribute("STARTTIME"));
			System.out.println("Total Took:"+TimeUnit.MILLISECONDS.toSeconds(timeMillis) +" Seconds");
		}  
	}
	
}
