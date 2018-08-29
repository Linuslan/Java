package com.saleoa.service;

import java.util.List;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Employee;

public interface IEmployeeService extends IBaseService<Employee> {

	/**
	 * 查询部门的领导人
	 * @param departmentId
	 * @return
	 */
	public List<Employee> selectManagerByDepartment(Long departmentId);
	
}
