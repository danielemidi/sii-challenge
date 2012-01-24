package sii.challenge.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
<<<<<<< .mine
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
=======
>>>>>>> .r14

import sii.challenge.domain.MovieRating;


public class IOFile {
		
	public List<MovieRating> leggirigasplit(String path) throws Exception{
		
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
				ts = Long.parseLong(s[2]);
				mr = new MovieRating(uID,mID,ts,0);
				movieRatingList.add(mr);
			}			
		}
		return movieRatingList;
	}
	
	public static void scriviOut(List<MovieRating> movieRatings) throws Exception{
		
		
		String line = bf.readLine();
		List<MovieRating> lineList= new LinkedList<MovieRating>();
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
				ts = Long.parseLong(s[2]);
				mr = new MovieRating(uID,mID,ts,0);
				lineList.add(mr);
			}			
		}
	}

	
	public static void main(String[] args) throws Exception{
		leggirigasplit("C:/Users/Antedesk/Desktop/Challenge/ChallengeDataset/user_ratedmovies.dat");
		System.out.println("done");
	}


}