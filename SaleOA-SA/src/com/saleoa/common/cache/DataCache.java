package com.saleoa.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.common.utils.BeanUtil;
import com.saleoa.model.Sale;

public class DataCache {
	private static final Map<String, Object> cache = new HashMap<String, Object>();
	private static final Map<String, Object> secondCache = new HashMap<String, Object> ();
	private static final Map<String, Map<String, Object>> treeCache = new HashMap<String, Map<String, Object>> ();
	private static final Map<String, Map<String, Object>> treeSecondCache = new HashMap<String, Map<String, Object>> ();
	private static final Map<String, Map<String, Object>> DATA_CACHE = new HashMap<String, Map<String, Object>> ();
	private static final int cacheSize = 500000;
	public static void push(String key, Object value) {
		if(cache.size() <= cacheSize) {
			cache.put(key, value);
		} else if(secondCache.size() <= cacheSize) {
			secondCache.put(key, value);
		} else {
			cache.put(key, value);
		}
		String pKey = value.getClass().getSimpleName()+"_";
		Map<String, Object> map = null;
		if(null == DATA_CACHE.get(pKey)) {
			map = new HashMap<String, Object>();
			DATA_CACHE.put(pKey, map);
		}
		map = DATA_CACHE.get(pKey);
		map.put(key, value);
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
	
	public static void remove(String key, String pKey) {
		if(null != cache.get(key)) {
			cache.remove(key);
		}
		if(null != secondCache.get(key)) {
			secondCache.remove(key);
		}
		if(null != DATA_CACHE.get(pKey) && null != DATA_CACHE.get(pKey).get(key)) {
			DATA_CACHE.get(pKey).remove(key);
		}
	}
	
	public static List<? extends Object> selectAll(String key) {
		List<Object> list = new ArrayList<Object> ();
		Map<String, Object> map = DATA_CACHE.get(key);
		if(null == map) {
			return list;
		}
		list = BeanUtil.parseMapValueToList(map);
		return list;
	}
	
	/**
	 * 
	 * @param key 父节点的id作为key
	 * @param cKey 父节点下子节点的id作为key
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
	 * 获取子节点的集合
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
	 * 获取子节点
	 * @param key 父节点的key
	 * @param cKey 子节点的key
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
	 * 移除父节点
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
	 * 移除父节点中的子节点
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
