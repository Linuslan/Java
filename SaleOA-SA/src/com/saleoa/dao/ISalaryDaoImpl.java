package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Salary;

public class ISalaryDaoImpl extends IBaseDaoImpl<Salary> implements ISalaryDao {
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month) {
		String monthStr = month>9?month+"":"0"+month;
		String startTime = year+"-"+monthStr+"-01 00:00:00";
		String endTime = year+"-"+monthStr+"-31 23:59:59";
		boolean success = false;
		String sql = "SELECT t.employee_id, t.employee_name, SUM(salary) salary FROM tbl_oa_sale t WHERE t.sale_date >='"+startTime+"'" +
				" AND t.sale_date <= '"+endTime+"' GROUP BY t.employee_id, t.employee_name";
		List<Salary> salaryList = new ArrayList<Salary>();
		PreparedStatement ps = null;
		try {
			ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			Long id = JdbcHelper.id(Salary.class);
			while(rs.next()) {
				Long employeeId = rs.getLong("employee_id");
				String employeeName = rs.getString("employee_name");
				Long salary = rs.getLong("salary");
				Salary salaryObj = new Salary();
				salaryObj.setId(id);
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
				salaryList.add(salaryObj);
				id++;
			}
			JdbcHelper.insertBatch(salaryList);
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
}
