package com.saleoa.service;

import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.plugin.Page;
import com.saleoa.dao.ISaleSalaryDao;
import com.saleoa.dao.ISaleSalaryDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.SaleSalary;

public class ISaleSalaryServiceImpl extends IBaseServiceImpl<SaleSalary> implements
		ISaleSalaryService {
	private ILevelDao levelDao;
	private ISaleSalaryDao saleLogDao;
	public ISaleSalaryServiceImpl() {
		saleLogDao = new ISaleSalaryDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = saleLogDao;
	}
	
	public Page<SaleSalary> selectPage(Map<String, Object> paramMap, long pageNo, int limit) {
		return saleLogDao.selectPage(paramMap, pageNo, limit);
	}
	
	public SaleSalary selectByEmployeeOnSaleDate(Long employeeId, Date saleDate) {
		// TODO Auto-generated method stub
		return saleLogDao.selectByEmployeeOnSaleDate(employeeId, saleDate);
	}
}
