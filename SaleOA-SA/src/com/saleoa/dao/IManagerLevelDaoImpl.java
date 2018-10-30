package com.saleoa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.cache.DataCache;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.ManagerLevel;

public class IManagerLevelDaoImpl extends IBaseDaoImpl<ManagerLevel> implements IManagerLevelDao {
	
	/**
	 * 查询销售的数量查询对应的等级
	 * @param point
	 * @return
	 */
	public ManagerLevel selectBySale(long sale, Long departmentId) {
		ManagerLevel level = null;
		try {
			List<ManagerLevel> allList = (List<ManagerLevel>) DataCache.selectAll(this.getKey());
			Iterator<ManagerLevel> iter = allList.iterator();
			List<ManagerLevel> selectedList = new ArrayList<ManagerLevel> ();
			while(iter.hasNext()) {
				ManagerLevel ml = iter.next();
				if(ml.getMinSale() <= sale && ml.getMaxSale() >= sale
						&& ml.getDepartmentId().longValue() == departmentId.longValue()) {
					selectedList.add(ml);
				}
			}
			if(null == selectedList || 0 >= selectedList.size()) {
				ExceptionUtil.throwExcep("未查询到等级");
			}
			level = selectedList.get(0);
			/*Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("minSale<=", sale);
			paramMap.put("maxSale>=", sale);
			paramMap.put("departmentId", departmentId);
			String sql = JdbcHelper.selectSql(ManagerLevel.class, paramMap, false, null, null);
			List<ManagerLevel> levels = JdbcHelper.select(sql, ManagerLevel.class);
			if(null == levels || 0 >= levels.size()) {
				ExceptionUtil.throwExcep("未查询到等级");
			}*/
			//level = levels.get(0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return level;
	}
}
