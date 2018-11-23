package com.saleoa.dao;

import java.util.Date;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

public class ISaleLogDaoImpl extends IBaseDaoImpl<SaleLog> implements ISaleLogDao {
	public SaleLog getInstance(Sale sale, Date saleDate) {
		SaleLog saleLog = new SaleLog();
		saleLog.setEmployeeId(sale.getEmployeeId());
		saleLog.setEmployeeName(sale.getEmployeeName());
		if(null != saleDate) {
			saleLog.setSaleDate(saleDate);
		} else {
			saleLog.setSaleDate(sale.getSaleDate());
		}
		saleLog.setCreateDate(new Date());
		saleLog.setOriginalSaleId(sale.getId());
		return saleLog;
	}
}
