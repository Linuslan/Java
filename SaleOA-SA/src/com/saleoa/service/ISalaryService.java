package com.saleoa.service;

import java.util.List;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Salary;

public interface ISalaryService extends IBaseService<Salary> {
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month) throws Exception;
	
	public Long getTax(Long supposedMoney);
	
	/**
	 * 批量审核
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public boolean auditBatch(List<Long> idList) throws Exception;
	
}
