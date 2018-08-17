package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Level;

public interface ILevelDao extends IBaseDao<Level> {

	/**
	 * 查询最高层级或最低层级
	 * @return
	 */
	public Level selectExtremeLevel(boolean isMax);
	
	/**
	 * 查询积分对应的等级
	 * @param point
	 * @return
	 */
	public Level selectByPoint(long point);
}
