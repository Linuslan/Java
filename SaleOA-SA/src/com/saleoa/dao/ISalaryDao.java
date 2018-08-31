package com.saleoa.dao;

import java.util.List;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Salary;

public interface ISalaryDao extends IBaseDao<Salary> {
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Salary> createSalary(int year, int month) throws Exception;
	
	/**
	 * 批量审核
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public boolean auditBatch(List<Long> idList) throws Exception;
	
	/**
	 * 通过年月查询工资
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public List<Salary> queryByYearAndMonth(int year, int month) throws Exception;
	
}
