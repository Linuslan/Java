package util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
	
	/**
	 * 获得数据库连接
	 * @return
	 */
	public static Connection getConn() {
		String propertiesURL = System.getProperty("user.dir").replace("\\", "/")+"/src/property/DB.properties";
		Connection conn = null;
		try {
			FileInputStream fis = new FileInputStream(propertiesURL);
			Properties prop = new Properties();
			prop.load(fis);
			Class.forName(prop.getProperty("driver"));
			String name = prop.getProperty("username");
			String password = prop.getProperty("password");
			String url = prop.getProperty("url");
			
			conn = DriverManager.getConnection(url, name, password);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return conn;
	}
}
