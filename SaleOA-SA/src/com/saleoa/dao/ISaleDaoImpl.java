package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Sale;

public class ISaleDaoImpl extends IBaseDaoImpl<Sale> implements ISaleDao {
	
	/**
	 * 查询部门在销售月内的销售总数
	 * @param departmentId
	 * @param saleDate
	 * @return
	 */
	public int getSaleCountByDepartment(Long departmentId, Date saleDate) {
		int count = 0;
		String startDate = DateUtil.getFirstDateStrOfMonthByDate(saleDate);
		String endDate = DateUtil.getEndDateStrOfMonthByDate(saleDate);
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
