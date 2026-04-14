package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBManager {
	
private static Connection conn = null;
	
	private DBManager() {}
	
	public static Connection getConnection() throws SQLException {
			
			if(conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection("jdbc:h2:./DetergentsShop", "sa", "");
			}
			
			return conn;
		
	}
	
	
	public static void closeConnection() throws SQLException {
		
			if(conn != null) {
				conn.close();
			}
	}

}
