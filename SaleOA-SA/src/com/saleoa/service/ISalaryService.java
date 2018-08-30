package com.saleoa.service;

import java.util.List;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Salary;

public interface ISalaryService extends IBaseService<Salary> {
	
	/**
	 * ��������
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Salary> createSalary(int year, int month);
	
}
