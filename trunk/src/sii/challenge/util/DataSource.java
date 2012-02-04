package sii.challenge.util;

import java.sql.DriverManager;
import java.sql.Connection;

public class DataSource {
	private String dbUri = "jdbc:mysql://localhost:3306/sii_challenge";
	private String username = "root";
	private String password = "root";

	public Connection getConnection() throws Exception {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(dbUri, username, password);
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return connection;
	}
}
