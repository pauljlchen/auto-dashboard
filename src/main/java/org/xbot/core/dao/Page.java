package org.xbot.core.dao;

import java.util.LinkedHashMap;
import java.util.Map;

public class Page {
	private Long firstPage = 1L;
	private Long currentPage;
	private Long requestedPage;
	private Long recordsPerPage;
	private Long lastPage;
	private Map<String, ORDER> order = new LinkedHashMap<String, ORDER>();
	public static enum ORDER{DESC, ASC};

	/**
	 * By default if no change for the parameters
	 */
	public Page(){
		currentPage=1L;
		requestedPage=1L;
		recordsPerPage=20L;
		lastPage=1L;
	}
	public Page(final Long currentPage, final Long requestedPage, final Long recordsPerPage, final Map<String, ORDER> order){
		this.currentPage = currentPage;
		this.requestedPage = requestedPage;
		this.recordsPerPage = recordsPerPage;
		this.order = order;
	}
	
	public Long getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(Long firstPage) {
		this.firstPage = firstPage;
	}
	public Long getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}
	public Long getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(Long recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public Long getLastPage() {
		return lastPage;
	}
	public void setLastPage(Long lastPage) {
		this.lastPage = lastPage;
	}
	public Long getRequestedPage() {
		return requestedPage;
	}
	public void setRequestedPage(Long requestedPage) {
		this.requestedPage = requestedPage;
	}
	@Override
	public String toString() {
		return "Page [firstPage=" + firstPage + ", currentPage=" + currentPage
				+ ", requestedPage=" + requestedPage + ", recordsPerPage="
				+ recordsPerPage + ", lastPage=" + lastPage + "]";
	}
	public Map<String, ORDER> getOrder() {
		return order;
	}
	public void setOrder(Map<String, ORDER> order) {
		this.order = order;
	}
	 
}
