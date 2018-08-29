package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleSalary;

public class ISaleSalaryDaoImpl extends IBaseDaoImpl<SaleSalary> implements ISaleSalaryDao {
	
	public SaleSalary getInstance(Sale sale) {
		SaleSalary saleLog = new SaleSalary();
		saleLog.setCreateDate(new Date());
		saleLog.setEmployeeId(sale.getEmployeeId());
		saleLog.setEmployeeName(sale.getEmployeeName());
		saleLog.setSalary(0L);
		saleLog.setSaleId(sale.getId());
		saleLog.setUpdateDate(new Date());
		return saleLog;
	}
	
	
	public Page<SaleSalary> selectPage(Map<String, Object> paramMap, long pageNo, int limit) {
		Page<SaleSalary> pageObj = null;
		if(null == paramMap.get("createDate>=")) {
			paramMap.put("createDate>=", "'"+DateUtil.getFirstDateStrOfMonthByDate(new Date())+"'");
		}
		if(null == paramMap.get("createDate<=")) {
			paramMap.put("createDate<=", "'"+DateUtil.getEndDateStrOfMonthByDate(new Date())+"'");
		}
		try {
			String sql = "SELECT employee_id, employee_name, SUM(salary) salary FROM tbl_oa_sale_salary";
			String condition = "";
			if(null != paramMap.get("createDate>=")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "create_date>="+paramMap.get("createDate>=").toString();
			}
			if(null != paramMap.get("createDate<=")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "create_date<="+paramMap.get("createDate<=").toString();
			}
			if(null != paramMap.get("employeeId")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "employee_id="+paramMap.get("employeeId");
			}
			sql = sql+(StringUtil.isEmpty(condition)?"":" WHERE ")+condition;
			sql += " GROUP BY employee_id, employee_name LIMIT "+(pageNo-1)*limit+", "+limit;
			System.out.println(sql);
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<SaleSalary> list = new ArrayList<SaleSalary> ();
			while(rs.next()) {
				SaleSalary saleSalary = new SaleSalary();
				saleSalary.setEmployeeId(rs.getLong(1));
				saleSalary.setEmployeeName(rs.getString(2));
				saleSalary.setSalary(rs.getLong(3));
				list.add(saleSalary);
			}
			String countSql = "SELECT COUNT(*) FROM tbl_oa_sale_salary "+(StringUtil.isEmpty(condition)?"":" WHERE ")+condition+" GROUP BY employee_id, employee_name";
			ps = JdbcHelper.getConnection().prepareStatement(countSql);
			rs = ps.executeQuery();
			Long count = 0l;
			if(rs.next()) {
				count = rs.getLong(1);
			}
			long totalPage = count % limit == 0 ? count / limit : (int)(count / limit) + 1;
			pageObj = new Page<SaleSalary> (pageNo, totalPage, limit, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageObj;
	}
	
	/**
	 * 通过员工id和销售时间查询销售时间内的奖金
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleSalary selectByEmployeeOnSaleDate(Long employeeId, Date saleDate) {
		SaleSalary log = null;
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("employeeId", employeeId);
		paramMap.put("createDate>=", DateUtil.getFirstDateStrOfMonthByDate(saleDate));
		paramMap.put("createDate<=", DateUtil.getEndDateStrOfMonthByDate(saleDate));
		List<SaleSalary> logs = this.select(paramMap);
		if(logs.size() > 0) {
			log = logs.get(0);
		}
		return log;
	}
	
}
