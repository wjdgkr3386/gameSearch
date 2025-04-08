package com.example.demo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.type.TypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/steam")
@RestController
public class SteamController {

	@Autowired
	SteamService steamService;
	@Autowired
	SteamDAO steamDAO;
	
	@RequestMapping("/API.do")
	public ModelAndView steamAPI(
	) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("steamAPI.jsp");
		return mav;
	}
	
    @PostMapping("/appid-input")
    public Map<String,Object> SteamGamesIDinsert(
    ){
    	//appid 받는 방법 (약 24만개)
    	WebClient webClient = WebClient.builder()
    	            .codecs(configurer -> configurer
    	            .defaultCodecs()
    	            .maxInMemorySize(16 * 1024 * 1024))
    	.baseUrl("http://api.steampowered.com/ISteamApps/GetAppList/v2")
    	             .build();

    	Map response = webClient.get()
    	                .retrieve()
    	                .bodyToMono(Map.class)
    	                .block();
    	
    	List<Map<String,Object>> gameList = new ArrayList<Map<String,Object>>();
    	if(response!=null) {
    		Map<String,Object> applistMap = (Map<String, Object>)response.get("applist");
    		if(applistMap!=null) {
    			List<Map<String,Object>> apps = (List<Map<String,Object>>)applistMap.get("apps");
    			if(apps!=null) {
    				int appsSize = apps.size();
    				for(int i=0; i<appsSize; i++) {
    					Map<String,Object> app = (Map<String, Object>)apps.get(i);
    					if(app!=null) {
							int appid = (int)app.get("appid");
							String game_name = (String)app.get("name");
							if(game_name!=null && !game_name.isEmpty() && !game_name.equals("")) {
								if(appid>0 && appid%10==0) {
									Map<String,Object> gameMap = new HashMap<String, Object>();
									gameMap.put("appid", appid);
									gameMap.put("game_name", game_name);
									gameList.add(gameMap);
								}
							}
    						
    					}
    				}
    				if(gameList.size()>0) {
    					try {
    						steamService.insertAppid(gameList);
    					}catch(Exception e) {
    						System.out.println(e);
    					}
    				}
    			}
    		}
    	}
		return response;
	}
    
    @PostMapping("/games-input")
    public int SteamGamesinsert(
    ){
    	List<Integer> appidList = steamDAO.getAppid();
    	if(appidList!=null) {
			int size = appidList.size();
			for(int i=0; i<size; i++) {
				try {
					int appidInteger = appidList.get(i);
					int appid = appidInteger;
					
					//appid 가 10의 배수가 아니면 이상한 게임이 많아서 제외, 10의 배수여도 있긴하지만 1차 거르기
					if(appid%10!=0) {
						continue;
					}
					WebClient webClient = WebClient.builder()
			        		.codecs(configurer -> configurer
			    	                .defaultCodecs()
			    	                .maxInMemorySize(16 * 1024 * 1024))
			        	    .baseUrl("https://store.steampowered.com/api/appdetails")
			        	    .build();
			    	Map response = webClient.get()
			    	    	.uri(uriBuilder -> uriBuilder
			    	    	          .queryParam("appids", appid)
			    	    	          .queryParam("l", "ko")
			    	    	          .build())
			    	        .retrieve()
			    	        .bodyToMono(Map.class)
			    	        .block();
	
					if(response==null) {
						continue;
					}
					
		        	Map<String, Object> m = (Map<String,Object>)response.get(String.valueOf(appid));
		        	
		        	if(m==null) {
		        		continue;
		        	}
		        	
					boolean success = (boolean)m.get("success");
	
					if (!success) {
					    continue;
					}
	
					Map<String, Object> data = (Map<String, Object>) m.get("data");
					if(data==null) {
						continue;
					}
					
	
					SteamDTO steamDTO = new SteamDTO();
	
					//타입ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					String type = (String) data.get("type");
					if(type==null || !type.equals("game")) {
						continue;
					}
					
					//이름ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					String game_name = data.get("name")!=null?(String)data.get("name"):"null";
					if(game_name=="null") {
						continue;
					}
					steamDTO.setGame_name(game_name);
					
					if(steamDAO.checkAppid(appid)>0) {
						continue;
					}
					if(steamDAO.checkName(game_name)>0){
						continue;
					}
	
					String lowerName = game_name.toLowerCase();
					if(lowerName.contains("trailer")) { continue; }
					if(lowerName.contains("teaser")) { continue; }
					if(lowerName.contains("preview")) { continue; }
					if(lowerName.contains("season pass")) { continue; }
					if(lowerName.contains("dlc")) { continue; }
					if(lowerName.contains("soundtrack")) { continue; }
					if(lowerName.contains("ost")) { continue; }
					if(lowerName.contains("pack")) { continue; }
					if(lowerName.contains("artbook")) { continue; }
					if(lowerName.contains("expansion")) { continue; }
					if(lowerName.contains("bundle")) { continue; }
					if(lowerName.contains("collection")) { continue; }
					if(lowerName.contains("complete edition")) { continue; }
					if(lowerName.contains("modding")) { continue; }
					if(lowerName.contains("editor")) { continue; }
					if(lowerName.contains("wallpaper")) { continue; }
					if(lowerName.contains("benchmark")) { continue; }
					if(lowerName.contains("vr experience")) { continue; }
					if(lowerName.contains("prologue")) { continue; }
					
					//사진ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					steamDTO.setHeader_image(data.get("header_image")!=null?(String)data.get("header_image"):"null");
	
					//가격ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					int price=0;
					if((boolean)data.get("is_free")) {
						price=0;
					}else {
						Map<String, Object> pri = (Map<String, Object>)data.get("price_overview");
						if(pri==null) {
							price=0;
						}else {
							price = pri.get("initial")!=null?(int)pri.get("initial"):0;
						}
						if(price!=0) {
							price/=100;
						}
					}
					steamDTO.setPrice(price);
	
					//점수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					Map<String, Object> recommendations = (Map<String, Object>)data.get("recommendations");
					steamDTO.setRecommendations(recommendations!=null?(int)recommendations.get("total"):0);
	
					//연령등급ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					Map<String, Object> ratings = (Map<String, Object>)data.get("ratings");
					if(ratings==null) {
						continue;
					}
					Map<String, Object> dejus = (Map<String, Object>) ratings.get("dejus");
					if(dejus==null) {
						continue;
					}
					steamDTO.setRatings(dejus!=null?(String)dejus.get("rating"):"null");
	
					//카테고리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					List<Map<String, Object>> categories = (ArrayList)data.get("categories");
					if(categories==null) {
						continue;
					}
					List<String> categoriesList = new ArrayList<String>();
					for(Map<String, Object> cate : categories) {
						String description = (String)cate.get("description");
						categoriesList.add(description!=null?description:"null");
					}
					steamDTO.setCategories(categoriesList!=null?String.join(",", categoriesList):"null");
	
					//idㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					steamDTO.setAppid(appid);
	
					//release_dateidㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					Map<String,Object> release_date = (Map<String,Object>)data.get("release_date");
					if(release_date==null) {
						continue;	
					}
					String dateStr;
					dateStr = (String)release_date.get("date");
					if(dateStr==null || dateStr.equals("null") || dateStr.equals("")) {
						continue;
					}
					dateStr = Util.convertDate(dateStr);
					if(dateStr.equals("없음") || dateStr.length()!=8) {
						continue;
					}
					steamDTO.setRelease_date(dateStr);
				
					//입력ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
					steamService.insertGame(steamDTO);
				}catch(WebClientRequestException m) {
					if(m.getMessage().contains("Connection") && m.getMessage().contains("closed")) {
    					System.out.println("연결이 끝겼습니다.");
    					break;
					}
					System.out.println(m.getMessage());
				}catch(WebClientResponseException w) {
					if (w.getMessage().contains("429 Too Many Requests")) {
						i--;
						LocalDateTime now = LocalDateTime.now();
						System.out.println("요청이 너무 많습니다. 5분 쉽니다. 현재 날짜 및 시간: " +now);
    					try {
    					    Thread.sleep(300000);
    					} catch (InterruptedException ex) {
    					    Thread.currentThread().interrupt();
    					}
					}
				}catch(TypeException t) {
					System.out.println("타입처리 문제가 있습니다.");
				}catch (SQLException e) {
					System.out.println("mybatis에서 문제가 있습니다.");
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
    	}
    	return 0;
    }
    

	@RequestMapping("/game_tag")
	public int SteamGamesTag(
	) {
		System.out.println("들어옴");
		Map<String,Object> map = new HashMap<String,Object>();		
		int cnt=0;
		List<Integer> appidList = steamDAO.getGamesAppid();
		List<Map<String,Object>> genresTags = new ArrayList<Map<String,Object>>();
		if(appidList!=null) {
			WebClient webClient = WebClient.builder()
			        .codecs(configurer -> configurer
			             .defaultCodecs()
			             .maxInMemorySize(16 * 1024 * 1024))
			        .baseUrl("https://steamspy.com/api.php")
			        .build();
			for(int appid : appidList) {
				try {
					Map<String,Object> response = webClient.get()
					            .uri(uriBuilder -> uriBuilder
					               .queryParam("request", "appdetails")
					               .queryParam("appid", appid )
					               .build())
					            .retrieve()
					            .bodyToMono(Map.class)
					            .block();
					
					if(response==null) {
						continue;
					}
					
					String genres = (String) response.get("genre");
					if(genres==null || genres.isEmpty()) {
						continue;
					}
					
					genres = genres.replace(" ","");
					
					Map<String,Object> tagsMap = null;
					Object t = response.get("tags");
					if(t==null) {
						continue;
					}
					
					if(t instanceof Map) {
						tagsMap = (Map<String,Object>) t;
						if(tagsMap==null) {
							continue;
						}
						Set<String> keySet = tagsMap.keySet();
						if(keySet==null) {
							continue;
						}
						List<String> keyList = new ArrayList<>(keySet);
						String tags = String.join(",", keyList);
						
						Map<String,Object> m = new HashMap<String,Object>();
						m.put("genres", genres);
						m.put("tags", tags);
						m.put("appid", appid);
						genresTags.add(m);
					}else{
						Map<String,Object> m = new HashMap<String,Object>();
						m.put("genres", genres);
						m.put("tags", "");
						m.put("appid", appid);
						genresTags.add(m);
					}
					cnt++;
					
					if(cnt%100==0) {
						steamService.updateGenresTags(genresTags);
						genresTags.clear();
					}
				}catch(Exception e) {
					System.out.println(e);
				}
			}
		}
		System.out.println("나감");
		return 0;
	}
	
	@RequestMapping("/newGame")
	public String SteamGamesUpdate(
			int appid
	) {
		if(appid%10==0) {
			if(steamDAO.checkAppid(appid)>0) { return "게임이 이미 존재합니다"; }
			
			WebClient webClient = WebClient.builder()
	        		.codecs(configurer -> configurer
	    	                .defaultCodecs()
	    	                .maxInMemorySize(16 * 1024 * 1024))
	        	    .baseUrl("https://store.steampowered.com/api/appdetails")
	        	    .build();
	    	Map response = webClient.get()
	    	    	.uri(uriBuilder -> uriBuilder
	    	    	          .queryParam("appids", appid)
	    	    	          .queryParam("l", "ko")
	    	    	          .build())
	    	        .retrieve()
	    	        .bodyToMono(Map.class)
	    	        .block();
	
			if(response!=null) {
		    	Map<String, Object> m = (Map<String,Object>)response.get(String.valueOf(appid));
		    	if(m!=null) {
		    		boolean success = (boolean)m.get("success");
					if (success) {
						Map<String, Object> data = (Map<String, Object>) m.get("data");
						if(data!=null) {
							SteamDTO steamDTO = new SteamDTO();
							//타입ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
							String type = (String) data.get("type");
							if(type!=null && type.equals("game")) {
								//이름ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
								String game_name = data.get("name")!=null?(String)data.get("name"):"null";
								if(game_name!="null") {
									steamDTO.setGame_name(game_name);
									//사진ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
									steamDTO.setHeader_image(data.get("header_image")!=null?(String)data.get("header_image"):"null");
							
									//가격ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
									int price=0;
									if((boolean)data.get("is_free")) {
										price=0;
									}else {
										Map<String, Object> pri = (Map<String, Object>)data.get("price_overview");
										if(pri==null) {
											price=0;
										}else {
											price = pri.get("initial")!=null?(int)pri.get("initial"):0;
										}
										if(price!=0) {
											price/=100;
										}
									}
									steamDTO.setPrice(price);
							
									//점수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
									Map<String, Object> recommendations = (Map<String, Object>)data.get("recommendations");
									steamDTO.setRecommendations(recommendations!=null?(int)recommendations.get("total"):0);
							
									//연령등급ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
									Map<String, Object> ratings = (Map<String, Object>)data.get("ratings");
									if(ratings!=null) {
										Map<String, Object> dejus = (Map<String, Object>) ratings.get("dejus");
										if(dejus!=null) {
											steamDTO.setRatings(dejus!=null?(String)dejus.get("rating"):"null");
									
											//카테고리ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
											List<Map<String, Object>> categories = (ArrayList)data.get("categories");
											if(categories!=null) {
												List<String> categoriesList = new ArrayList<String>();
												for(Map<String, Object> cate : categories) {
													String description = (String)cate.get("description");
													categoriesList.add(description!=null?description:"null");
												}
												steamDTO.setCategories(categoriesList!=null?String.join(",", categoriesList):"null");
										
												//idㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
												steamDTO.setAppid(appid);
										
												//release_dateidㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
												Map<String,Object> release_date = (Map<String,Object>)data.get("release_date");
												if(release_date!=null) {
													String dateStr;
													dateStr = (String)release_date.get("date");
													if(dateStr!=null && !dateStr.equals("null") && !dateStr.equals("")) {
														dateStr = Util.convertDate(dateStr);
														if(!dateStr.equals("없음") && dateStr.length()==8) {
															steamDTO.setRelease_date(dateStr);
															
															
													    	WebClient web = WebClient.builder()
													    	        .codecs(configurer -> configurer
													    	             .defaultCodecs()
													    	             .maxInMemorySize(16 * 1024 * 1024))
													    	        .baseUrl("https://steamspy.com/api.php")
													    	        .build();
												
													    	Map res = web.get()
													    	            .uri(uriBuilder -> uriBuilder
													    	               .queryParam("request", "appdetails")
													    	               .queryParam("appid", appid )
													    	               .build())
													    	            .retrieve()
													    	            .bodyToMono(Map.class)
													    	            .block();
													    	
															if(res!=null) {
																String genres = (String) res.get("genre");
																if(genres!=null && !genres.isEmpty()) {
																	genres = genres.replace(" ","");
																	
																	Map<String,Object> tagsMap = null;
																	Object t = res.get("tags");
																	if(t!=null) {
																		System.out.println(t.getClass().getName());
																		System.out.println(t);
																		if(t instanceof Map) {
																			tagsMap = (Map<String,Object>) t;
																			if(tagsMap!=null) {
																				Set<String> keySet = tagsMap.keySet();
																				if(keySet!=null) {
																					List<String> keyList = new ArrayList<>(keySet);
																					String tags = String.join(",", keyList);
																					
																					Map<String,Object> map = new HashMap<String,Object>();
																					steamDTO.setGenres(genres);
																					steamDTO.setTags(tags);
																				}
																			}
																		}else {
																			steamDTO.setGenres(genres);
																			steamDTO.setTags("");
																		}
																		try {
																			steamService.insertNewGame(steamDTO);
																		}catch(Exception e) {
																			System.out.println(e);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
		    	}
			}
		}
		return "";
	}
}
