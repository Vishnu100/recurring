package com.itranswarp.recurring.cache;

import java.io.IOException;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * A memcached based caching service.
 */
@Named
public class Cache {

	final Log log = LogFactory.getLog(getClass());

	@Value("${cache.servers}")
	String servers = "127.0.0.1:11211";

	@Value("${cache.expires}")
	int expires = 3600;

	MemcachedClient mc = null;

	@PostConstruct
	public void init() {
		try {
			this.mc = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(this.servers));
		} catch (IOException e) {
			log.warn("Cannot connect to memcached. Cache is disabled.");
		}
	}

	@PreDestroy
	public void destroy() {
		if (this.mc != null) {
			this.mc.shutdown();
		}
		this.mc = null;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String id) {
		if (this.mc == null) {
			return null;
		}
		return (T) this.mc.get(id);
	}

	public <T> T get(String id, Function<String, T> fn) {
		if (this.mc == null) {
			return fn.apply(id);
		}
		T t = get(id);
		if (t == null) {
			log.info("Key is missing in memcached: " + id);
			t = fn.apply(id);
			this.mc.set(id, this.expires, t);
			log.info("Put object to memcached: " + id);
		} else {
			log.info("Key hit in memcached: " + id);
		}
		return t;
	}

	public void remove(String id) {
		if (this.mc != null) {
			log.info("Manualy remove key from memcached: " + id);
			this.mc.delete(id);
		}
	}
}
