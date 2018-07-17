/** ********************************************************************************
 * @author  Gobinath A Created on Nov 20, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Nov 20, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * The Interface AppicationAudit.
 *
 * @author  Gobinath A
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppicationAudit{
	
	/**
	 * Value.
	 *
	 * @return the string
	 */
	String value();
}
