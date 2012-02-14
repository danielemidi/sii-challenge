package sii.challenge.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import sii.challenge.domain.MovieRating;


public class IOFile {
		
	public static List<MovieRating> leggirigasplit(String path) throws Exception{
		
		FileReader fileDat = new FileReader(path);
		BufferedReader bf = new BufferedReader(fileDat);
		
		String line = bf.readLine();
		List<MovieRating> movieRatingList= new LinkedList<MovieRating>();
		MovieRating mr;
		int uID;
		int mID;
		long ts;
		while(line!=null){
			line = bf.readLine();
			if(line!=null){
				String s[] = line.split("\t");
				uID = Integer.parseInt(s[0]);
				mID = Integer.parseInt(s[1]);
				ts = Long.parseLong(s[3]);
				mr = new MovieRating(uID,mID,ts,0);
				movieRatingList.add(mr);
			}			
		}
		return movieRatingList;
	}
	
	public static void scriviOutputFile(List<MovieRating> movieRatings, String fileOutPut) throws Exception{
		
			  
			  File file = new File(fileOutPut);
			  boolean exist = file.createNewFile();
			  if (!exist)
			  {
				  System.out.println("File already exists.");
				  System.exit(0);
			  }
			  else
			  {
				  FileWriter fstream = new FileWriter(fileOutPut);
				  BufferedWriter out = new BufferedWriter(fstream);
				  for(MovieRating mr : movieRatings){
					  out.write(mr.getUserId()+"\t"+mr.getMovieId()+"\t"+mr.getTimestamp()+"\t"+mr.getRating()+"\n");
				  }
				  
				  out.close();
			  }
	}
	

	
	public static void main(String[] args) throws Exception{
	
		//"C:/Users/Antedesk/Desktop/Challenge/ChallengeDataset/user_ratedmovies.dat
		String fileInPut = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
		//"C:/Users/Antedesk/Desktop/fileoutpu.dat
		System.out.print("Inserire percorso di destinazione del file di output seguito dal nome: ");
		String fileOutPut = (new BufferedReader(new InputStreamReader(System.in))).readLine();
		
		List<MovieRating> list = leggirigasplit(fileInPut);
	
		scriviOutputFile(list,fileOutPut);
	
		System.out.println("Grazie per aver usato il nostro sistema di raccomandazione \n A presto.");
	}


}