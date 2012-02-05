package sii.challenge.repository;

import java.sql.Connection;

public interface IRepository {

	float getSingleFloatValue(String query, int[] args) throws Exception;
	float getSingleFloatValue(String query, int[] is, Connection connection) throws Exception;

}
