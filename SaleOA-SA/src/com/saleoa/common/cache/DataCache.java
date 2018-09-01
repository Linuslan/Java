package com.saleoa.common.cache;

import java.util.HashMap;
import java.util.Map;

public class DataCache {
	private static final Map<String, Object> cache = new HashMap<String, Object>();
	
	public static void push(String key, Object value) {
		cache.put(key, value);
	}
	
	public static Object get(String key) {
		return cache.get(key);
	}
	
	public static void remove(String key) {
		cache.remove(key);
	}
}
