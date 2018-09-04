package com.saleoa.common.cache;

import java.util.HashMap;
import java.util.Map;

public class DataCache {
	private static final Map<String, Object> cache = new HashMap<String, Object>();
	private static final Map<String, Object> secondCache = new HashMap<String, Object> ();
	private static final Map<String, Map<String, Object>> treeCache = new HashMap<String, Map<String, Object>> ();
	private static final Map<String, Map<String, Object>> treeSecondCache = new HashMap<String, Map<String, Object>> ();
	private static final int cacheSize = 500000;
	public static void push(String key, Object value) {
		if(cache.size() <= cacheSize) {
			cache.put(key, value);
		} else if(secondCache.size() <= cacheSize) {
			secondCache.put(key, value);
		} else {
			cache.put(key, value);
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
	
	/**
	 * 
	 * @param key ���ڵ��id��Ϊkey
	 * @param cKey ���ڵ����ӽڵ��id��Ϊkey
	 * @param value
	 */
	public static void treePush(String key, String cKey, Object value) {
		Map<String, Map<String, Object>> map = null;
		if(null != treeCache.get(key)) {
			map = treeCache;
		} else if(null != treeSecondCache.get(key)) {
			map = treeSecondCache;
		}
		if(null == map) {
			if(treeCache.size() <= cacheSize) {
				map = treeCache;
			} else if(treeSecondCache.size() <= cacheSize) {
				map = treeSecondCache;
			} else {
				map = treeCache;
			}
		}
		Map<String, Object> childMap = map.get(key);
		if(null == childMap) {
			childMap = new HashMap<String, Object> ();
			map.put(key, childMap);
		}
		childMap.put(cKey, value);
	}
	
	/**
	 * ��ȡ�ӽڵ�ļ���
	 * @param key
	 * @return
	 */
	public static Map<String, Object> treeGet(String key) {
		if(null != treeCache.get(key)) {
			return treeCache.get(key);
		}
		if(null != treeSecondCache.get(key)) {
			return treeSecondCache.get(key);
		}
		return null;
	}
	
	/**
	 * ��ȡ�ӽڵ�
	 * @param key ���ڵ��key
	 * @param cKey �ӽڵ��key
	 * @return
	 */
	public static Object treeGetChild(String key, String cKey) {
		Map<String, Object> childMap = treeCache.get(key);
		if(null == childMap) {
			childMap = treeSecondCache.get(key);
		}
		if(null == childMap) {
			return null;
		}
		return childMap.get(cKey);
	}
	
	/**
	 * �Ƴ����ڵ�
	 * @param key
	 */
	public static void treeRemove(String key) {
		if(null != treeCache.get(key)) {
			treeCache.remove(key);
		}
		if(null != treeSecondCache.get(key)) {
			treeSecondCache.remove(key);
		}
	}
	
	/**
	 * �Ƴ����ڵ��е��ӽڵ�
	 * @param key
	 * @param cKey
	 */
	public static void treeRemoveChild(String key, String cKey) {
		Map<String, Object> childMap = treeCache.get(key);
		if(null == childMap) {
			childMap = treeSecondCache.get(key);
		}
		if(null == childMap) {
			return;
		}
		if(null == childMap.get(cKey)) {
			return ;
		}
		childMap.remove(cKey);
	}
}