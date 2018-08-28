package com.saleoa.service;

import java.util.Date;

import com.saleoa.base.IBaseService;
import com.saleoa.model.SaleLog;

public interface ISaleLogService extends IBaseService<SaleLog> {
	
	/**
	 * 通过员工id和销售时间查询销售时间内的奖金
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
	
}
