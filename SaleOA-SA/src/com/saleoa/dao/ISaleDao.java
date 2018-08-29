package com.saleoa.dao;

import java.util.Date;

import com.saleoa.base.IBaseDao;
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
}
