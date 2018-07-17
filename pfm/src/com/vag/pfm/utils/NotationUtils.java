/** ********************************************************************************
 * @author  Gobinath A Created on Aug 29, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Aug 29, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class NotationUtils.
 *
 * @author  Gobinath A
 */
public final class NotationUtils {
	
	/**
	 * Capitalize first letter.
	 *
	 * @param propertyName the property name
	 * @return the string
	 */
	public static String capitalizeFirstLetter(String propertyName) {
		if(StringUtils.isNotEmpty(propertyName)) {
			propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		}
		return propertyName;
	}
	
	/**
	 * Gets the ter method.
	 *
	 * @param propertyName the property name
	 * @return the ter method
	 */
	public static String getterMethod(String propertyName) {
		return "get" + capitalizeFirstLetter(propertyName);
	}
	
	/**
	 * Setter method.
	 *
	 * @param propertyName the property name
	 * @return the string
	 */
	public static String setterMethod(String propertyName) {
		return "set" + capitalizeFirstLetter(propertyName);
	}

}
