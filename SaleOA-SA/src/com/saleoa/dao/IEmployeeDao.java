package com.saleoa.dao;

import java.util.List;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Employee;

public interface IEmployeeDao extends IBaseDao<Employee> {

	/**
	 * 查询部门的领导人
	 * @param departmentId
	 * @return
	 */
	public List<Employee> selectManagerByDepartment(Long departmentId);
	
}
