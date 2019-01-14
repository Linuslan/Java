package com.saleoa.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.cache.DataCache;
import com.saleoa.common.constant.EmployeeRoleConst;
import com.saleoa.common.plugin.Page;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Level;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleSalary;

public class ISaleDaoImpl extends IBaseDaoImpl<Sale> implements ISaleDao {
	
	private IBalanceLevelDao balanceLevelDao = new IBalanceLevelDaoImpl();
	
	public Page<Sale> selectPage(Map<String, Object> paramMap, long pageNo, int limit) {
		Page<Sale> pageObj = null;
		if(null == paramMap.get("saleDate>=")) {
			paramMap.put("saleDate>=", "'"+DateUtil.getCustomFirstDateStrOfMonthByDate(new Date())+"'");
		}
		if(null == paramMap.get("saleDate<=")) {
			paramMap.put("saleDate<=", "'"+DateUtil.getCustomEndDateStrOfMonthByDate(new Date())+"'");
		}
		int searchType = 0;	//查询维度，0：查询本人的销售业绩；1：查询总的销售业绩
		try {
			//String sql = "SELECT t.*, SUM(t1.bonus) FROM tbl_oa_sale t LEFT JOIN tbl_oa_sale_log t1 ON t.id=t1.sale_id";
			if(null != paramMap.get("searchType")) {
				searchType = (Integer) paramMap.get("searchType");
			}
			String sql1 = "SELECT * FROM "+(searchType == 0 ? "tbl_oa_sale" : "sale");
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
			
			Long employeeId = 1L;
			if(null != paramMap.get("employeeId")) {
				if(searchType == 0) {
					if(!StringUtil.isEmpty(condition)) {
						condition += " AND ";
					}
					condition += "employee_id="+paramMap.get("employeeId");
				}
				employeeId = (Long) paramMap.get("employeeId");
			}
			String withSql = "WITH RECURSIVE sale AS(SELECT * FROM (SELECT * FROM tbl_oa_sale t WHERE t.employee_id="+employeeId+" ORDER BY t.id ASC LIMIT 1) t UNION SELECT t2.* FROM sale t1 join tbl_oa_sale t2 ON t1.id=t2.last_sale_id)";
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
					selectWhere;
			
			if(searchType != 0) {
				sql = withSql + " "+sql;
			}
			System.out.println(sql);
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql+ " LIMIT "+(pageNo-1)*limit+", "+limit);
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
			try {
				Sale sale = null;
				for(int i = 0; i < list.size(); i ++) {
					sale = list.get(i);
					if(0 >= sale.getLastSaleId().longValue()) {
						continue;
					}
					String pKey = getPKey() + sale.getLastSaleId().longValue();
					String cKey = getKey() + sale.getId();
					if(null == DataCache.treeGet(pKey)
							|| null == DataCache.treeGetChild(pKey, cKey)) {
						DataCache.treePush(pKey, cKey, sale);
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			String countSql = "SELECT COUNT(*) FROM ("+sql+")";
			ps = JdbcHelper.getConnection().prepareStatement(countSql);
			rs = ps.executeQuery();
			Long count = 0l;
			if(rs.next()) {
				count = rs.getLong(1);
			}
			long totalPage = count % limit == 0 ? count / limit : (int)(count / limit) + 1;
			
			String sumSQL = "SELECT SUM(salary) salary FROM ("+sql+")";
			ps = JdbcHelper.getConnection().prepareStatement(sumSQL);
			rs = ps.executeQuery();
			Long sum = 0l;
			if(rs.next()) {
				sum = rs.getLong(1);
			}
			Sale sale = new Sale();
			sale.setName("汇总");
			sale.setSalary(sum);
			sale.setLastSaleName("销售总数：");
			sale.setRewardPoints(count);
			list.add(sale);
			pageObj = new Page<Sale> (pageNo, totalPage, limit, list);
			pageObj.setTotalCount(count);
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
		Date startDate = DateUtil.getCustomFirstDateOfMonthByDate(saleDate);
		Date endDate = DateUtil.getCustomEndDateOfMonthByDate(saleDate);
		List<Sale> allSales = this.selectCacheAll();
		Iterator<Sale> iter = allSales.iterator();
		while(iter.hasNext()) {
			Sale sale = iter.next();
			if(sale.getDepartmentId().longValue() != departmentId.longValue()) {
				continue;
			}
			if(sale.getSaleDate().before(startDate)) {
				continue;
			}
			if(sale.getSaleDate().after(endDate)) {
				continue;
			}
			count ++;
		}
		//String startDate = DateUtil.getCustomFirstDateStrOfMonthByDate(saleDate);
		//String endDate = DateUtil.getCustomEndDateStrOfMonthByDate(saleDate);
		/*String sql = "SELECT COUNT(*) FROM tbl_oa_sale WHERE sale_date >= '"+startDate+"'" +
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
		}*/
		return count;
	}
	
	/**
	 * 根据员工id查找员工的销售记录
	 * @param employeeId
	 * @return
	 */
	public List<Sale> selectByEmployeeId(Long employeeId) {
		List<Sale> sales = new ArrayList<Sale>();
		String key = this.getKey();
		List<Sale> list = (List<Sale>) DataCache.selectAll(key);
		if(list.size() > 0) {
			for(int i = 0; i < list.size(); i ++) {
				Sale temp = list.get(i);
				if(temp.getEmployeeId().longValue() != employeeId) {
					continue;
				}
				sales.add(temp);
			}
		}
		if(sales.size() <= 0) {
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("employeeId", employeeId);
			paramMap.put("orderby", " ORDER BY id DESC");
			try {
				sales = this.select(paramMap);
				if(null == sales) {
					sales = new ArrayList<Sale> ();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sales = new ArrayList<Sale>();
			}
		}
		return sales;
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
	
	public Sale selectById(Long id) {
		Sale sale = super.selectById(id);
		if(0 >= sale.getLastSaleId().longValue()) {
			return sale;
		}
		String pKey = getPKey() + sale.getLastSaleId().longValue();
		String cKey = getKey() + sale.getId();
		if(null == DataCache.treeGet(pKey)
				|| null == DataCache.treeGetChild(pKey, cKey)) {
			DataCache.treePush(pKey, cKey, sale);
		}
		return sale;
	}
	
	public List<Sale> select(Map<String, Object> paramMap) {
		List<Sale> list = super.select(paramMap);
		Sale sale = null;
		for(int i = 0; i < list.size(); i ++) {
			sale = list.get(i);
			if(0 >= sale.getLastSaleId().longValue()) {
				continue;
			}
			String pKey = getPKey() + sale.getLastSaleId().longValue();
			String cKey = getKey() + sale.getId();
			if(null == DataCache.treeGet(pKey)
					|| null == DataCache.treeGetChild(pKey, cKey)) {
				DataCache.treePush(pKey, cKey, sale);
			}
		}
		return list;
	}
	
	public boolean add(Sale sale) {
		boolean success = false;
		try {
			super.add(sale);
			if(0 >= sale.getLastSaleId().longValue()) {
				return true;
			}
			String pKey = getPKey() + sale.getLastSaleId().longValue();
			String cKey = getKey() + sale.getId();
			DataCache.treePush(pKey, cKey, sale);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public boolean addBatch(List<Sale> list) {
		boolean success = false;
		try {
			super.addBatch(list);
			Sale sale = null;
			for(int i = 0; i < list.size(); i ++) {
				sale = list.get(i);
				if(0 >= sale.getLastSaleId().longValue()) {
					continue;
				}
				String pKey = getPKey() + sale.getLastSaleId().longValue();
				String cKey = getKey() + sale.getId();
				DataCache.treePush(pKey, cKey, sale);
			}
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public boolean update(Sale sale) {
		boolean success = false;
		try {
			super.update(sale);
			if(0 >= sale.getLastSaleId().longValue()) {
				return true;
			}
			String pKey = getPKey() + sale.getLastSaleId().longValue();
			String cKey = getKey() + sale.getId();
			DataCache.treePush(pKey, cKey, sale);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public boolean updateBatch(List<Sale> list) {
		boolean success = false;
		try {
			super.updateBatch(list);
			Sale sale = null;
			for(int i = 0; i < list.size(); i ++) {
				sale = list.get(i);
				if(0 >= sale.getLastSaleId().longValue()) {
					continue;
				}
				String pKey = getPKey() + sale.getLastSaleId().longValue();
				String cKey = getKey() + sale.getId();
				DataCache.treePush(pKey, cKey, sale);
			}
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	public boolean delete(Sale sale) {
		boolean success = false;
		try {
			super.delete(sale);
			if(0 >= sale.getLastSaleId().longValue()) {
				return true;
			}
			String pKey = getPKey() + sale.getLastSaleId().longValue();
			String cKey = getKey() + sale.getId();
			DataCache.treeRemoveChild(pKey, cKey);
			success = true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return success;
	}
	
	/**
	 * 通过员工id查询员工的第一个售出
	 * @param employeeId
	 * @return
	 */
	public Sale selectFirstSaleByEmployeeId(Long employeeId) {
		Sale sale = null;
		try {
			String sql = "SELECT * FROM tbl_oa_sale t WHERE employee_id="+employeeId+" ORDER BY id ASC LIMIT 1";
			List<Sale> sales = JdbcHelper.select(sql, Sale.class);
			if(null != sales && !sales.isEmpty()) {
				sale = sales.get(0);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return sale;
	}
	
	//查询经理
	public int selectManagerCountBySale(Sale sale) {
		int count = 0;
		String sql = "with recursive sale as (SELECT * FROM (SELECT * FROM tbl_oa_sale t WHERE id="+sale.getId()+" ORDER BY id ASC LIMIT 1) t UNION SELECT t.* FROM sale t1 join tbl_oa_sale t ON t1.id=t.last_sale_id) SELECT COUNT(*) FROM sale s INNER JOIN tbl_oa_employee t ON s.employee_id=t.id WHERE t.status = 0 AND s.employee_id <> "+sale.getEmployeeId()+" AND t.employee_role_id="+EmployeeRoleConst.MANAGER+" GROUP BY employee_id";
		try {
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public long selectSaleCountBySale(Map<String, Object> paramMap) {
		long count = 0;
		String condition1 = "";
		if(null != paramMap) {
			if(null != paramMap.get("saleId")) {
				condition1 += " AND id="+paramMap.get("saleId");
			}
			if(null != paramMap.get("employeeId")) {
				condition1 += " AND employee_id="+paramMap.get("employeeId");
			}
		}
		String sql = "with recursive sale as (SELECT * FROM (SELECT * FROM tbl_oa_sale t WHERE 1=1"+condition1+" ORDER BY id ASC LIMIT 1) t UNION SELECT t.* FROM sale t1 join tbl_oa_sale t ON t1.id=t.last_sale_id) SELECT COUNT(*) FROM sale s WHERE 1=1";
		String condition = "";
		if(null != paramMap) {
			if(null != paramMap.get("saleDate>=")) {
				condition += " AND sale_date >= '"+paramMap.get("saleDate>=").toString()+"'";
			}
			if(null != paramMap.get("saleDate<=")) {
				condition += " AND sale_date <= '"+paramMap.get("saleDate<=").toString()+"'";
			}
		}
		sql+=condition;
		try {
			PreparedStatement ps = JdbcHelper.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				count = rs.getLong(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return count;
	}
	
	public BalanceLevel getBalanceLevelByEmployeeId(Long employeeId) {
		Sale sale = this.selectFirstSaleByEmployeeId(employeeId);
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("lastSaleId", sale.getId());
		List<Sale> sales = this.select(paramMap);
		List<BalanceLevel> balances = this.balanceLevelDao.select(null);
		BalanceLevel balanceLevel = null;
		BalanceLevel maxBalanceLevel = null;
		Iterator<BalanceLevel> blIter = balances.iterator();
		while(blIter.hasNext()) {
			BalanceLevel bl = blIter.next();
			if(null == maxBalanceLevel
					|| bl.getManagerCount() >= maxBalanceLevel.getManagerCount()) {
				maxBalanceLevel = bl;
			}
		}
		for(int i = 0; i < sales.size(); i ++) {
			int managerCount = this.selectManagerCountBySale(sales.get(i));
			BalanceLevel bl = null;
			blIter = balances.iterator();
			while(blIter.hasNext()) {
				BalanceLevel level = blIter.next();
				if(level.getManagerCount() == managerCount) {
					bl = level;
					break;
				}
			}
			if(null == bl && managerCount >= maxBalanceLevel.getManagerCount()) {
				bl = maxBalanceLevel;
			}
			if(null == balanceLevel || balanceLevel.getManagerCount() >= bl.getManagerCount()) {
				balanceLevel = bl;
			}
		}
		return balanceLevel;
	}
	
	public long getMinSaleCount(Long employeeId, Map<String, Object> paramMap) {
		Long count = null;
		Sale sale = this.selectFirstSaleByEmployeeId(employeeId);
		Map<String, Object> paramMap2 = new HashMap<String, Object> ();
		paramMap2.put("lastSaleId", sale.getId());
		List<Sale> sales = this.select(paramMap2);
		
		for(int i = 0; i < sales.size(); i ++) {
			sale = sales.get(i);
			paramMap.put("saleId", sale.getId());
			long cnt = this.selectSaleCountBySale(paramMap);
			if(null == count || count > cnt) {
				count = cnt;
			}
		}
		return count;
	}
	
	/*public static void main(String[] args) {
		Sale sale = new Sale();
		sale.setId(11L);
		sale.setEmployeeId(1L);
		int count = selectManagerCountBySale(sale);
		System.out.println(count);
	}*/
}
