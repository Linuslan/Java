package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Sale;

public class ISaleDaoImpl extends IBaseDaoImpl<Sale> implements ISaleDao {
	
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
