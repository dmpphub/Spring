package com.vag.base.web.servlet;

import java.io.File;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.util.StringUtils;

import com.vag.pfm.constants.PfmConstants;
import com.vag.pfm.utils.PropertiesUtils;

public class AppStartup extends HttpServlet {

	private static final long serialVersionUID = 7705755923888434392L;
	
	@Override
    public void init(ServletConfig conf) throws ServletException {
		String realPath = PfmConstants.EMPTY_STRING;
		try {
			realPath = conf.getServletContext().getRealPath("/");
			PfmConstants.REAL_PATH = realPath.replace('\\', '/').trim();
	        super.init(conf);
	        initConfig();
	        createDirectory();
	        //writeStdOutAndStdErr();
	        System.out.println("Welcome the Servlet...!");
	        System.out.println("Application Real Path : "+PfmConstants.REAL_PATH);
	        System.out.println("Application Install Path : "+PfmConstants.INSTALL_PATH);
	        
	        if (PfmConstants.INSTALL_PATH != null) {
	        	System.setProperty("installPathFile.Name",PfmConstants.INSTALL_PATH);
	        	//createConnectionPool();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	private void initConfig() {
		String applicationInstallPath = PfmConstants.EMPTY_STRING;
		Properties props = null;
		try {
			props = PropertiesUtils.getInstance().readPropertiesFromClassPath("/ApplicationPath.properties");
			
			if (props != null) {
				applicationInstallPath = props.getProperty("APPLICATION_INSTALL_PATH");
				
				if (StringUtils.hasLength(applicationInstallPath)) {
					PfmConstants.INSTALL_PATH = applicationInstallPath.replace('\\', '/').trim();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDirectory() {
		createIfNotExsits("logs");
		createIfNotExsits("tempDocs");
	}
	
	
	private void createIfNotExsits(String folderName) {
		File file = new File(PfmConstants.INSTALL_PATH + PfmConstants.FRONT_SLASH + folderName);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/*private void createConnectionPool() {
		Session session = null;
		Connection conn = null;
		try {
			session = HibernateUtils.getInstance().getSession();
			System.out.println("Session is Created!");
			conn = DatabaseUtils.getDataSource().getConnection();
			System.out.println("DataSource is Created!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SessionFactoryUtils.closeSession(session);
			DatabaseUtils.closeQuietly(conn);
		}
	}

	private void writeStdOutAndStdErr() throws Exception {
		StdOutErrLogger.initializeStdOutErrLog();
	}*/

}
