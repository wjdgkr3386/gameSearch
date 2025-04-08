package com.example.demo;

public class GameSearchDTO {

	private String keyword;			//키워드
	private String ratings;			//연령등급
	private String categories;		//카테고리
	private int price;				//가격	
	private String sort;			//정렬기준
	private String genres;			//정렬기준
	private String tags;			//정렬기준
	
	private int last_pageNo;		//마지막 페이지번호
	private int selectPageNo;		//선택한 페이지번호
	private int begin_pageNo;		//화면의 표시할 처음 페이지번호
	private int end_pageNo;			//화면의 표시할 마지막 페이지번호
	private int rowCnt;				//행보기
	private int begin_rowNo;		//테이블 검색 시 시작행 번호
	private int end_rowNo;			//테이블 검색 시 끝행 번호
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getRatings() {
		return ratings;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getLast_pageNo() {
		return last_pageNo;
	}
	public void setLast_pageNo(int last_pageNo) {
		this.last_pageNo = last_pageNo;
	}
	public int getSelectPageNo() {
		return selectPageNo;
	}
	public void setSelectPageNo(int selectPageNo) {
		this.selectPageNo = selectPageNo;
	}
	public int getBegin_pageNo() {
		return begin_pageNo;
	}
	public void setBegin_pageNo(int begin_pageNo) {
		this.begin_pageNo = begin_pageNo;
	}
	public int getEnd_pageNo() {
		return end_pageNo;
	}
	public void setEnd_pageNo(int end_pageNo) {
		this.end_pageNo = end_pageNo;
	}
	public int getRowCnt() {
		return rowCnt;
	}
	public void setRowCnt(int rowCnt) {
		this.rowCnt = rowCnt;
	}
	public int getBegin_rowNo() {
		return begin_rowNo;
	}
	public void setBegin_rowNo(int begin_rowNo) {
		this.begin_rowNo = begin_rowNo;
	}
	public int getEnd_rowNo() {
		return end_rowNo;
	}
	public void setEnd_rowNo(int end_rowNo) {
		this.end_rowNo = end_rowNo;
	}
	
	
	
}
