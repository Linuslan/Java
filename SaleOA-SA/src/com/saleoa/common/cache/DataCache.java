package com.saleoa.common.cache;

import java.util.HashMap;
import java.util.Map;

public class DataCache {
	private static final Map<String, Object> cache = new HashMap<String, Object>();
	private static final Map<String, Object> secondCache = new HashMap<String, Object> ();
	private static final int cacheSize = 500000;
	public static void push(String key, Object value) {
		if(cache.size() <= cacheSize) {
			cache.put(key, value);
		} else if(secondCache.size() <= cacheSize) {
			secondCache.put(key, value);
		}
	}
	
	public static Object get(String key) {
		if(null != cache.get(key)) {
			return cache.get(key);
		}
		if(null != secondCache.get(key)) {
			return secondCache.get(key);
		}
		return null;
	}
	
	public static void remove(String key) {
		if(null != cache.get(key)) {
			cache.remove(key);
		}
		if(null != secondCache.get(key)) {
			secondCache.remove(key);
		}
	}
}
