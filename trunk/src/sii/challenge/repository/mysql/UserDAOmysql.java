package sii.challenge.repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.repository.UserDAO;
import sii.challenge.util.DataSource;

public class UserDAOmysql implements UserDAO{
	
	private DataSource dataSource;
	
	public UserDAOmysql(){
		this.dataSource = new DataSource();
	}

	@Override
	public List<String> doRetriveFavoriteActors() throws Exception {
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement =null;
		String actor;
		List<String> favoriteActors =new LinkedList<String>();
		ResultSet risultato=null;
		String query="select * from fornitori";

		try{
			statement=connection.prepareStatement(query);
			risultato=statement.executeQuery();

			while(risultato.next()){
				actor = risultato.getString("");
				favoriteActors.add(actor);
			}
		}catch(SQLException e){
			throw new Exception(e.getMessage());
		}finally{
			try{
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			}
			catch(SQLException e){
				throw new Exception(e.getMessage());
			}
		}return favoriteActors;
	}

	@Override
	public List<String> doRetriveFavoriteDirectors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> doRetriveFavoriteGenres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> doRetriveFavoriteCountries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> doRetriveFavoriteYears() {
		// TODO Auto-generated method stub
		return null;
	}

}
