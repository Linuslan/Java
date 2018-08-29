package com.saleoa.dao;

import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseDao;
import com.saleoa.common.plugin.Page;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleSalary;

public interface ISaleSalaryDao extends IBaseDao<SaleSalary> {

	public SaleSalary getInstance(Sale sale);
	
	public Page<SaleSalary> selectPage(Map<String, Object> paramMap, long pageNo, int limit);
	
	/**
	 * 通过员工id和销售时间查询销售时间内的奖金
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleSalary selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
}
