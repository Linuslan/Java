package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Salary;

public interface ISalaryDao extends IBaseDao<Salary> {
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month);
	
}
