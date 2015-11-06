package com.itranswarp.recurring.db;

import java.util.List;

public class Results<T> {

	public final List<T> results;

	public Results(List<T> results) {
		this.results = results;
	}

}
