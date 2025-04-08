package com.example.demo;

public class Util {
	//검색 페이지 제어
	public static void searchUtil(int searchResultCount, GameSearchDTO GameSearchDTO){
		if(searchResultCount==0) {
			return;
		}
		
		try {
			int rowCnt = 8; //한 페이지 안에 행의 개수
			int selectPageNo = 0; //선택한 페이지 번호
			int begin_rowNo = 0; //시작할 행의 번호
			int end_rowNo = 0;     //끝날 행의 번호
			
			int last_pageNo = 0; //마지막 페이지 번호
			int remainder = 0;   //last_pageNo를 구하기 위한 변수
			int pageNoCntPerPage= 10; //한번에 보여줄 페이지의 갯수
			
			last_pageNo = searchResultCount/rowCnt;
			remainder = searchResultCount%rowCnt;
			if(remainder>0) {
				last_pageNo++;
			}
			
			selectPageNo = GameSearchDTO.getSelectPageNo();
			if(selectPageNo<=0) {
				selectPageNo=1;
			}
			
			begin_rowNo = ((selectPageNo-1)*rowCnt)+1;
			end_rowNo = selectPageNo*rowCnt;
			if( end_rowNo>searchResultCount ) {
				end_rowNo = searchResultCount;
			}
			
			int begin_pageNo = selectPageNo-(pageNoCntPerPage/2);
			if(begin_pageNo<1) {
				begin_pageNo = 1;
			}

			int end_pageNo = begin_pageNo + pageNoCntPerPage - 1;
			if(end_pageNo>last_pageNo) {
				end_pageNo=last_pageNo;
			}
			
			if(selectPageNo+(pageNoCntPerPage/2)>last_pageNo) {
				begin_pageNo = begin_pageNo - (selectPageNo+(pageNoCntPerPage/2)-last_pageNo)+1;
				if(begin_pageNo<1) {
					begin_pageNo = 1;
				}
			}
			
			GameSearchDTO.setLast_pageNo(last_pageNo);
			GameSearchDTO.setRowCnt(rowCnt);
			GameSearchDTO.setSelectPageNo(selectPageNo);
			GameSearchDTO.setBegin_pageNo(begin_pageNo);
			GameSearchDTO.setEnd_pageNo(end_pageNo);
			GameSearchDTO.setBegin_rowNo(begin_rowNo);
			GameSearchDTO.setEnd_rowNo(end_rowNo);
		}catch(Exception e) {
			System.out.println(e);
		}
	}

	//날짜를 문자로 변환
	static String convertDate(String dateStr){
		if(dateStr==null || dateStr.equals("null")) { return dateStr; }
		try {
			if(dateStr.length()==11) {
				dateStr = "0"+dateStr;
			}else if(dateStr.length()<11){
				return "없음";
			}
			
			String day = dateStr.substring( 0, 2 );
			String month = dateStr.substring( 3, 6 );
			String year = dateStr.substring( 8, 12 );
			
	        switch (month) {
	        case "Jan": month = "01"; break;
	        case "Feb": month = "02"; break;
	        case "Mar": month = "03"; break;
	        case "Apr": month = "04"; break;
	        case "May": month = "05"; break;
	        case "Jun": month = "06"; break;
	        case "Jul": month = "07"; break;
	        case "Aug": month = "08"; break;
	        case "Sep": month = "09"; break;
	        case "Oct": month = "10"; break;
	        case "Nov": month = "11"; break;
	        case "Dec": month = "12"; break;
	        default: month = ""; break;
	        }
			return year+month+day;
		}catch(StringIndexOutOfBoundsException t) {
			System.out.println("배열 크기에 맞지 않는 인덱스입니다. 날짜 : ");
			return "없음";
		}catch(Exception e) {
			System.out.println(e);
			return "없음";
		}
	}
}
