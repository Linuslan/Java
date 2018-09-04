package com.saleoa.service;

import java.util.Map;

import com.saleoa.base.IBaseService;
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Sale;

public interface ISaleService extends IBaseService<Sale> {

	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
	
	public BalanceLevel getBalanceLevelByEmployeeId(Long employeeId);
	
	public long getMinSaleCount(Long employeeId, Map<String, Object> paramMap);
}
