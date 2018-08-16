package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IEmployeeDao;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.model.Employee;

public class IEmployeeServiceImpl extends IBaseServiceImpl<Employee> implements
		IEmployeeService {
	public IEmployeeServiceImpl() {
		this.dao = new IEmployeeDaoImpl();
	}
}
