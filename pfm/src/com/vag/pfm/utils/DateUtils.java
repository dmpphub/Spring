
package com.vag.pfm.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vag.pfm.constants.*;

/**
 * The Class DateUtils.
 *
 * @author  Gobinath A
 */
public class DateUtils {
	
	/** The Constant DD_MM_YYYY. */
    public static final String DD_MM_YYYY = "dd-MM-yyyy";

    /** The Constant DD_MON_YY. */
    public static final String DD_MON_YY = "dd-MMM-yy";

    /** The Constant DD_MM_YY. */
    public static final String DD_MM_YY = "dd-MM-yy";
	
	/** The Constant DD_MON_YYYY. */
	public static final String DD_MON_YYYY = "dd-MMM-yyyy";
	/** The Constant DD_MON_YYYY_24HH_MM_SS. */
    public static final String DD_MON_YYYY_24HH_MM_SS = "dd-MMM-yyyy HH:mm:ss";

    /** The Constant DD_MON_YYYY_HH_MM_SS. */
    public static final String DD_MON_YYYY_HH_MM_SS = "dd-MMM-yyyy hh:mm:ss";
	
	/**
	 * Convert util date to string.
	 *
	 * @param utilDate the util date
	 * @return the string
	 */
	public static String convertUtilDateToString(Date utilDate) {
		return convertUtilDateToString(utilDate, DateUtils.DD_MON_YYYY);
	}

	/**
	 * Convert util date to string.
	 *
	 * @param utilDate the util date
	 * @param format the format
	 * @return the string
	 */
	public static String convertUtilDateToString(Date utilDate, String format) {
		String strUtilDate = PfmConstants.EMPTY_STRING;
		SimpleDateFormat formatter = null;
		try {
			if (utilDate != null) {
				formatter = new SimpleDateFormat(format);
				strUtilDate = formatter.format(utilDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strUtilDate;
	}
	
	/**
	 * Convert string to util date.
	 *
	 * @param strDate the str date
	 * @return the date
	 */
	public static Date convertStringToUtilDate(String strDate) {
		return convertStringToUtilDate(strDate, DateUtils.DD_MON_YYYY);
	}
	
	/**
	 * Convert string to util date.
	 *
	 * @param strDate the str date
	 * @param format the format
	 * @return the date
	 */
	public static Date convertStringToUtilDate(String strDate, String format) {
		Date utilDate = null;
		SimpleDateFormat formatter = null;
		try {
			if (strDate != null && !strDate.isEmpty()) {
				formatter = new SimpleDateFormat(format);
				utilDate = formatter.parse(strDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return utilDate;
	}
	
	/**
	 * Convert util to sql date.
	 *
	 * @param utilDate the util date
	 * @return the java.sql. date
	 */
	public static java.sql.Date convertUtilToSqlDate(Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}

	/**
	 * Convert sql to util date.
	 *
	 * @param sqlDate the sql date
	 * @return the date
	 */
	public static Date convertSqlToUtilDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());
	}
	
	/**
	 * Convert sql to string date.
	 *
	 * @param sqlDate the sql date
	 * @param format the format
	 * @return the string
	 */
	public static String convertSqlToStringDate(java.sql.Date sqlDate, String format) {
		return convertUtilDateToString(convertSqlToUtilDate(sqlDate), format);
	}
	
	/**
	 * Convert string to sql date.
	 *
	 * @param strSqlDate the str sql date
	 * @param format the format
	 * @return the java.sql. date
	 */
	public static java.sql.Date convertStringToSqlDate(String strSqlDate, String format) {
		return convertUtilToSqlDate(convertStringToUtilDate(strSqlDate, format));
	}
	
	/**
	 * Convert util date to timestamp.
	 *
	 * @param utilDate the util date
	 * @return the timestamp
	 */
	public static Timestamp convertUtilDateToTimestamp(Date utilDate) {
		Timestamp timestamp = null;
        if (utilDate != null) {
        	timestamp = new Timestamp(utilDate.getTime());
        }
        return timestamp;
    }
	
	/**
	 * Gets the current date as sql.
	 *
	 * @return the current date as sql
	 */
	public static java.sql.Date getCurrentDateAsSql() {
        return new java.sql.Date(System.currentTimeMillis());
    }
	
	/**
	 * Gets the current date as string.
	 *
	 * @param dateFormat the date format
	 * @return the current date as string
	 */
	public static String getCurrentDateAsString(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(new Date());
    }
	
	/**
	 * Convert string date to timestamp.
	 *
	 * @param dateString the date string
	 * @return the timestamp
	 */
	public static Timestamp convertStringDateToTimestamp(String dateString) {
        return new Timestamp(convertStringToSqlDate(dateString, DateUtils.DD_MON_YYYY).getTime());
    }
	
	/**
	 * Convert string timestamp to timestamp.
	 *
	 * @param dateString the date string
	 * @return the timestamp
	 */
	public static Timestamp convertStringTimestampToTimestamp(String dateString) {
        return new Timestamp(convertStringToSqlDate(dateString, DateUtils.DD_MON_YYYY_HH_MM_SS).getTime());
    }

	/**
	 * Convert timestamp to string.
	 *
	 * @param timestamp the timestamp
	 * @param format the format
	 * @return the string
	 */
	public static String convertTimestampToString(Timestamp timestamp,
			String format) {
		String strDate = PfmConstants.EMPTY_STRING;
		SimpleDateFormat sdf = null;
		try {
			if (timestamp != null) {
				sdf = new SimpleDateFormat(format);
				strDate = sdf.format(timestamp);
			}
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate.toUpperCase();
	}

}
