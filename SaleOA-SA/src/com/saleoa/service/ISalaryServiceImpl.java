package com.saleoa.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.constant.EmployeeRoleConst;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.dao.IDepartmentDao;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.IEmployeeDao;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.ISalaryDao;
import com.saleoa.dao.ISalaryDaoImpl;
import com.saleoa.dao.ISaleDao;
import com.saleoa.model.Employee;
import com.saleoa.model.Salary;

public class ISalaryServiceImpl extends IBaseServiceImpl<Salary> implements
		ISalaryService {
	private ISalaryDao salaryDao;
	private IDepartmentDao departmentDao;
	private IEmployeeDao employeeDao;
	private ISaleDao saleDao;
	private IManagerLevelDao managerLevelDao;
	public ISalaryServiceImpl() {
		salaryDao = new ISalaryDaoImpl();
		this.dao = salaryDao;
		departmentDao = new IDepartmentDaoImpl();
		employeeDao = new IEmployeeDaoImpl();
	}
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Salary> createSalary(int year, int month) {
		List<Salary> salaryList = this.salaryDao.createSalary(year, month);
		Salary salary = null;
		Map<Long, List<Salary>> departmentSalaryMap = new HashMap<Long, List<Salary>> ();
		for(int i = 0; i < salaryList.size(); i ++) {
			salary = salaryList.get(i);
			Employee employee = this.employeeDao.selectById(salary.getUserId());
			Long departmentId = employee.getDepartmentId();
			List<Salary> deptSalarys = departmentSalaryMap.get(departmentId.longValue());
			if(null == deptSalarys) {
				deptSalarys = new ArrayList<Salary> ();
				departmentSalaryMap.put(departmentId.longValue(), deptSalarys);
			}
			deptSalarys.add(salary);
			
			//如果是高管，则工资的算法不同
			if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.EMPLOYEE.longValue()) {
				continue;
			}
		}
		Iterator<Entry<Long, List<Salary>>> iter = departmentSalaryMap.entrySet().iterator();
		Entry<Long, List<Salary>> entry = null;
		while(iter.hasNext()) {
			entry = iter.next();
			Long departmentId = entry.getKey();
			List<Salary> salarys = entry.getValue();
			String dateStr = year+"-"+(month > 9 ? month : "0"+month)+"-13 00:00:00";
			int saleCount = this.saleDao.getSaleCountByDepartment(departmentId, DateUtil.parseFullDate(dateStr));
			List<Employee> managers = this.employeeDao.selectManagerByDepartment(departmentId);
			for(int i = 0; i < managers.size(); i ++) {
				Salary mngSalary = new Salary();
				
			}
		}
		return this.salaryDao.createSalary(year, month);
	}
}
