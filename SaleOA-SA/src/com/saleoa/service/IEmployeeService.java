package com.saleoa.service;

import java.util.List;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Employee;

public interface IEmployeeService extends IBaseService<Employee> {

	/**
	 * ��ѯ���ŵ��쵼��
	 * @param departmentId
	 * @return
	 */
	public List<Employee> selectManagerByDepartment(Long departmentId);
	
}
