/**
 * REVISION         DATE        NAME     DESCRIPTION
 * 511.101       Aug 7, 2016	GOBI      Initial Code
 */

package com.vag.pfm.db.hibernate;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.vag.pfm.constants.PfmConstants;
import com.vag.pfm.utils.PropertiesUtils;




/**
 * The Class HibernateUtils.
 *
 * @author GOBINATH A
 */
public class HSessionFactory {

	/** The Constant sessionFactory. */
	private static final SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * Builds the session factory.
	 *
	 * @return the session factory
	 */
	private static SessionFactory buildSessionFactory() {
		Configuration configuration = null;
		Properties prop = null;
		Properties dbprops = null;
		StandardServiceRegistryBuilder serviceRegistryBuilder = null;
		ServiceRegistry serviceRegistry = null;
        try {
            prop = new Properties();
            configuration = new Configuration();
            serviceRegistryBuilder = new StandardServiceRegistryBuilder();

            dbprops = readDBPoolProperties();
            prop.setProperty("hibernate.connection.driver_class", (String) dbprops.get("oracle.driver_class"));
            prop.setProperty("hibernate.connection.url", (String) dbprops.get("oracle.connection.url"));
            prop.setProperty("hibernate.connection.username", (String) dbprops.get("oracle.user"));
            prop.setProperty("hibernate.connection.password", (String) dbprops.get("oracle.password"));
            prop.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
            prop.setProperty("hibernate.c3p0.min_size", (String) dbprops.get("oracle.c3p0.min_size"));
            prop.setProperty("hibernate.c3p0.max_size", (String) dbprops.get("oracle.c3p0.max_size"));
            prop.setProperty("hibernate.current_session_context_class", "thread");
            prop.setProperty("hibernate.bytecode.provider", "cglib");
            prop.setProperty("hibernate.show_sql", "false");
            prop.setProperty("hibernate.id.new_generator_mappings", "true");

            configuration.setProperties(prop);
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            serviceRegistry = serviceRegistryBuilder.configure().build();
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

	/**
	 * Rebuild session factory.
	 */
	public static void rebuildSessionFactory() {
        buildSessionFactory();
    }

    /**
     * Gets the session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shutdown.
     */
    public static void shutdown() {
        getSessionFactory().close();
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
