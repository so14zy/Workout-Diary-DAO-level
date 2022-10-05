package basis.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import basis.util.exception.DBException;

public class DBConnector {
	
	private static Properties DBproperties;
	private static Connection connection;
	
	private DBConnector() {}
	
	static {
		
		DBproperties = new Properties();
		
		try	{
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("db.properties");
			DBproperties.load(inputStream);
			
		} catch (IOException e) {
			DBException propertiesException = new DBException("Properties file not found!", e);
			throw new RuntimeException(propertiesException);
		}
		
	}
	
	public static Connection getConnection() throws DBException {
		
		if (connection == null)	{
			
			try  {
				Class.forName(DBproperties.getProperty("class"));
				
				connection = DriverManager.getConnection(
						DBproperties.getProperty("url"),
						DBproperties.getProperty("user"),
						DBproperties.getProperty("password")
					);
				
			} catch (ClassNotFoundException e) {
				throw new DBException("DB class not found", e);
				
			} catch (SQLException e) {
				throw new DBException("Failed to connect DB", e);
			}
			
		}
		
		return connection;
	}
}
