<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게임 추천</title>
<style>
	body {
	    margin: 0;
	    padding: 0;
	    overflow-x: hidden;
	    background-color:#f8f8f8;
	}
	select{
		float: left;
		width: 130px;
		height: 40px;
		font-size: 15px;
		margin:10 10;
	}
	td{ 
		position: relative;
		text-align: center;
		height: 150px;
		width: 360px;
		box-sizing: border-box;
    	cursor: pointer;
	}
	td:hover{
		transform: scale(1.1); /* 이미지 10% 확대 */
	}
	img{
		max-width:100%;
    	object-fit: contain; /* 비율 유지하며 td 안에 맞춤 */
    	user-select: none;
	}
	.gameName-span{
		display: flex;
		justify-content: space-between; /* 내부 요소를 좌우 배치 */
		position: absolute;
		bottom: 0; /* td의 맨 아래 배치 */
		width: 100%;
		font-size: 16px;
		color: white;
		background-color:black;
	}
	.search-container {
		margin-top: 100px;
		width: 800px;
		height: 50px;
		display: flex;
		gap: 10px; /* 서치바와 버튼 사이의 간격 */
	}
	.searchBar{
		width: 100%;
		height: 50px;
        padding: 10px;
        font-size: 18px;
        border-radius: 10px;
	}
	.searchButton{
		width: 70px;
		height: 50px;
		background-color: tomato;
		display: flex;
		justify-content: center;
		align-items: center;
        border: none;
        border-radius: 10px;
		cursor: pointer; /* 마우스 커서 변경 */
	}
	.searchFiltersBox{
		box-sizing: border-box;
		width: 800px;
		display: inline-block;
	}
	.searchResult-div{
		width: 1600px;
	}
	.gameTable{
		width: 100%;
		table-layout: fixed;
		display: grid;
		box-sizing: border-box;
		border-spacing: 30 100px; /* 행 사이 간격 조절 */
		padding: 10px;
	}
</style>
<script>
	$(function(){init();});
	function init(){
		//엔터를 눌렀을때 폼 제출 방지
		$("[name='submitForm'] input[name='keyword']").on('keydown', function(event) {
		    if (event.key === 'Enter') {
		        //폼 제출 방지
		        event.preventDefault();
		        search(0);
		    }
		});
	}

	function search(isPageClick){
		if(isPageClick===1){
			if(parseInt($(".last_pageNoHistory").text()) < $("[name='selectPageNo']").val()){
				$("[name='selectPageNo']").val(parseInt($(".last_pageNoHistory").text()));
				return;
			}
			$("[name='keyword']").val($("[name='keywordHistory']").val());
			$("[name='ratings']").val($("[name='ratingsHistory']").val());
			$("[name='categories']").val($("[name='categoriesHistory']").val());
			$("[name='genres']").val($("[name='genresHistory']").val());
			$("[name='tags']").val($("[name='tagsHistory']").val());
			$("[name='sort']").val($("[name='sortHistory']").val());
		}else{
			$("[name='selectPageNo']").val(1);
			$("[name='keywordHistory']").val($("[name='keyword']").val());
			$("[name='ratingsHistory']").val($("[name='ratings']").val());
			$("[name='categoriesHistory']").val($("[name='categories']").val());
			$("[name='genresHistory']").val($("[name='genres']").val());
			$("[name='tagsHistory']").val($("[name='tags']").val());
			$("[name='sortHistory']").val($("[name='sort']").val());
		}
		
		var formObj = $("[name='submitForm']");
		ajax(
		     "/search.do",
		     "post",
		     formObj,
		     function (responseHtml) {
                 var obj = $(responseHtml);
                 $(".searchResult-div").html(obj.find(".searchResult-div").html());
		     }
		);
	}
	
    function pageNoClick(clickPageNo) {
        $("[name='selectPageNo']").val(clickPageNo);
        search(1);
    }
    
    function popup(HEADER_IMAGE, GAME_NAME, RECOMMENDATIONS, PRICE, RELEASE_DATE, RATINGS){
    	var width = 800;
    	var height = 800;
    	var left = (screen.width - width) / 2;
    	var top = (screen.height - height) / 2;
    	var options = 'width=' + width + ', height=' + height + ', top=' + top + ', left=' + left + ', resizable=no, scrollbars=yes'; //창크기 조절 불가능, 스크롤바 활성화 

    	// 새 창 열기
    	var popup = window.open('', 'popupWindow', options);

    	// 팝업 창의 문서에 HTML 삽입
    	popup.document.write(`
    			<!DOCTYPE html>
    			<html lang="ko">
    			<head>
    			    <meta charset="UTF-8">
    			    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    			    <title>게임 정보 카드<\/title>
    			    <style>
    			    	table{
    			    		border-collapse:collapse;
    			    		width: 700px;
    			    	}
    			        img{
    			        	display: block;
    			        	width:100%;
    			        	user-select: none;
    			        }
    			        p{
    			        	text-align: left;
    			        	padding: 10px;
    			        }
    			        .name{
    			        	font-weight: 700;
    			        	font-size: 25px;
    			        }
    			        .td-1{
    			        	width: 100px;
    			        }
    			        td{
    			        	border: 1px solid lightpink;
    			        }
    			        .header{
			        		text-align:center;
    			        }
    			    <\/style>
    			<\/head>
    			<body>
    			<center>
					<table>
						<tr>
							<td class="td-1" colspan='2' style="text-align: center;">
								<img src="`+HEADER_IMAGE+`">
							<\/td>
						<\/tr>
						<tr>
							<td colspan='2'>
								<p><span class="name">`+GAME_NAME+`<\/span><\/p>
							<\/td>
						<\/tr>
						<tr>
							<td class="td-1">
								<p class="header">추천<\/p>
							<\/td>
							<td>
								<p>`+RECOMMENDATIONS+`<\/p>
							<\/td>
						<\/tr>
						<tr>
							<td class="td-1">
								<p class="header">가격<\/p>
							<\/td>
							<td>
								<p>`+PRICE+`원<\/p>
							<\/td>
						<\/tr>
						<tr>
							<td class="td-1">
								<p class="header">출시일<\/p>
							<\/td>
							<td>
								<p>`+RELEASE_DATE+`<\/p>
							<\/td>
						<\/tr>
						<tr>
							<td class="td-1">
								<p class="header">연령 등급<\/p>
							<\/td>
							<td>
								<p> 
									`+(RATINGS == 18 ? "성인" : RATINGS)+`
								<\/p>
							<\/td>
						<\/tr>
					<\/table>
    			<\/center>
    			<\/body>
    			<\/html>
    	`);
    }
    
    
