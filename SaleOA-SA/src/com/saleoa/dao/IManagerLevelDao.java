package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Level;
import com.saleoa.model.ManagerLevel;

public interface IManagerLevelDao extends IBaseDao<ManagerLevel> {
	
	/**
	 * 查询积分对应的等级
	 * @param point
	 * @return
	 */
	public ManagerLevel selectBySale(long sale, Long departmentId);
}
