package com.itranswarp.recurring.db;

public final class Select<T> {


	final Class<T> clazz;

	Select(Class<T> clazz) {
		this.clazz = clazz;
	}

}
