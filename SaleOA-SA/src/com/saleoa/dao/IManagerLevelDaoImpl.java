package com.saleoa.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Level;
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
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("minSale<=", sale);
			paramMap.put("maxSale>=", sale);
			paramMap.put("departmentId", departmentId);
			String sql = JdbcHelper.selectSql(ManagerLevel.class, paramMap, false, null, null);
			List<ManagerLevel> levels = JdbcHelper.select(sql, ManagerLevel.class);
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
