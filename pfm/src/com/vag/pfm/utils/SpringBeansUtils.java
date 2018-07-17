/** ********************************************************************************
 * @author  Gobinath A Created on Sep 1, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Sep 1, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/

package com.vag.pfm.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

import com.vag.pfm.annotation.EntityProperty;
import com.vag.pfm.constants.PfmConstants;



/**
 * The Class SpringBeansUtils.
 *
 * @author  Gobinath A
 */
public class SpringBeansUtils {

	/**
	 * Gets the single instance of SpringBeansUtils.
	 *
	 * @param definitionFile the definition file
	 * @param beanId the bean id
	 * @return single instance of SpringBeansUtils
	 */
	public static Object getInstance(String definitionFile, String beanId) {
		Object obj = null;
		BeanFactory beanFactory = null;
		XmlBeanDefinitionReader reader = null;
		beanFactory = new DefaultListableBeanFactory();
		reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)beanFactory);
		reader.loadBeanDefinitions(new FileSystemResource(definitionFile));
		obj = beanFactory.getBean(beanId);
		return obj;
	}
	
	/**
	 * Gets the single instance of SpringBeansUtils.
	 *
	 * @param beanId the bean id
	 * @return single instance of SpringBeansUtils
	 */
	public static Object getInstance(String beanId) {
		Object obj = null;
		ApplicationContext ctx = null;
		try {
			ctx = new FileSystemXmlApplicationContext("file:"+ PfmConstants.INSTALL_PATH + PfmConstants.ALL_BEANS);
			if (ctx != null) {
				obj = ctx.getBean(beanId);
				((FileSystemXmlApplicationContext)ctx).close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ctx != null) {
				((FileSystemXmlApplicationContext)ctx).close();
			}
		}
		return obj;
	}

	/**
	 * Populate vo to eo.
	 *
	 * @param valueObject the value object
	 * @param entityObject the entity object
	 * @return the object
	 */
	public static Object populateVOToEO(Object valueObject, Object entityObject) {
		String columnName = PfmConstants.EMPTY_STRING;
		Field[] fieldArray = null;
		EntityProperty entityProperty = null;
		Object value = null;
		try {
			fieldArray = valueObject.getClass().getDeclaredFields();
			if (fieldArray != null && fieldArray.length > 0) {
				for (Field field : fieldArray) {
					if (field.isAnnotationPresent(EntityProperty.class)) {
						entityProperty = field.getAnnotation(EntityProperty.class);
						columnName = entityProperty.columnName().trim();
						if(StringUtils.isNotEmpty(columnName)) {
							value = SpringReflectionUtils.executeGetterMethod(valueObject, columnName);
							if (value != null) {
								SpringReflectionUtils.executeSetterMethod(entityObject, columnName, value);
							}
							value = null;
						}
	        		}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entityObject;
	}

	/**
	 * Populate eo to vo.
	 *
	 * @param entityObject the entity object
	 * @param valueObject the value object
	 * @param needNewInstance the need new instance
	 * @return the object
	 */
	public static Object populateEOToVO(Object entityObject, Object valueObject, boolean needNewInstance) {
		String columnName = PfmConstants.EMPTY_STRING;
		Field[] fieldArray = null;
		EntityProperty entityProperty = null;
		Object value = null;
		Object newValueObject = null;
		try {
			fieldArray = valueObject.getClass().getDeclaredFields();
			
			if (needNewInstance) {
				newValueObject = valueObject.getClass().newInstance();
			} else {
				newValueObject = valueObject;
			}
			
			if (fieldArray != null && fieldArray.length > 0) {
				for (Field field : fieldArray) {
					if (field.isAnnotationPresent(EntityProperty.class)) {
						entityProperty = field.getAnnotation(EntityProperty.class);
						columnName = entityProperty.columnName().trim();
						if(StringUtils.isNotEmpty(columnName)) {
							value = SpringReflectionUtils.executeGetterMethod(entityObject, columnName);
							if (value != null) {
								SpringReflectionUtils.executeSetterMethod(newValueObject, columnName, value);
							}
							value = null;
						}
	        		}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newValueObject;
	}
}
