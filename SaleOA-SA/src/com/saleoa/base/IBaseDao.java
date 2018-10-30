package com.saleoa.base;

import java.util.List;
import java.util.Map;

import com.saleoa.common.plugin.Page;

public interface IBaseDao<T> {
	public boolean add(T t);
	
	public boolean addBatch(List<T> list);
	
	public boolean update(T t);
	
	public boolean updateBatch(List<T> list);
	
	public boolean delete(T t);
	
	public List<T> select(Map<String, Object> paramMap);
	
	public List<T> selectCacheAll();

	/**
	 * 查询所有的对象
	 * @return
	 */
	public List<T> selectAll();
	
	public T selectOne(Map<String, Object> paramMap);
	
	public T selectById(Long id);
	
	public Page<T> selectPage(Map<String, Object> paramMap, long currPage, int limit);
}
