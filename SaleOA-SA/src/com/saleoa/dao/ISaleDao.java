package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Sale;

public interface ISaleDao extends IBaseDao<Sale> {

	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
}
