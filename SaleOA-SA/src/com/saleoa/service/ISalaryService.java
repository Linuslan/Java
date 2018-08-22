package com.saleoa.service;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Salary;

public interface ISalaryService extends IBaseService<Salary> {
	
	/**
	 * ��������
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month);
	
}
