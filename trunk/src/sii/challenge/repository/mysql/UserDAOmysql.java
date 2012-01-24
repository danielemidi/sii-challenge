package sii.challenge.repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import sii.challenge.repository.UserDAO;
import sii.challenge.util.DataSource;

public class UserDAOmysql implements UserDAO{
	
	private DataSource ds;
	
	public UserDAOmysql(){
		this.ds = new DataSource();
	}

	@Override
	public List<String> doRetriveFavoriteActors() {
		Connection connection=this.dataSource.getConnection();
		PreparedStatement statement =null;
		String actor;
		List<String> =new LinkedList<Fornitore>();
		ResultSet risultato=null;
		String query="select * from fornitori";

		try{
			statement=connection.prepareStatement(query);
			risultato=statement.executeQuery();

			while(risultato.next()){
				fornitore=new FornitoreProxy();
				fornitore.setId(risultato.getInt("idf"));
				fornitore.setCodice(risultato.getString("codicef"));
				fornitore.setIndirizzo(risultato.getString("indirizzof"));
				fornitore.setNome(risultato.getString("nomef"));
				fornitore.setNtelefono(risultato.getString("ntelefono"));

				.add(fornitore);
			}
		}catch(SQLException e){
			throw new PersistenceException(e.getMessage());
		}finally{
			try{
				if(statement!=null)
					statement.close();
				if(connection!=null)
					connection.close();
			}
			catch(SQLException e){
				throw new PersistenceException(e.getMessage());
			}
		}return ;
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
