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
	
}
