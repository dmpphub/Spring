/**
 * REVISION         DATE        NAME     DESCRIPTION
 * 511.101       Aug 10, 2016	GOBI      Initial Code
 */

package com.vag.pfm.db.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.vag.pfm.constants.PfmConstants;
import com.vag.pfm.utils.PropertiesUtils;


/**
 * The Class DatabaseUtils.
 *
 * @author GOBINATH A
 */
public final class DatabaseUtils implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3622180074324232605L;

	/** The Constant connectionLog. */
	static final Logger connectionLog = Logger.getLogger("connectionLogger");

	 /** The cpds. */
    private static ComboPooledDataSource cpds = null;

    static {
        cpds = createConnectionPool();
    }

	/**
	 * Creates the connection pool.
	 *
	 * @return the combo pooled data source
	 */
	private static ComboPooledDataSource createConnectionPool() {
		int minPoolSize = 5;
		int maxPoolSize = 20;
		int initialPoolSize = 10;
		ComboPooledDataSource dataSource = null;
		Properties prop = null;
		try {
			prop = readDBPoolProperties();
			dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(prop.getProperty("oracle.driver_class").trim());
            dataSource.setJdbcUrl(prop.getProperty("oracle.connection.url").trim());
            dataSource.setUser(prop.getProperty("oracle.user").trim());
            dataSource.setPassword(prop.getProperty("oracle.password").trim());
            minPoolSize = Integer.parseInt(prop.getProperty("oracle.c3p0.min_size").trim());
            maxPoolSize = Integer.parseInt(prop.getProperty("oracle.c3p0.max_size").trim());
            initialPoolSize = Integer.parseInt(prop.getProperty("oracle.c3p0.initialPoolSize").trim());
            dataSource.setMaxPoolSize(maxPoolSize);
            dataSource.setMinPoolSize(minPoolSize);
            dataSource.setInitialPoolSize(initialPoolSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	/**
	 * Gets the data source.
	 *
	 * @return the data source
	 */
	public static ComboPooledDataSource getDataSource() {
        return cpds;
    }

	 /**
 	 * Gets the connection.
 	 *
 	 * @return the connection
 	 * @throws SQLException the SQL exception
 	 */
 	public static Connection getConnection() throws SQLException {
		JDBConnection jdbcCon = new JDBConnection(cpds.getConnection());
        int hash = jdbcCon.hashCode();
        connectionLog.debug("Connection- get @" + Integer.toHexString(hash));
        return jdbcCon;
    }

	/**
	 * Close.
	 *
	 * @param con the con
	 * @throws SQLException the SQL exception
	 */
	public static void close(Connection con) throws SQLException {
        if (con != null && !con.isClosed()) {
            int hash = con.hashCode();
            con.close();
            connectionLog.debug("Connection- close @" + Integer.toHexString(hash));
        }
	}
	
	/**
	 * Close quietly.
	 *
	 * @param con the con
	 */
	public static void closeQuietly(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
	            int hash = con.hashCode();
	            con.close();
	            connectionLog.debug("Connection- close @" + Integer.toHexString(hash));
	        }
		} catch (Exception e) {
		}
	}

	 /**
 	 * Close.
 	 *
 	 * @param con the con
 	 * @param stmt the stmt
 	 * @param rs the rs
 	 * @throws SQLException the SQL exception
 	 */
 	public static void close(Connection con, Statement stmt, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
            int hash = rs.hashCode();
            connectionLog.debug("\"ResultSet- close @" + Integer.toHexString(hash));
        }

        if (stmt != null) {
            stmt.close();
            int hash = stmt.hashCode();
            connectionLog.debug("\"Statement- close @" + Integer.toHexString(hash));
        }

        if (con != null && !con.isClosed()) {
            con.close();
            int hash = con.hashCode();
            connectionLog.debug("\"Connection- close @" + Integer.toHexString(hash));
        }
    }
 	
 	/**
	  * Close quietly.
	  *
	  * @param con the con
	  * @param stmt the stmt
	  * @param rs the rs
	  */
	 public static void closeQuietly(Connection con, Statement stmt, ResultSet rs) {
 		try {
 			if (rs != null) {
 	            rs.close();
 	            int hash = rs.hashCode();
 	            connectionLog.debug("\"ResultSet- close @" + Integer.toHexString(hash));
 	        }

 	        if (stmt != null) {
 	            stmt.close();
 	            int hash = stmt.hashCode();
 	            connectionLog.debug("\"Statement- close @" + Integer.toHexString(hash));
 	        }

 	        if (con != null && !con.isClosed()) {
 	            con.close();
 	            int hash = con.hashCode();
 	            connectionLog.debug("\"Connection- close @" + Integer.toHexString(hash));
 	        }
 		} catch (Exception e) {
 		}
    }

 	/**
	  * Close.
	  *
	  * @param con the con
	  * @param stmt the stmt
	  * @param rs the rs
	  * @throws SQLException the SQL exception
	  */
	 public static void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
            int hash = rs.hashCode();
            connectionLog.debug("\"ResultSet- close @" + Integer.toHexString(hash));
        }

        if (stmt != null) {
            stmt.close();
            int hash = stmt.hashCode();
            connectionLog.debug("\"Statement- close @" + Integer.toHexString(hash));
        }

        if (con != null && !con.isClosed()) {
            con.close();
            int hash = con.hashCode();
            connectionLog.debug("\"Connection- close @" + Integer.toHexString(hash));
        }
    }
	 
	 /**
 	 * Close quietly.
 	 *
 	 * @param con the con
 	 * @param stmt the stmt
 	 * @param rs the rs
 	 */
 	public static void closeQuietly(Connection con, PreparedStatement stmt, ResultSet rs) {
 		try {
 			if (rs != null) {
 	            rs.close();
 	            int hash = rs.hashCode();
 	            connectionLog.debug("\"ResultSet- close @" + Integer.toHexString(hash));
 	        }

 	        if (stmt != null) {
 	            stmt.close();
 	            int hash = stmt.hashCode();
 	            connectionLog.debug("\"Statement- close @" + Integer.toHexString(hash));
 	        }

 	        if (con != null && !con.isClosed()) {
 	            con.close();
 	            int hash = con.hashCode();
 	            connectionLog.debug("\"Connection- close @" + Integer.toHexString(hash));
 	        }
 		} catch (Exception e) {
 		}
    }

	/**
	 * Read db pool properties.
	 *
	 * @return the properties
	 */
	private static Properties readDBPoolProperties() {
    	PropertiesUtils props = null;
    	Properties dbprops = null;
    	try {
    		props = PropertiesUtils.getInstance();
    		dbprops = props.readPropertiesFromFilePath(PfmConstants.INSTALL_PATH + PfmConstants.DATABASE_POOL);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return dbprops;
    }

}
