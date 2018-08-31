package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleSalary;

public class ISaleDaoImpl extends IBaseDaoImpl<Sale> implements ISaleDao {
	
	public Page<Sale> selectPage(Map<String, Object> paramMap, long pageNo, int limit) {
		Page<Sale> pageObj = null;
		if(null == paramMap.get("saleDate>=")) {
			paramMap.put("saleDate>=", "'"+DateUtil.getCustomFirstDateStrOfMonthByDate(new Date())+"'");
		}
		if(null == paramMap.get("saleDate<=")) {
			paramMap.put("saleDate<=", "'"+DateUtil.getCustomEndDateStrOfMonthByDate(new Date())+"'");
		}
		try {
			//String sql = "SELECT t.*, SUM(t1.bonus) FROM tbl_oa_sale t LEFT JOIN tbl_oa_sale_log t1 ON t.id=t1.sale_id";
			String sql1 = "SELECT * FROM tbl_oa_sale";
			String sql2 = "SELECT sale_id, SUM(bonus) salary FROM tbl_oa_sale_log";
			String condition = "";
			if(null != paramMap.get("saleDate>=")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "sale_date>="+paramMap.get("saleDate>=").toString();
			}
			if(null != paramMap.get("saleDate<=")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "sale_date<="+paramMap.get("saleDate<=").toString();
			}
			if(null != paramMap.get("employeeId")) {
				if(!StringUtil.isEmpty(condition)) {
					condition += " AND ";
				}
				condition += "employee_id="+paramMap.get("employeeId");
			}
			sql1 = sql1+(StringUtil.isEmpty(condition)?"":" WHERE ")+condition;
			sql2 = sql2+(StringUtil.isEmpty(condition)?"":" WHERE ")+condition+" GROUP BY sale_id";
			String selectColumn = "SELECT t.id, t.name, t.name_en," +
					" t.sale_no, t.employee_id, t.employee_name," +
					" t.last_sale_id, t.last_sale_name, t.sale_date," +
					" t.create_date, t.update_date, t.is_delete," +
					" t.reward_points, t.level_id, t.level_name," +
					" t.department_id, t.department_name, t1.salary";
			String selectWhere = " FROM ("+sql1+") t LEFT JOIN ("+sql2+") t1 ON t.id=t1.sale_id";
			String sql = selectColumn +
					selectWhere + " LIMIT "+(pageNo-1)*limit+", "+limit;
			System.out.println(sql);
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Sale> list = new ArrayList<Sale> ();
			while(rs.next()) {
				Sale sale = new Sale();
				sale.setId(rs.getLong("id"));
				sale.setName(rs.getString("name"));
				sale.setNameEn(rs.getString("name_en"));
				sale.setSaleNo(rs.getInt("sale_no"));
				sale.setEmployeeId(rs.getLong("employee_id"));
				sale.setEmployeeName(rs.getString("employee_name"));
				sale.setLastSaleId(rs.getLong("last_sale_id"));
				sale.setLastSaleName(rs.getString("last_sale_name"));
				sale.setSaleDate(DateUtil.parseFullDate(rs.getString("sale_date")));
				sale.setCreateDate(DateUtil.parseFullDate(rs.getString("create_date")));
				sale.setUpdateDate(DateUtil.parseFullDate(rs.getString("update_date")));
				sale.setIsDelete(rs.getInt("is_delete"));
				sale.setRewardPoints(rs.getLong("reward_points"));
				sale.setLevelId(rs.getLong("level_id"));
				sale.setLevelName(rs.getString("level_name"));
				sale.setDepartmentId(rs.getLong("department_id"));
				sale.setDepartmentName(rs.getString("department_name"));
				sale.setSalary(rs.getLong("salary"));
				list.add(sale);
			}
			String countSql = "SELECT COUNT(*) "+selectWhere;
			ps = JdbcHelper.getConnection().prepareStatement(countSql);
			rs = ps.executeQuery();
			Long count = 0l;
			if(rs.next()) {
				count = rs.getLong(1);
			}
			long totalPage = count % limit == 0 ? count / limit : (int)(count / limit) + 1;
			
			String sumSQL = "SELECT SUM(bonus) salary FROM tbl_oa_sale_log"+(StringUtil.isEmpty(condition)?"":" WHERE ")+condition;
			ps = JdbcHelper.getConnection().prepareStatement(sumSQL);
			rs = ps.executeQuery();
			Long sum = 0l;
			if(rs.next()) {
				sum = rs.getLong(1);
			}
			Sale sale = new Sale();
			sale.setName("汇总");
			sale.setSalary(sum);
			list.add(sale);
			pageObj = new Page<Sale> (pageNo, totalPage, limit, list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageObj;
	}
	
	/**
	 * 查询部门在销售月内的销售总数
	 * @param departmentId
	 * @param saleDate
	 * @return
	 */
	public int getSaleCountByDepartment(Long departmentId, Date saleDate) {
		int count = 0;
		String startDate = DateUtil.getCustomFirstDateStrOfMonthByDate(saleDate);
		String endDate = DateUtil.getCustomEndDateStrOfMonthByDate(saleDate);
		String sql = "SELECT COUNT(*) FROM tbl_oa_sale WHERE sale_date >= '"+startDate+"'" +
				" AND sale_date <= '"+endDate+"' AND department_id="+departmentId;
		try {
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id) {
		Long number = 0L;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT MAX(sale_no) saleNo FROM tbl_oa_sale WHERE employee_id=?";
			ps = JdbcHelper.getConnection().prepareStatement(sql);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				number = rs.getLong("saleNo");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			JdbcHelper.close(rs, ps, null);
		}
		return number;
	}
	
}
