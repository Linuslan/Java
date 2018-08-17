package com.saleoa.base;

import java.util.List;
import java.util.Map;

public interface IBaseService<T> {
	public boolean add(T t) throws Exception;
	
	public boolean update(T t) throws Exception;
	
	public boolean delete(T t) throws Exception;
	
	public List<T> select(Map<String, Object> paramMap) throws Exception;
	
	public T selectOne(Map<String, Object> paramMap) throws Exception;
	
	public T selectById(Long id) throws Exception;
}
