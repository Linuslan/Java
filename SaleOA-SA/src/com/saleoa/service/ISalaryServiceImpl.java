package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.ISalaryDao;
import com.saleoa.dao.ISalaryDaoImpl;
import com.saleoa.model.Salary;

public class ISalaryServiceImpl extends IBaseServiceImpl<Salary> implements
		ISalaryService {
	private ISalaryDao salaryDao;
	public ISalaryServiceImpl() {
		salaryDao = new ISalaryDaoImpl();
		this.dao = salaryDao;
	}
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month) {
		return this.salaryDao.createSalary(year, month);
	}
}
