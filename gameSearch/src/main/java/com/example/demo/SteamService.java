package com.example.demo;

import java.util.List;
import java.util.Map;

public interface SteamService {

	int insertGame(SteamDTO steamDTO) throws Exception;
	
	int insertAppid(List<Map<String, Object>> list) throws Exception;

	int updateGenresTags(List<Map<String, Object>> genresTags) throws Exception;
	
	int insertNewGame(SteamDTO steamDTO) throws Exception;
	
}
