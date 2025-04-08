package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SteamServiceImpl implements SteamService{

	@Autowired
	SteamDAO steamDAO;
	
	static int cc=0;
	
	public int insertGame(SteamDTO steamDTO) {
		if(steamDTO != null) {
			if(steamDAO.insertGame(steamDTO)>0) {
				cc++;
			}
			if(cc%100==0) { System.out.println(cc+"개 저장됨"); }
		}
		return 0;
	}
	
	public int insertAppid(List<Map<String, Object>> list) {
		
		for(Map<String,Object> map : list) {
			if(steamDAO.isAppid(map)==0) {
				steamDAO.insertAppid(map);
			}
		}
		return 0;
	}
	static int aaa=0;
	public int updateGenresTags(List<Map<String, Object>> list) {
		for(Map<String,Object> map : list) {
			steamDAO.updateGenresTags(map);
			aaa++;
			if(aaa%100==0) {
				System.out.println(aaa+"개 저장");
			}
		}
		return 0;
	}
	
	public int insertNewGame(SteamDTO steamDTO) {
		steamDAO.insertNewGame(steamDTO);
		return 0;
	}
	
}
