package com.saleoa.service;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Sale;

public interface ISaleService extends IBaseService<Sale> {

	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
}
