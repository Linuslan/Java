package com.saleoa.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.common.annotation.Table;
import com.saleoa.common.cache.DataCache;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.AnnotationUtil;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.JdbcHelper;


public class IBaseDaoImpl<T> implements IBaseDao<T> {
	private Class<T> entityClass;
	
	public String getKey() {
		Class<T> cls = getTClass();
		String key = cls.getSimpleName()+"_";
		return key;
	}
	
	public String getPKey() {
		Class<T> cls = getTClass();
		String key = cls.getSimpleName()+"_p_";
		return key;
	}
	
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
			String key = getKey()+id.longValue();
			DataCache.push(key, t);
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	public boolean addBatch(List<T> list) {
		// TODO Auto-generated method stub
		boolean success = false;
		try {
			Long id = JdbcHelper.id(getTClass());
			for(int i = 0; i < list.size(); i ++) {
				id ++;
				BeanUtil.setValue(list.get(i), "id", id);
			}
			JdbcHelper.insertBatch(list);
			this.pushList(list);
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
			success = JdbcHelper.update(t);
			Long id = (Long) BeanUtil.getValue(t, "id");
			String key = getKey()+id.longValue();
			DataCache.push(key, t);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public boolean updateBatch(List<T> list) {
		boolean success = false;
		try {
			success = JdbcHelper.updateBatch(list);
			this.pushList(list);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	public boolean delete(T t) {
		// TODO Auto-generated method stub
		boolean success = false;
		try {
			String pKey = getKey();
			String key = pKey+((Long)BeanUtil.getValue(t, "id")).longValue();
			try {
				Field field = t.getClass().getDeclaredField("isDelete");
				BeanUtil.setValue(t, "isDelete", 1);
				this.update(t);
			} catch(Exception ex) {
				String sql = JdbcHelper.deleteSql(t);
				JdbcHelper.executeSql(sql);
			}
			DataCache.remove(key, pKey);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}

	public List<T> select(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try {
			//T t = entityClass.newInstance();
			try {
				getTClass().getField("isDelete");
				paramMap.put("isDelete", 0);
			} catch(Exception ex) {
				//ex.printStackTrace();
				System.out.println("未找到is_delete属性");
			}
			
			String sql = JdbcHelper.selectSql(getTClass(), paramMap, false, null, null);
			System.out.println(sql);
			List<T> list = JdbcHelper.select(sql, getTClass());
			this.pushList(list);
			return list;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<T> selectCacheAll() {
		return (List<T>) DataCache.selectAll(this.getKey());
	}
	
	/**
	 * 查询所有的对象
	 * @return
	 */
	public List<T> selectAll() {
		List<T> list = null;
		try {
			Class<T> cls = this.getTClass();
			String tableName = AnnotationUtil.getTableName(cls);
			String sql = "SELECT * FROM "+tableName+" ORDER BY id ASC";
			list = JdbcHelper.select(sql, cls);
			this.pushList(list);
			System.out.println("加载("+tableName+")所有数据成功");
		} catch(Exception ex) {
			ex.printStackTrace();
			list = new ArrayList<T>();
		}
		return list;
	}

	public T selectById(Long id) {
		String key = getKey()+id.longValue();
		Object obj = DataCache.get(key);
		if(null != obj) {
			return (T) obj;
		}
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("id", id);
		List<T> list = this.select(paramMap);
		T t = list.get(0);
		DataCache.push(key, t);
		return t;
	}
	
	public T selectOne(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Page<T> selectPage(Map<String, Object> paramMap, long currPage, int limit) {
		Page<T> page = null;
		try {
			String sql = JdbcHelper.selectSql(getTClass(), paramMap, false, currPage, limit);
			List<T> list = JdbcHelper.select(sql, getTClass());
			if(null == list) {
				list = new ArrayList<T> ();
			}
			sql = JdbcHelper.selectSql(getTClass(), paramMap, true, null, null);
			page = (Page<T>) JdbcHelper.select(sql, Page.class).get(0);
			page.setData(list);
			page.setLimit(limit);
			page.setCurrPage(currPage);
			page.setTotalPage(page.getTotalCount()%limit == 0 ? page.getTotalCount()/limit : page.getTotalCount()/limit+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return page;
	}

	public void pushList(List<T> list) {
		for(int i = 0; i < list.size(); i ++) {
			T t = list.get(i);
			Long objId = (Long) BeanUtil.getValue(t, "id");
			String key = getKey()+objId.longValue();
			DataCache.push(key, t);
		}
	}
	
}
