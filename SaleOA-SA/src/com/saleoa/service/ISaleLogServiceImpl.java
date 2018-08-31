package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.dao.ISalaryConfigDaoImpl;
import com.saleoa.dao.ISaleLogDaoImpl;
import com.saleoa.model.Department;
import com.saleoa.model.SalaryConfig;
import com.saleoa.model.SaleLog;

public class ISaleLogServiceImpl extends IBaseServiceImpl<SaleLog> implements
		ISaleLogService {
	private ILevelDao levelDao;
	public ISaleLogServiceImpl() {
		this.dao = new ISaleLogDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
}
