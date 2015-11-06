package com.itranswarp.recurring.db;

public class Page {

	public static final int DEFAULT_ITEMS_PER_PAGE = 20;

	int pageIndex;
	int itemsPerPage;
	int totalItems;
	int totalPages;

	public Page(int pageIndex) {
		this(pageIndex, DEFAULT_ITEMS_PER_PAGE);
	}

	public Page(int pageIndex, int itemsPerPage) {
		this.pageIndex = pageIndex;
		this.itemsPerPage = itemsPerPage;
		this.totalPages = 0;
	}

	int offset() {
		return (pageIndex - 1) * itemsPerPage;
	}

	void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
		if (totalItems > 0) {
			this.totalPages = totalItems / itemsPerPage + (totalItems % itemsPerPage > 0 ? 1 : 0);
		}
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}
}
