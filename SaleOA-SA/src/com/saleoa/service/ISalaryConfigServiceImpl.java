package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.dao.ISalaryConfigDaoImpl;
import com.saleoa.model.Department;
import com.saleoa.model.SalaryConfig;

public class ISalaryConfigServiceImpl extends IBaseServiceImpl<SalaryConfig> implements
		ISalaryConfigService {
	private ILevelDao levelDao;
	public ISalaryConfigServiceImpl() {
		this.dao = new ISalaryConfigDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
}
