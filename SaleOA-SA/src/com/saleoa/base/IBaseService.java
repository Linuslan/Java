package com.saleoa.base;

import java.util.List;
import java.util.Map;

import com.saleoa.common.plugin.Page;

public interface IBaseService<T> {
	public boolean add(T t) throws Exception;
	
	public boolean addBatch(List<T> list) throws Exception;
	
	public boolean update(T t) throws Exception;

	public boolean updateBatch(List<T> list) throws Exception;
	
	public boolean delete(T t) throws Exception;
	
	public List<T> select(Map<String, Object> paramMap) throws Exception;

	public List<T> selectCacheAll();
	
	/**
	 * 查询所有的对象
	 * @return
	 */
	public List<T> selectAll();
	
	public T selectOne(Map<String, Object> paramMap) throws Exception;
	
	public T selectById(Long id) throws Exception;
	
	public Page<T> selectPage(Map<String, Object> paramMap, long currPage, int limit) throws Exception;
}
