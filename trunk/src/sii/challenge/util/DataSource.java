package sii.challenge.util;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
	private String dbUri= "jdbc:mysql://localhost:3306/sii_challenge";
	private String username="root";
	private String password="antedesk";

	public Connection getConnection() throws Exception{
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		      connection = DriverManager.getConnection(dbUri, username, password);
		      if(!connection.isClosed())
		          System.out.println("Successfully connected to " +
		            "MySQL server using TCP/IP...");

		      } catch(Exception e) {
		        System.err.println("Exception: " + e.getMessage());
		      
		      }
		return connection;
	}
}
