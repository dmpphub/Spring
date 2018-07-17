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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import org.springframework.util.ReflectionUtils;

import com.vag.pfm.constants.PfmConstants;



/**
 * The Class SpringReflectionUtils.
 *
 * @author Gobinath A
 */
public final class SpringReflectionUtils {

	/**
	 * Gets the property.
	 *
	 * @param valueObject the value object
	 * @param propertyName the property name
	 * @return the property
	 * @throws Exception the exception
	 */
	public static Object getProperty(final Object valueObject,
			final String propertyName) throws Exception {
		Class<?> objClass = valueObject.getClass();
		Field field = objClass.getDeclaredField(propertyName);
		return getProperty(valueObject, field);
	}

	/**
	 * Gets the property.
	 *
	 * @param valueObject the value object
	 * @param field the field
	 * @return the property
	 * @throws Exception the exception
	 */
	public static Object getProperty(final Object valueObject, final Field field)
			throws Exception {
		ReflectionUtils.makeAccessible(field);
		return ReflectionUtils.getField(field, valueObject);
	}

	/**
	 * Sets the property.
	 *
	 * @param valueObject the value object
	 * @param propertyName the property name
	 * @param value the value
	 * @throws Exception the exception
	 */
	public static void setProperty(final Object valueObject,
			final String propertyName, Object value)
			throws Exception {
		try {
			Class<?> objClass = valueObject.getClass();
			Field field = objClass.getDeclaredField(propertyName);
			setProperty(valueObject, field, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Sets the property.
	 *
	 * @param valueObject the value object
	 * @param field the field
	 * @param value the value
	 * @throws Exception the exception
	 */
	public static void setProperty(final Object valueObject, final Field field,
			final Object value) throws Exception {
		try {
			Class<?> paramType = field.getType();
			Object typeValue = getValueAsParamType(value, paramType);
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, valueObject, typeValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Execute getter method.
	 *
	 * @param valueObject the value object
	 * @param field the field
	 * @return the object
	 * @throws Exception the exception
	 */
	public static Object executeGetterMethod(final Object valueObject,
			final Field field) throws Exception {
		Method method = ReflectionUtils.findMethod(valueObject.getClass(),
				NotationUtils.getterMethod(field.getName()));
		return executeGetMethod(valueObject, method);
	}

	/**
	 * Execute getter method.
	 *
	 * @param valueObject the value object
	 * @param propertyName the property name
	 * @return the object
	 * @throws Exception the exception
	 */
	public static Object executeGetterMethod(final Object valueObject,
			final String propertyName) throws Exception {
		Method method = ReflectionUtils.findMethod(valueObject.getClass(),
				NotationUtils.getterMethod(propertyName));
		if (method != null)
			return executeGetMethod(valueObject, method);
		else
			return null;
	}

	/**
	 * Execute setter method.
	 *
	 * @param valueObject the value object
	 * @param field the field
	 * @param value the value
	 * @return the object
	 * @throws Exception the exception
	 */
	public static Object executeSetterMethod(final Object valueObject,
			final Field field, final Object value)
			throws Exception {
		Method method = ReflectionUtils.findMethod(valueObject.getClass(),
				NotationUtils.setterMethod(field.getName()),
				new Class<?>[] { field.getType() });
		if (method != null)
			return executeSetMethod(valueObject, method, value);
		else
			return null;
	}

	/**
	 * Execute setter method.
	 *
	 * @param valueObject the value object
	 * @param propertyName the property name
	 * @param value the value
	 * @return the object
	 * @throws Exception the exception
	 */
	public static Object executeSetterMethod(final Object valueObject,
			final String propertyName, final Object value)
			throws Exception {
		Object obj = null;
		try {
			Field field = valueObject.getClass().getDeclaredField(propertyName);
			Method method = ReflectionUtils.findMethod(valueObject.getClass(),
					NotationUtils.setterMethod(propertyName),
					new Class<?>[] { field.getType() });
			obj = executeSetMethod(valueObject, method, value);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return obj;
	}

	/**
	 * Execute method.
	 *
	 * @param valueObject the value object
	 * @param method the method
	 * @param value the value
	 * @return the object
	 */
	public static Object executeSetMethod(final Object valueObject,
			final Method method, final Object value) {
		return ReflectionUtils.invokeMethod(method, valueObject, new Object[]{value});
	}
	
	/**
	 * Execute get method.
	 *
	 * @param valueObject the value object
	 * @param method the method
	 * @return the object
	 */
	public static Object executeGetMethod(final Object valueObject,
			final Method method) {
		return ReflectionUtils.invokeMethod(method, valueObject);
	}


	/**
	 * Gets the value as param type.
	 *
	 * @param value the value
	 * @param paramType the param type
	 * @return the value as param type
	 */
	private static Object getValueAsParamType(final Object value,
			final Class<?> paramType) {
		if (value != null && value instanceof BigDecimal) {
			if (paramType == int.class)
				return ((BigDecimal) value).intValueExact();
			else if (paramType == float.class)
				return ((BigDecimal) value).floatValue();
			else if (paramType == double.class)
				return ((BigDecimal) value).doubleValue();
			else if (paramType == long.class)
				return ((BigDecimal) value).longValueExact();
		}
		return value;
	}
	
	public static void showType(String className) throws Exception {
		String flavor = PfmConstants.EMPTY_STRING;
		Class<?> thisClass = null;
		Class<?> parent = null;
		Class<?>[] interfaces = null;
		try {
			thisClass = Class.forName(className);
			if (thisClass != null) {
				flavor = thisClass.isInterface() ? "interface" : "class";
				System.out.println(flavor + " " + className);
				parent = thisClass.getSuperclass();
				if (parent != null) {
					System.out.println("extends " + parent.getName());
				}
				interfaces = thisClass.getInterfaces();
				for (Class<?> interfacz : interfaces) {
					System.out.println("implements " + interfacz.getName());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void showAllMethods(String className) throws Exception {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
			showAllMethods(clazz.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void showAllMethods(Object object) throws Exception {
		Class<?> clazz = null;
		Method[] methods = null;
		Class<?>[] parameterTypes = null;
		try {
			clazz = object.getClass();
			if (clazz != null) {
				methods = clazz.getMethods();
				for (Method method : methods) {
					System.out.println("Method Name : "+method.getName());
					System.out.println("Return Type : "+method.getReturnType().getName());
					parameterTypes = method.getParameterTypes();
					System.out.println("Parameter Types : ");
					for (Class<?> parameterType : parameterTypes) {
						System.out.println(parameterType.getName());
					}
					System.out.println("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void showAllFields(String className) throws Exception {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
			showAllFields(clazz.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void showAllFields(Object object) throws Exception {
		Class<?> clazz = null;
		Field[] fields = null;
		try {
			clazz = object.getClass();
			if (clazz != null) {
				fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					if (!Modifier.isPublic(field.getModifiers())) {
						field.setAccessible(true);
					}
					System.out.println("Field Name : "+field.getName());
					System.out.println("Field Type : "+field.getType());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}