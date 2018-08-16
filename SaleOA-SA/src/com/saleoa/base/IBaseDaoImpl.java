package com.saleoa.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.JdbcHelper;


public class IBaseDaoImpl<T> implements IBaseDao<T> {
	private Class<T> entityClass;
	
	public Class<T> getTClass() {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
	public boolean add(T t) {
		// TODO Auto-generated method stub
		boolean success = false;
		try {
			Long id = JdbcHelper.id(t.getClass());
			BeanUtil.setValue(t, "id", id);
			String sql = JdbcHelper.insertSql(t);
			JdbcHelper.executeSql(sql);
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public boolean update(T t) {
		// TODO Auto-generated method stub
		boolean success = false;
		try {
			String sql = JdbcHelper.updateSql(t);
			JdbcHelper.executeSql(sql);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	public boolean delete(T t) {
		// TODO Auto-generated method stub
		boolean success = false;
		try {
			try {
				Field field = t.getClass().getDeclaredField("isDelete");
				BeanUtil.setValue(t, "isDelete", 1);
				this.update(t);
			} catch(Exception ex) {
				String sql = JdbcHelper.deleteSql(t);
				JdbcHelper.executeSql(sql);
			}
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public List<T> select(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try {
			//T t = entityClass.newInstance();
			try {
				getTClass().getField("isDelete");
				paramMap.put("isDelete", 0);
			} catch(Exception ex) {
				
			}
			
			String sql = JdbcHelper.selectSql(getTClass(), paramMap);
			System.out.println(sql);
			List<T> list = JdbcHelper.select(sql, getTClass());
			return list;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public T selectOne(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
}