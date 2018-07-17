package com.vag.pfm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.vag.pfm.constants.*;

/**
 * The Class PropertiesUtils.
 *
 * @author GOBINATH A
 */
public class PropertiesUtils {

	/** create an object of Singleton Object. */
	private static PropertiesUtils propertiesUtils;

	/**
	 *  Create private constructor.
	 */
	private PropertiesUtils(){}

	/**
	 * * Create a static method to get instance.
	 *
	 * @return single instance of PropertiesUtils
	 */
	public static PropertiesUtils getInstance(){
		if(propertiesUtils == null){
			propertiesUtils = new PropertiesUtils();
		}
		return propertiesUtils;
	}

	/**
	 * Read properties from file path.
	 *
	 * @param propertiesFullPath the properties full path
	 * @return the properties
	 */
	public Properties readPropertiesFromFilePath(String propertiesFullPath) {
		Properties prop = null;
		InputStream input = null;
		try {
			prop = new Properties();
			input = new FileInputStream(propertiesFullPath);
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	/**
	 * Read properties from class path.
	 *
	 * @param propertiesFileName the properties file name
	 * @return the properties
	 */
	public Properties readPropertiesFromClassPath(String propertiesFileName) {
		Properties prop = null;
		InputStream input = null;
		try {
			prop = new Properties();
			input = PropertiesUtils.class.getClassLoader().getResourceAsStream(
					propertiesFileName);
			if (input == null) {
				System.out.println("Sorry, unable to find " + propertiesFileName);
			}
			prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	/**
	 * Read properties from file path.
	 *
	 * @param externalPath the external path
	 * @param propertyFileName the property file name
	 * @return the properties
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Properties readPropertiesFromFilePath(String externalPath, String propertyFileName) throws IOException {
        return readPropertiesFromFilePath(externalPath + PfmConstants.FRONT_SLASH + propertyFileName);
    }

}
