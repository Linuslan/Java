package com.saleoa.service;

import com.saleoa.base.IBaseService;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Sale;

public interface IManagerLevelService extends IBaseService<ManagerLevel> {

	/**
	 * 查询销售的数量查询对应的等级
	 * @param point
	 * @return
	 */
	public ManagerLevel selectBySale(long sale);
}
