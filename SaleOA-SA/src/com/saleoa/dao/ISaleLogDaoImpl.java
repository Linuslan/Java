package com.saleoa.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

public class ISaleLogDaoImpl extends IBaseDaoImpl<SaleLog> implements ISaleLogDao {
	
	public SaleLog getInstance(Sale sale) {
		SaleLog saleLog = new SaleLog();
		saleLog.setCreateDate(new Date());
		saleLog.setEmployeeId(sale.getEmployeeId());
		saleLog.setEmployeeName(sale.getEmployeeName());
		saleLog.setSalary(0L);
		saleLog.setSaleId(sale.getId());
		saleLog.setUpdateDate(new Date());
		return saleLog;
	}
	
	/**
	 * 通过员工id和销售时间查询销售时间内的奖金
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate) {
		SaleLog log = null;
		Map<String, Object> paramMap = new HashMap<String, Object> ();
		paramMap.put("employeeId", employeeId);
		paramMap.put("createDate>=", DateUtil.getFirstDateStrOfMonthByDate(saleDate));
		paramMap.put("createDate<=", DateUtil.getEndDateStrOfMonthByDate(saleDate));
		List<SaleLog> logs = this.select(paramMap);
		if(logs.size() > 0) {
			log = logs.get(0);
		}
		return log;
	}
	
}
