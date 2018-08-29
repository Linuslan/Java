package com.saleoa.service;

import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseService;
import com.saleoa.common.plugin.Page;
import com.saleoa.model.SaleSalary;

public interface ISaleSalaryService extends IBaseService<SaleSalary> {
	
	public Page<SaleSalary> selectPage(Map<String, Object> paramMap, long pageNo, int limit);
	
	/**
	 * ͨ��Ա��id������ʱ���ѯ����ʱ���ڵĽ���
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleSalary selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
	
}