</script>
</head>
<body>
<center>
<form name="submitForm">
	<div class="search-container">
		<input class="searchBar" name="keyword" placeholder="검색">
		<div class="searchButton" onclick="search(0)">검색</div>
	</div>
	<div class="searchFiltersBox">
		<select class="ratings" name="ratings">
			<option value="">연령 등급</option>
			<option value="l">전체연령가</option>
			<option value="10">10세 이상</option>
			<option value="12">12세 이상</option>
			<option value="14">14세 이상</option>
			<option value="16">16세 이상</option>
			<option value="18">성인</option>
		</select>
		<select class="categories" name="categories">
			<option value="">카테고리</option>
			<option value="Single-player">싱글 플레이</option>
			<option value="Multi-player">멀티 플레이</option>
			<option value="PvP">PvP</option>
			<option value="Co-op">Co-op</option>
			<option value="VR">VR 지원</option>
			<option value="MMO">MMO</option>
		</select>
		<select class="genres" name="genres">
			<option value="">분류</option>
			<option value="Action">액션</option>
			<option value="Adventure">어드벤쳐</option>
			<option value="Indie">인디</option>
			<option value="Racing">레이싱</option>
			<option value="Simulation">시뮬레이션</option>
			<option value="Casual">캐쥬얼</option>
			<option value="Sports">스포츠</option>
			<option value="RPG">RPG</option>
		</select>
		<select class="tags" name="tags">
			<option value="">태그</option>
			<option value="Open World">오픈 월드</option>
			<option value="FPS">FPS</option>
			<option value="Survival">생존</option>
			<option value="Shooter">슈팅</option>
			<option value="Rhythm">리듬</option>
			<option value="Sandbox">샌드박스</option>
			<option value="Story">스토리</option>
			<option value="Horror">공포</option>
			<option value="Sci-Fi">SF</option>
			<option value="Exploration">탐험</option>
			<option value="Arcade">아케이드</option>
			<option value="Zombie">좀비</option>
			<option value="Funny">재미</option>
			<option value="Fighting">격투</option>
			<option value="Puzzle">퍼즐</option>
			<option value="Roguelike">로그라이크</option>
			<option value="Building">건축</option>
			<option value="City">도시 건설</option>
			<option value="Sports">스포츠</option>
			<option value="Card">카드</option>
			<option value="Escape">탈출</option>
			<option value="Mystery">미스터리</option>
			<option value="Dark Fantasy">다크 판타지</option>
			<option value="Crime">범죄</option>
			<option value="Space">우주 공간</option>
			<option value="Farm">농장</option>
			<option value="Gambling">갬블</option>
			<option value="Hentai">성적인</option>
		</select>
  		<select class="sort" name="sort">
			<option value="recommendations desc">사용자 추천▲</option>
			<option value="recommendations asc">사용자 추천▼</option>
			<option value="release_date desc">출시일▲</option>
			<option value="release_date asc">출시일▼</option>
		</select>
	</div>
	<!-- 띄우기 -->
	<div style="height:10px;"></div>

	<div class="searchResult-div">
		<c:if test="${requestScope.searchMap.searchResultCount gt 0}">
			<div class="searchResult">
				<table class="gameTable">
					<c:choose>
					    <c:when test="${requestScope.searchMap.searchCount <= 4}">
							<tr>
								<c:forEach var="i" items="${requestScope.searchMap.searchList}" varStatus="status">
									<c:if test="${status.index < 4}">
										<td onclick="popup(`${i.HEADER_IMAGE}`, `${i.GAME_NAME}`,`${i.RECOMMENDATIONS}`,`${i.PRICE}`,`${i.RELEASE_DATE}`,`${i.RATINGS}`)">
											<img src="${i.HEADER_IMAGE}" alt="이미지 없음">
											<span class="gameName-span">
												<span>${i.GAME_NAME}</span>
											</span>
										</td>
									</c:if>
					        	</c:forEach>
							</tr>
					    </c:when>
					    <c:when test="${requestScope.searchMap.searchCount > 4}">
							<tr>
								<c:forEach var="i" items="${requestScope.searchMap.searchList}" varStatus="status">
									<c:if test="${status.index < 4}">
										<td onclick="popup(`${i.HEADER_IMAGE}`, `${i.GAME_NAME}`,`${i.RECOMMENDATIONS}`,`${i.PRICE}`,`${i.RELEASE_DATE}`,`${i.RATINGS}`)">
											<img src="${i.HEADER_IMAGE}" alt="이미지 없음">
											<span class="gameName-span">
												<span>${i.GAME_NAME}</span>
											</span>
										</td>
									</c:if>
					        	</c:forEach>
							</tr>
							<tr>
								<c:forEach var="i" items="${requestScope.searchMap.searchList}" varStatus="status">
									<c:if test="${status.index >= 4}">
										<td onclick="popup(`${i.HEADER_IMAGE}`, `${i.GAME_NAME}`,`${i.RECOMMENDATIONS}`,`${i.PRICE}`,`${i.RELEASE_DATE}`,`${i.RATINGS}`)">
											<img src="${i.HEADER_IMAGE}" alt="이미지 없음">
											<span class="gameName-span">
												<span>${i.GAME_NAME}</span>
											</span>
										</td>
									</c:if>
					        	</c:forEach>
							</tr>
					    </c:when>
					</c:choose>
				</table>
				<div class="pageNos" style="user-select: none;">
				    <span style="cursor:pointer" onClick="pageNoClick(1)">[처음]</span>
				    <span style="cursor:pointer" onClick="pageNoClick(${requestScope.searchMap.selectPageNo-1})">[이전]</span>
				    <c:forEach var="pageNo" begin="${requestScope.searchMap.begin_pageNo}" end="${requestScope.searchMap.end_pageNo}">
				        <c:choose>
				            <c:when test="${requestScope.searchMap.selectPageNo==pageNo}">
				                <span style="font-weight:bold;">${pageNo}</span>
				            </c:when>
				            <c:otherwise>
				                <span style="cursor:pointer" onClick="pageNoClick(${pageNo})">[${pageNo}]</span>
				            </c:otherwise>
				        </c:choose>
				    </c:forEach>
				    <span style="cursor:pointer" onClick="pageNoClick(${requestScope.searchMap.selectPageNo+1})">[다음]</span>
				    <span style="cursor:pointer" onClick="pageNoClick(${requestScope.searchMap.last_pageNo})">[마지막]</span>
				</div>
			</div>
		</c:if>
		<input type="hidden" name="selectPageNo" value="1">
		<span class="last_pageNoHistory" style="display:none;">${requestScope.searchMap.last_pageNo}</span>
	</div>
</form>

<input type="hidden" name="keywordHistory">
<input type="hidden" name="ratingsHistory">
<input type="hidden" name="categoriesHistory">
<input type="hidden" name="genresHistory">
<input type="hidden" name="tagsHistory">
<input type="hidden" name="sortHistory">

</center>
</body>
</html>
