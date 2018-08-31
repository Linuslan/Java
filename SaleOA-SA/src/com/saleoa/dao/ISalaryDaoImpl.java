package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
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
		String startTime = DateUtil.getCustomStartDateStr(year, month);
		String endTime = DateUtil.getCustomEndDateStr(year, month);
		boolean success = false;
		String sql = "SELECT t.employee_id, t.employee_name, SUM(bonus) salary FROM tbl_oa_sale_log t WHERE t.sale_date >='"+startTime+"'" +
				" AND t.sale_date <= '"+endTime+"' GROUP BY t.employee_id, t.employee_name";
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
	
	/**
	 * 批量审核
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public boolean auditBatch(List<Long> idList) throws Exception {
		String ids = BeanUtil.parseLongListToString(idList, ",");
		String sql = "UPDATE tbl_oa_salary SET status = 1 WHERE id IN ("+ids+")";
		return JdbcHelper.executeSql(sql);
	}
	
	/**
	 * 通过年月查询工资
	 * @param year
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public List<Salary> queryByYearAndMonth(int year, int month) throws Exception {
		String sql = "SELECT * FROM tbl_oa_salary WHERE year="+year+" AND month="+month;
		List<Salary> salaryList = JdbcHelper.select(sql, Salary.class);
		return salaryList;
	}
	
}
