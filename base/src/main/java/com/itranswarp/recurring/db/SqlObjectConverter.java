package com.itranswarp.recurring.db;

@FunctionalInterface
public interface SqlObjectConverter {

	public Object convert(Object obj);
}
