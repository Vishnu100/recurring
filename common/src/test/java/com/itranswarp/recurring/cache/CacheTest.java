package com.itranswarp.recurring.cache;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CacheTest {

	Cache cache = null;

	@Before
	public void setUp() throws Exception {
		cache = new Cache();
		cache.init();
	}

	@After
	public void tearDown() {
		cache.destroy();
	}

	@Test
	public void testGetEmpty() {
		assertNull(cache.get("this-key-is-not-exist"));
	}

	@Test
	public void testGetObject() {
		String key = UUID.randomUUID().toString();
		assertNull(cache.get(key));
		String result = cache.get(key, (s) -> {
			return "RESULT-" + s;
		});
		assertEquals("RESULT-" + key, result);
	}

	@Test
	public void testGetBean() {
		String key = UUID.randomUUID().toString();
		assertNull(cache.get(key));
		CachedBean origin = new CachedBean(key, LocalDate.of(2015, 5, 5), true, 12300990083L);
		CachedBean cached1 = cache.get(key, (s) -> {
			return origin;
		});
		assertSame(origin, cached1);
		CachedBean cached2 = cache.get(key, (s) -> {
			fail("Should not call this function!");
			return null;
		});
		assertNotSame(origin, cached2);
		assertEquals(origin, cached2);
	}
}

class CachedBean implements Serializable {

	String id;

	LocalDate start;

	boolean expires;

	long createdAt;

	public CachedBean(String id, LocalDate start, boolean expires, long createdAt) {
		this.id = id;
		this.start = start;
		this.expires = expires;
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CachedBean) {
			CachedBean b = (CachedBean) o;
			return this.expires == b.expires && this.createdAt == b.createdAt && this.id.equals(b.id)
					&& this.start.equals(b.start);
		}
		return false;
	}
}
