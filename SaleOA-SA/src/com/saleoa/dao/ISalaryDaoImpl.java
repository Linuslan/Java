package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Salary;
import com.saleoa.model.SalaryConfig;
import com.saleoa.service.ISalaryConfigService;
import com.saleoa.service.ISalaryConfigServiceImpl;

public class ISalaryDaoImpl extends IBaseDaoImpl<Salary> implements ISalaryDao {
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public List<Salary> createSalary(int year, int month) throws Exception {
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
		int startDay = salaryConfig.getSalaryStartDay();
		int endDay = salaryConfig.getSalaryEndDay();
		String monthStr = month>9?month+"":"0"+month;
		String startTime = year+"-"+monthStr+"-"+(startDay>9 ? startDay : "0"+startDay)+" 00:00:00";
		String endTime = year+"-"+monthStr+"-"+(endDay>9 ? endDay : "0"+endDay)+" 23:59:59";
		boolean success = false;
		String sql = "SELECT t.employee_id, t.employee_name, SUM(salary) salary FROM tbl_oa_sale_salary t WHERE t.create_date >='"+startTime+"'" +
				" AND t.create_date <= '"+endTime+"' GROUP BY t.employee_id, t.employee_name";
		List<Salary> salaryList = new ArrayList<Salary>();
		PreparedStatement ps = null;
		try {
			ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Long employeeId = rs.getLong("employee_id");
				String employeeName = rs.getString("employee_name");
				Long salary = rs.getLong("salary");
				Salary salaryObj = new Salary();
				salaryObj.setCreateDate(new Date());
				salaryObj.setYear(year);
				salaryObj.setMoney(salary);
				salaryObj.setTotalMoney(salary);
				salaryObj.setDeductMoney(0l);
				salaryObj.setIsDelete(0);
				salaryObj.setMonth(month);
				salaryObj.setStatus(0);
				salaryObj.setUpdateDate(new Date());
				salaryObj.setUserId(employeeId);
				salaryObj.setUserName(employeeName);
				salaryObj.setMemo("");
				salaryObj.setAmercement(0L);
				salaryObj.setCompanyLend(0L);
				salaryObj.setFullDutyBonus(0L);
				salaryObj.setOfficeManageBonus(0L);
				salaryObj.setOverGoalBonus(0L);
				salaryObj.setReachGoalBonus(0L);
				salaryObj.setSupposedMoney(0L);
				salaryObj.setTax(0L);
				salaryObj.setTotalReachGoalBonus(0L);
				salaryList.add(salaryObj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salaryList;
	}
	
}
