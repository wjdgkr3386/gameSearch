<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SteamApi</title>
<script>

	async function appid_input() {
 		var formObj = $('[name="submitForm"]');
 		console.log("요청이 완료될때까지 기다립니다...");
		ajax(
			     "/steam/appid-input",
			     "post",
			     formObj,
			     function (response) {
			    	 console.log(response);
			     }
		);
	}
	
	async function games_input() {
 		var formObj = $('[name="submitForm"]');
 		console.log("요청이 완료될때까지 기다립니다...");
		ajax(
			     "/steam/games-input",
			     "post",
			     formObj,
			     function (response) {
			    	 console.log(response);
			     }
		);
	}
	
	async function game_tag() {
 		var formObj = $('[name="submitForm"]');
 		console.log("요청이 완료될때까지 기다립니다...");
		ajax(
			     "/steam/game_tag",
			     "post",
			     formObj,
			     function (response) {
			    	 console.log(response);
			     }
		);
	}
	
	async function game_update() {
 		var formObj = $('[name="submitForm"]');
 		console.log("요청이 완료될때까지 기다립니다...");
		ajax(
			     "/steam/newGame",
			     "post",
			     formObj,
			     function (response) {
			    	 console.log(response);
			     }
		);
	}
	
</script>
</head>
<body>
    <button onclick="appid_input()">게임ID 불러오기</button>
    <button onclick="games_input()">게임 불러오기</button>
    <button onclick="game_tag()">태그 넣기</button>
    <button onclick="game_update()">새 게임 넣기</button>
    <form name="submitForm">
    	<br>
    	<input type="text" style="width:300px;" name="appid" placeholder="새 게임 넣기 전용 : appid를 넣어주세요">
    	<input type="text" style="display: none;">
    </form>
</body>
</html>