package com.example.demo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameSearchDAO {
	int getSearchResultCount(GameSearchDTO gameSearchDTO);
	
	List<Map<String, Object>> search(GameSearchDTO gameSearchDTO);
}
