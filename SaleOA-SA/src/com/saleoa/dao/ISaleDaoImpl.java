package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.model.Sale;

public class ISaleDaoImpl extends IBaseDaoImpl<Sale> implements ISaleDao {
	
	public Page<Sale> selectPage(Map<String, Object> paramMap) {
		Page<Sale> page = null;
		String sql = "SELECT t.* FROM tbl_oa_sale t LEFT JOIN tbl_oa_sale_log t1 ON t.id = t1.sale_id";
		return page;
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
