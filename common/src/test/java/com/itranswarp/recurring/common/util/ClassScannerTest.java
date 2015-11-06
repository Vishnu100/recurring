package com.itranswarp.recurring.common.util;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.itranswarp.recurring.common.util.ClassScanner;

public class ClassScannerTest {

	@Test
	public void testScanInDir() throws Exception {
		ClassScanner cs = new ClassScanner();
		List<Class<?>> list = cs.scan("com.itranswarp", c -> {
			return c.getSimpleName().equals("IdGenerator");
		});
		assertEquals(1, list.size());
		assertEquals(IdGenerator.class, list.get(0));
	}

	@Test
	public void testScanInJar() {
		ClassScanner cs = new ClassScanner();
		List<Class<?>> list = cs.scan("org.apache.commons", c -> {
			return c.getName().startsWith("org.apache.commons.logging.Log");
		});
		assertTrue(list.size() >= 3);
		assertTrue(list.contains(Log.class));
		assertTrue(list.contains(LogConfigurationException.class));
		assertTrue(list.contains(LogFactory.class));
	}

}
