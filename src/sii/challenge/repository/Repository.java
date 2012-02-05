package sii.challenge.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sii.challenge.util.DataSource;

public class Repository implements IRepository {

	private DataSource dataSource;
	
	public Repository()
	{
		this.dataSource = new DataSource();
	}
	
	

	public float getSingleFloatValue(String query, int[] args) throws Exception
	{
		Connection connection = null;
		float result = 0;
		
		try {
			connection = this.dataSource.getConnection();
			result = this.getSingleFloatValue(query, args, connection);
		} catch(Exception e) {
			
		} finally {
			try {
				if (connection != null) connection.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
		
		return result;
	}
	public float getSingleFloatValue(String query, int[] args, Connection connection) throws Exception
	{
		PreparedStatement statement = null;
		ResultSet result = null;
		float res = 0;

		try {
			statement = connection.prepareStatement(query);
			for(int i = 0; i<args.length; i++) statement.setInt(i+1, args[i]);
			result = statement.executeQuery();

			while (result.next()){
				res = result.getFloat(1);
			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage());
			}
		}
		
		return res;
	}
	
	
}
