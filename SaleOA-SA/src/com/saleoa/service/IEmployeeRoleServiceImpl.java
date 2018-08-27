package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IEmployeeRoleDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.EmployeeRole;

public class IEmployeeRoleServiceImpl extends IBaseServiceImpl<EmployeeRole> implements
		IEmployeeRoleService {
	private ILevelDao levelDao;
	public IEmployeeRoleServiceImpl() {
		this.dao = new IEmployeeRoleDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
}
