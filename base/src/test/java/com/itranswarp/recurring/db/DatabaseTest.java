package com.itranswarp.recurring.db;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

public class DatabaseTest {

	Database database;

	@Before
	public void setUp() {
		this.database = new Database(new SqlObjectConverters());
	}

	@Test
	public void testRowMapper() throws Exception {
		Map<String, Object> row = new HashMap<String, Object>() {{
			put("id", "A-0001");
			put("name", "Michael");
			put("email", null);
			put("gender", true);
			put("balance", 908.1);
			put("birth", java.sql.Date.valueOf("2001-02-10"));
			put("clock", java.sql.Time.valueOf("17:20:59"));
			put("created", java.sql.Timestamp.valueOf("2015-08-12 18:20:45.123"));
			put("age", 20);
			put("extra", "Extra Data should be ignored...");
		}};
		@SuppressWarnings("unchecked")
		RowMapper<TestUser> mapper = (RowMapper<TestUser>) this.database.mapping.get("testuser").rowMapper;
		TestUser u1 = mapper.mapRow(createResultSet(row), 1);
		assertEquals("A-0001", u1.id);
		assertEquals("Michael", u1.name);
		assertTrue(u1.gender);
		assertEquals(908.1, u1.balance, 0.00001);
		assertEquals(LocalDate.parse("2001-02-10"), u1.birth);
		assertEquals(LocalTime.parse("17:20:59"), u1.clock);
		assertEquals(LocalDateTime.parse("2015-08-12T18:20:45.123"), u1.created);
		assertNull(u1.email);
		assertEquals(0, u1.age);
	}

	@Test
	public void testGetEntity() {
		assertEquals(TestUser.class, database.getEntity("select * from TestUser").clazz);
		assertEquals(TestUser.class, database.getEntity("select * from testuser").clazz);
		assertEquals(TestUser.class, database.getEntity("select * from `testuser`").clazz);
		assertEquals(TestUser.class, database.getEntity("select * from `TestUser` where id=?").clazz);
		assertEquals(TestUser.class, database.getEntity("select * from TestUser where id=?").clazz);
		assertEquals(TestUser.class, database.getEntity("select * from testuser where id=?").clazz);
	}

	ResultSet createResultSet(Map<String, Object> row) throws Exception {
		List<String> columns = new ArrayList<String>(row.keySet());
		List<Object> data = columns.stream().map(s -> {
			return row.get(s);
		}).collect(Collectors.toList());
		return (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { ResultSet.class }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equals("getMetaData")) {
					return createResultSetMetaData(columns);
				}
				if (method.getName().equals("getObject")) {
					return data.get((Integer)args[0]-1);
				}
				throw new UnsupportedOperationException(method.getName());
			}
		});
	}

	ResultSetMetaData createResultSetMetaData(List<String> columns) throws Exception {
		return (ResultSetMetaData) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { ResultSetMetaData.class }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (method.getName().equals("getColumnCount")) {
					return columns.size();
				}
				if (method.getName().equals("getColumnName")) {
					return columns.get((Integer)args[0]-1);
				}
				throw new UnsupportedOperationException(method.getName());
			}
		});
	}
}

@Entity
class TestTenant {

	static int global;

	String id;

	String name;

	@Transient
	String intern;
}

@Entity
class TestUser {

	static int global;

	String id;

	String name;

	String email;

	boolean gender;

	double balance;

	LocalDate birth;

	LocalTime clock;

	LocalDateTime created;

	// should be ignored:
	transient int age;
}
