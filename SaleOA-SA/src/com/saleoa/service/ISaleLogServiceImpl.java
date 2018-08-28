package com.saleoa.service;

import java.util.Date;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.ISaleLogDao;
import com.saleoa.dao.ISaleLogDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.SaleLog;

public class ISaleLogServiceImpl extends IBaseServiceImpl<SaleLog> implements
		ISaleLogService {
	private ILevelDao levelDao;
	private ISaleLogDao saleLogDao;
	public ISaleLogServiceImpl() {
		saleLogDao = new ISaleLogDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = saleLogDao;
	}
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate) {
		// TODO Auto-generated method stub
		return saleLogDao.selectByEmployeeOnSaleDate(employeeId, saleDate);
	}
}
