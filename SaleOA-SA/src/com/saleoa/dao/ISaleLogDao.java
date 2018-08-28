package com.saleoa.dao;

import java.util.Date;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

public interface ISaleLogDao extends IBaseDao<SaleLog> {

	public SaleLog getInstance(Sale sale);
	
	/**
	 * 通过员工id和销售时间查询销售时间内的奖金
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
}
