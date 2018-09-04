package com.saleoa.dao;

import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Sale;

public interface ISaleDao extends IBaseDao<Sale> {
	
	/**
	 * 查询部门在销售月内的销售总数
	 * @param departmentId
	 * @param saleDate
	 * @return
	 */
	public int getSaleCountByDepartment(Long departmentId, Date saleDate);

	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
	
	/**
	 * 通过员工id查询员工的第一个售出
	 * @param employeeId
	 * @return
	 */
	public Sale selectFirstSaleByEmployeeId(Long employeeId);
	
	//查询经理
	public int selectManagerCountBySale(Sale sale);
	
	/**
	 * 
	 * @param sale
	 * @return
	 */
	public long selectSaleCountBySale(Map<String, Object> paramMap);
	
	public BalanceLevel getBalanceLevelByEmployeeId(Long employeeId);
	
	public long getMinSaleCount(Long employeeId, Map<String, Object> paramMap);
}
