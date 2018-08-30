package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.saleoa.dao.ISalaryConfigDao;
import com.saleoa.dao.ISalaryConfigDaoImpl;
import com.saleoa.dao.ISalaryDao;
import com.saleoa.dao.ISalaryDaoImpl;
import com.saleoa.dao.ISaleDao;
import com.saleoa.model.Department;
import com.saleoa.model.Employee;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Salary;
import com.saleoa.model.SalaryConfig;

public class ISalaryServiceImpl extends IBaseServiceImpl<Salary> implements
		ISalaryService {
	private ISalaryDao salaryDao;
	private IDepartmentDao departmentDao;
	private IEmployeeDao employeeDao;
	private ISaleDao saleDao;
	private IManagerLevelDao managerLevelDao;
	private ISalaryConfigDao salaryConfigDao;
	private SalaryConfig salaryConfig;
	public ISalaryServiceImpl() {
		salaryDao = new ISalaryDaoImpl();
		this.dao = salaryDao;
		departmentDao = new IDepartmentDaoImpl();
		employeeDao = new IEmployeeDaoImpl();
		salaryConfigDao = new ISalaryConfigDaoImpl();
	}
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month) throws Exception {
		List<Salary> salaryList = this.salaryDao.createSalary(year, month);
		Salary salary = null;
		Map<Long, List<Salary>> departmentSalaryMap = new HashMap<Long, List<Salary>> ();
		for(int i = 0; i < salaryList.size(); i ++) {
			salary = salaryList.get(i);
			Employee employee = this.employeeDao.selectById(salary.getUserId());
			Long departmentId = employee.getDepartmentId();
			salary.setDepartmentId(departmentId);
			salary.setDepartmentName(employee.getDepartmentName());
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
		//超额达标数，如果超额达标数等于部门数量，则每个经理都有总达标奖
		int overGoalCount = 0;
		salaryConfig = this.salaryConfigDao.selectById(1L);
		Map<Long, List<Salary>> mngSalaryMap = new HashMap<Long, List<Salary>> ();
		while(iter.hasNext()) {
			entry = iter.next();
			Long departmentId = entry.getKey();
			List<Salary> salarys = entry.getValue();
			String dateStr = year+"-"+(month > 9 ? month : "0"+month)+"-"+salaryConfig.getSalaryStartDay()+" 00:00:00";
			int saleCount = this.saleDao.getSaleCountByDepartment(departmentId, DateUtil.parseFullDate(dateStr));
			List<Employee> managers = this.employeeDao.selectManagerByDepartment(departmentId);
			for(int i = 0; i < managers.size(); i ++) {
				Employee employee = managers.get(i);
				Salary mngSalary = new Salary();
				ManagerLevel managerLevel = this.managerLevelDao.selectBySale(saleCount);
				mngSalary.setCreateDate(new Date());
				mngSalary.setYear(year);
				mngSalary.setMonth(month);
				mngSalary.setAmercement(0l);
				mngSalary.setCompanyLend(0l);
				mngSalary.setDeductMoney(0l);
				mngSalary.setDepartmentId(employee.getDepartmentId());
				mngSalary.setDepartmentName(employee.getDepartmentName());
				mngSalary.setFullDutyBonus(0l);
				mngSalary.setIsDelete(0);
				mngSalary.setMemo("");
				mngSalary.setMoney(managerLevel.getBasicSalary());
				//销售数大于最大的销售数，则有超额达标奖
				if(saleCount > managerLevel.getMaxSale()) {
					mngSalary.setReachGoalBonus(managerLevel.getReachGoalBonus());
					overGoalCount ++;
					mngSalary.setOverGoalBonus(managerLevel.getCommission()*saleCount);
				}
				mngSalary.setOfficeManageBonus(30000l);
				List<Salary> mngSalarys = mngSalaryMap.get(departmentId.longValue());
				if(null == mngSalarys) {
					mngSalarys = new ArrayList<Salary> ();
					mngSalaryMap.put(departmentId.longValue(), mngSalarys);
				}
				mngSalarys.add(mngSalary);
			}
		}
		//总达标奖
		if(overGoalCount >= departmentSalaryMap.size()) {
			Iterator<Entry<Long, List<Salary>>> mngIter = mngSalaryMap.entrySet().iterator();
			Entry<Long, List<Salary>> mngEntry = null;
			while(mngIter.hasNext()) {
				mngEntry = mngIter.next();
				Long deptId = mngEntry.getKey();
				List<Salary> salarys = mngEntry.getValue();
				for(int i = 0; i < salarys.size(); i ++) {
					Salary mngSalary = salarys.get(i);
					mngSalary.setTotalReachGoalBonus(salaryConfig.getTotalReachGoalBonus());
					departmentSalaryMap.get(deptId.longValue()).add(0, mngSalary);
				}
			}
		}
		
		//计算税额以及应发和最终工资
		List<Salary> salarys = new ArrayList<Salary> ();
		Iterator<Entry<Long, List<Salary>>> deptSalaryIter = departmentSalaryMap.entrySet().iterator();
		Entry<Long, List<Salary>> deptSalaryEntry = null;
		while(deptSalaryIter.hasNext()) {
			deptSalaryEntry = deptSalaryIter.next();
			List<Salary> deptSalarys = deptSalaryEntry.getValue();
			Salary sal = null;
			for(int i = 0; i < deptSalarys.size(); i ++) {
				sal = deptSalarys.get(i);
				sal.setSupposedMoney(getSupposedMoney(sal));
				Employee employee = this.employeeDao.selectById(sal.getUserId());
				if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.MANAGER.longValue()) {
					Long tax = getTax(salary.getSupposedMoney());
					sal.setTax(tax);
				}
				Long totalMoney = getTotalSalary(sal);
				sal.setTotalMoney(totalMoney);
				salarys.add(sal);
			}
		}
		return this.salaryDao.addBatch(salarys);
	}
	
	private Long getSupposedMoney(Salary salary) {
		return salary.getMoney() + salary.getReachGoalBonus() + salary.getOverGoalBonus() + salary.getOfficeManageBonus() + salary.getFullDutyBonus() + salary.getTotalReachGoalBonus()
				-salary.getDeductMoney();
	}
	
	public Long getTax(Long supposedMoney) {
		if(null == salaryConfig) {
			this.salaryConfig = this.salaryConfigDao.selectById(1L);
		}
		Long tax = 0L;
		Long taxThreshold = salaryConfig.getTaxThreshold();
		int taxRate = salaryConfig.getTaxRate();
		Long taxMoney = supposedMoney - taxThreshold;
		tax = taxMoney*taxRate/100;
		return tax;
	}
	
	private Long getTotalSalary(Salary salary) {
		return salary.getSupposedMoney()
				- salary.getAmercement() - salary.getCompanyLend() - salary.getTax();
	}
}
