package com.saleoa.base;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.JdbcHelper;


public class IBaseServiceImpl<T> implements IBaseService<T> {
	protected IBaseDao<T> dao;
	
	public boolean add(T t) throws Exception {
		// TODO Auto-generated method stub
		return dao.add(t);
	}
	
	public boolean addBatch(List<T> list) throws Exception {
		// TODO Auto-generated method stub
		return dao.addBatch(list);
	}

	public boolean update(T t) throws Exception {
		// TODO Auto-generated method stub
		return dao.update(t);
	}

	public boolean updateBatch(List<T> list) throws Exception {
		return dao.updateBatch(list);
	}

	public boolean delete(T t) throws Exception {
		// TODO Auto-generated method stub
		return dao.delete(t);
	}

	public List<T> select(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return dao.select(paramMap);
	}

	public List<T> selectCacheAll() {
		return dao.selectCacheAll();
	}
	
	/**
	 * 查询所有的对象
	 * @return
	 */
	public List<T> selectAll() {
		return dao.selectAll();
	}

	public T selectOne(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectOne(paramMap);
	}
	
	public T selectById(Long id) throws Exception {
		return dao.selectById(id);
	}

	public Page<T> selectPage(Map<String, Object> paramMap, long currPage, int limit) throws Exception {
		// TODO Auto-generated method stub
		return dao.selectPage(paramMap, currPage, limit);
	}
}
