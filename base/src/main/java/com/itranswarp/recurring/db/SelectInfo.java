package com.itranswarp.recurring.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itranswarp.recurring.base.context.TenantContext;
import com.itranswarp.recurring.db.model.BaseEntity;

final class SelectInfo<T extends BaseEntity> {

	final Database database;
	final Class<T> clazz;
	List<String> where = null;
	List<Object> whereParams = null;
	List<String> orderBy = null;
	int offset = 0;
	int maxResults = 0;

	SelectInfo(Database database, Class<T> clazz) {
		this.database = database;
		this.clazz = clazz;
	}

	String sql(String aggregate) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("SELECT ")
		  .append(aggregate == null ? "*" : aggregate)
		  .append(" FROM ")
		  .append(clazz.getSimpleName());
		sb.append(" WHERE ");
		if (where != null) {
			if (where.size() == 1) {
				// only 1 clause:
				sb.append(where.get(0)).append(" AND tenantId=?");
			} else {
				sb.append("(")
				  .append(String.join(" ", where))
				  .append(") AND tenantId=?");
			}
		} else {
			sb.append("tenantId=?");
		}
		if (orderBy != null) {
			sb.append(" ORDER BY ")
			  .append(String.join(" ", orderBy));
		}
		if (aggregate != null && offset >= 0 && maxResults > 0) {
			sb.append(" LIMIT ?, ?");
		}
		return sb.toString();
	}

	Object[] params(String aggregate) {
		List<Object> params = new ArrayList<Object>();
		if (where != null) {
			params.addAll(whereParams);
		}
		params.add(TenantContext.getCurrentTenant().getId());
		if (aggregate != null && offset >= 0 && maxResults > 0) {
			params.add(offset);
			params.add(maxResults);
		}
		return params.toArray();
	}

	List<T> list() {
		String selectSql = sql(null);
		Object[] selectParams = params(null);
		return database.list(selectSql, selectParams);
	}

	PagedResults<T> list(int pageIndex, int itemsPerPage) {
		Page page = new Page(pageIndex, itemsPerPage);
		List<T> list = list(page);
		return new PagedResults<T>(page, list);
	}

	List<T> list(Page page) {
		this.offset = page.offset();
		this.maxResults = page.itemsPerPage;
		String countSql = sql("count(id)");
		Object[] countParams = params("count(id)");
		int totalItems = database.queryForInt(countSql, countParams);
		page.setTotalItems(totalItems);
		if (totalItems == 0 || page.pageIndex > page.totalPages) {
			return Collections.emptyList();
		}
		String selectSql = sql(null);
		Object[] selectParams = params(null);
		return database.list(selectSql, selectParams);
	}

	int count() {
		String selectSql = sql("count(id)");
		Object[] selectParams = params("count(id)");
		return database.queryForInt(selectSql, selectParams);
	}

	T first() {
		this.offset = 0;
		this.maxResults = 1;
		String selectSql = sql(null);
		Object[] selectParams = params(null);
		List<T> list = database.list(selectSql, selectParams);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	T unique() {
		this.offset = 0;
		this.maxResults = 2;
		String selectSql = sql(null);
		Object[] selectParams = params(null);
		List<T> list = database.list(selectSql, selectParams);
		if (list.isEmpty()) {
			throw new RuntimeException("Expected unique row but nothing found.");
		}
		if (list.size() > 1) {
			throw new RuntimeException("Expected unique row but more than 1 rows found.");
		}
		return list.get(0);
	}
}
