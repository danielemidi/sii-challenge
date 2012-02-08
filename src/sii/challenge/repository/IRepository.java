package sii.challenge.repository;

import java.sql.Connection;

public interface IRepository {

	float getSingleFloatValue(String query, int[] args) throws Exception;
	float getSingleFloatValue(String query, int[] args, Connection connection) throws Exception;
	
	float getSingleFloatValue(String query, Object[] args) throws Exception;
	float getSingleFloatValue(String query, Object[] args, Connection connection) throws Exception;

}
