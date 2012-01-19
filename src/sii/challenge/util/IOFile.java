package sii.challenge.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class IOFile {
	
	private DataSource dataSource = null;
	
	public IOFile() {
		super();
		this.dataSource = new DataSource();
	}

	public static void leggirigasplit(String path) throws Exception{
		
		
		FileReader fileDat = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileDat);
		
		String line = bf.readLine();
		
		
		int count = 1;
		while(line!=null){
			
			line = bf.readLine();
			if(line!=null){
				String s[] =line.split("\t");
				int movid = Integer.parseInt(s[0]);
				String entityID = s[1];
				String entityName = s[2];
				int rk = Integer.parseInt(s[3]);
				System.out.println(" è stata inserita nel db la riga \n"+movid +"\t"+ entityID +"\t"+ entityName +"\t"+rk);

			}
		}
	}
	
	public void insertValue(String path) throws Exception{
		Connection connection= this.dataSource.getConnection();	
		PreparedStatement statement = null;
		String insert="insert into movie_actors(idmovie_actors,movieID,actorID,actorName,ranking) " +
				"values(?,?,?,?,?)";
		try{
			FileReader fileDat = new FileReader(path);
			BufferedReader bf = new BufferedReader(fileDat);
			
			String line = bf.readLine();
	
			int count = 1;
			System.out.println(line);
			while(line!=null){
				
				line = bf.readLine();
				if(line!=null){
					
					String s[] =line.split("\t");
					int movid = Integer.parseInt(s[0]);
					String entityID = s[1];
					String entityName = s[2];
					int rk = Integer.parseInt(s[3]);
					
//					Scanner s = new Scanner(line);
//					
//					int movid = s.nextInt();
//					String entityID = s.next();
//					String entityName=""; 
//					while(!s.hasNextInt()){
//						String str = s.next();
//						entityName= entityName.concat(str+" ");	
//					}
//						
//					int r = s.nextInt();
					
					statement=connection.prepareStatement(insert);
					statement.setInt(1, count);
					statement.setInt(2, movid);
					statement.setString(3, entityID);
					statement.setString(4, entityName);
					statement.setInt(5, rk);
					statement.executeUpdate();
//					System.out.println(" è stata inserita nel db la riga \n"+movid +"\t"+ entityID +"\t"+ entityName +"\t"+r);

				}
				count++;
			}
		}catch(Exception e){
			
			throw new Exception(e.getMessage());
		
		}finally{
			try
			{
				if(statement!=null) 	statement.close();
				if(connection!=null)	connection.close();
			}
			catch(SQLException e)
			{
				throw new Exception(e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		IOFile f = new IOFile();
		f.insertValue("C:/Users/Antedesk/Desktop/Challenge/ChallengeDataset/movie_actors.dat");
		
	}


}