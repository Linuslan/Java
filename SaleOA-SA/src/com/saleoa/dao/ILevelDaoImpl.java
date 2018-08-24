package com.saleoa.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Level;

public class ILevelDaoImpl extends IBaseDaoImpl<Level> implements ILevelDao {
	/**
	 * ��ѯ��߲㼶����Ͳ㼶
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
	 * ��ѯ���ֶ�Ӧ�ĵȼ�
	 * @param point
	 * @return
	 */
	public Level selectByPoint(long point) {
		Level level = null;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("minPoint<=", point);
			paramMap.put("maxPoint>=", point);
			String sql = JdbcHelper.selectSql(Level.class, paramMap, false, null, null);
			List<Level> levels = JdbcHelper.select(sql, Level.class);
			if(null == levels || 0 >= levels.size()) {
				ExceptionUtil.throwExcep("δ��ѯ���ȼ�");
			}
			level = levels.get(0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return level;
	}
}
