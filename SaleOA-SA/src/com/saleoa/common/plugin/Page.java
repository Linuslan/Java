package com.saleoa.common.plugin;

import java.util.List;

import com.saleoa.common.annotation.Column;

public class Page<T> {
	private long currPage;
	private long totalPage;
	private int limit;
	private List<T> data;
	@Column(name="total_count")
	private Long totalCount;
	
	public Page() {
		
	}
	
	public Page(long currPage, long totalPage, int limit, List<T> data) {
		this.currPage = currPage;
		this.totalPage = totalPage;
		this.limit = limit;
		this.data = data;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
