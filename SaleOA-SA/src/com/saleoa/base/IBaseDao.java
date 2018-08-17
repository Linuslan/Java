package com.saleoa.base;

import java.util.List;
import java.util.Map;

public interface IBaseDao<T> {
	public boolean add(T t);
	
	public boolean update(T t);
	
	public boolean updateBatch(List<T> list);
	
	public boolean delete(T t);
	
	public List<T> select(Map<String, Object> paramMap);
	
	public T selectOne(Map<String, Object> paramMap);
	
	public T selectById(Long id);
}
