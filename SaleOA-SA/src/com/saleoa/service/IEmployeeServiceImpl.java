package com.saleoa.service;

import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.dao.IEmployeeDao;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Employee;

public class IEmployeeServiceImpl extends IBaseServiceImpl<Employee> implements
		IEmployeeService {
	private ILevelDao levelDao;
	private IEmployeeDao employeeDao;
	public IEmployeeServiceImpl() {
		this.employeeDao = new IEmployeeDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = this.employeeDao;
	}
	
	/**
	 * 查询部门的领导人
	 * @param departmentId
	 * @return
	 */
	public List<Employee> selectManagerByDepartment(Long departmentId) {
		return this.employeeDao.selectManagerByDepartment(departmentId);
	}
	
	public boolean add(Employee employee) throws Exception {
		boolean success = false;
		employee.setIsDelete(0);
		employee.setCreateDate(new Date());
		employee.setUpdateDate(new Date());
		if(null == employee.getLeaderId()) {
			employee.setLeaderId(0L);
			employee.setLeaderName("");
		}
		
		this.dao.add(employee);
		success = true;
		return success;
	}
}
