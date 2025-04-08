package com.example.demo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SteamDAO {

	int checkAppid(int appid);
	int checkName(String name);
	
	int isAppid(Map<String,Object> map);
	
	int insertGame(SteamDTO steamDTO);
	
	int insertAppid(Map<String, Object> list);
	
	List<Integer> getAppid();
	
	List<Integer> getGamesAppid();
	
	int updateGenresTags(Map<String,Object> map);
	
	int insertNewGame(SteamDTO steamDTO);
}
