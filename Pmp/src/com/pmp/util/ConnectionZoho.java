package com.pmp.util;

import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;


public class ConnectionZoho {

	private static java.sql.Connection con = null;
	//private final static String user = "pesa";
	//private final static String password = "pesa";

	public static java.sql.Connection getConnection() {
		InputStream in = new Connection().getClass().getClassLoader().getResourceAsStream(IConstantAccess.CONF_FILE);
		Properties prop = new Properties();
			try {
				prop.load(in);
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password =prop.getProperty("password");
				Class.forName(prop.getProperty("driver.mysql")).newInstance();

				con = DriverManager.getConnection(url, user, password); 
				return con;
			}catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
}
