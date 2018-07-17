/**
 * 
 */
package com.vag.pfm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author GLOBAL\gobinath.a
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityProperty {

	/**
	 * Column name.
	 * 
	 * @return the string
	 */
	public String columnName() default "";

	/**
	 * Key name.
	 *
	 * @return the string
	 */
	public String keyName() default "";
	
	/**
     * Entity.
     * 
     * @return the string
     */
    public String entity() default "";

    /**
     * Type.
     * 
     * @return the string
     */
    public String type() default "";
}
