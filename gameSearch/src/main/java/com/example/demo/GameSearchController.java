package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GameSearchController {

	@Autowired
	GameSearchDAO GameSearchDAO;
	
	@RequestMapping( value="/gameHub.do")
	public ModelAndView gameHub(
	) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("gameHub.jsp");
		return mav;
	}
	
	@PostMapping( value="/search.do")
	public ModelAndView search(
			GameSearchDTO gameSearchDTO
	) {
		ModelAndView mav = new ModelAndView();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
		int searchCount=0;
		
		//검색 결과 갯수
		int searchResultCount = GameSearchDAO.getSearchResultCount(gameSearchDTO);
		if(searchResultCount!=0) {
			Util.searchUtil(searchResultCount, gameSearchDTO);
			searchList = GameSearchDAO.search(gameSearchDTO);
			searchCount = searchList.size();
		}
		
		searchMap.put("searchList", searchList);
		searchMap.put("searchCount", searchCount);
		searchMap.put("searchResultCount", searchResultCount);
		searchMap.put("keyword", gameSearchDTO.getKeyword());
		searchMap.put("last_pageNo", gameSearchDTO.getLast_pageNo());
		searchMap.put("selectPageNo", gameSearchDTO.getSelectPageNo());
		searchMap.put("begin_pageNo", gameSearchDTO.getBegin_pageNo());
		searchMap.put("end_pageNo", gameSearchDTO.getEnd_pageNo());
		searchMap.put("rowCnt", gameSearchDTO.getRowCnt());
		searchMap.put("begin_rowNo", gameSearchDTO.getBegin_rowNo());
		searchMap.put("end_rowNo", gameSearchDTO.getEnd_rowNo());
		
		mav.addObject("searchMap", searchMap);
		mav.setViewName("gameHub.jsp");
		return mav;
	}
	
	
}
