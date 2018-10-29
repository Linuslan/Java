package com.saleoa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.cache.DataCache;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Level;
import com.saleoa.model.SaleSalary;

public class ILevelDaoImpl extends IBaseDaoImpl<Level> implements ILevelDao {
	/**
	 * 查询最高层级或最低层级
	 * @return
	 */
	public Level selectExtremeLevel(boolean isMax) {
		Level level = null;
		try {
			String sql = "SELECT * FROM tbl_oa_level t ORDER BY t.id "+(isMax?"DESC":"ASC");
			List<Level> levels = JdbcHelper.select(sql, Level.class);
			if(null == levels || 0 >= levels.size()) {
				return null;
			}
			level = levels.get(0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return level;
	}
	
	/**
	 * 查询积分对应的等级
	 * @param point
	 * @return
	 */
	public Level selectByPoint(long point) {
		Level level = null;
		try {
			String key = this.getKey();
			List<Level> list = (List<Level>) DataCache.selectAll(key);
			if(list.size() > 0) {
				List<Level> selectedList = new ArrayList<Level>();
				for(int i = 0; i < list.size(); i ++) {
					Level temp = list.get(i);
					if(temp.getMinPoint() > point) {
						continue;
					}
					if(temp.getMaxPoint() < point) {
						continue;
					}
					selectedList.add(temp);
				}
				if(selectedList.size() > 0) {
					level = selectedList.get(0);
				}
			}
			if(null != level) {
				return level;
			}
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("minPoint<=", point);
			paramMap.put("maxPoint>=", point);
			String sql = JdbcHelper.selectSql(Level.class, paramMap, false, null, null);
			List<Level> levels = JdbcHelper.select(sql, Level.class);
			if(null == levels || 0 >= levels.size()) {
				ExceptionUtil.throwExcep("未查询到等级");
			}
			level = levels.get(0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return level;
	}
}
