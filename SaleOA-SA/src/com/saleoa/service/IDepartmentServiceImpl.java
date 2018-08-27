package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Department;

public class IDepartmentServiceImpl extends IBaseServiceImpl<Department> implements
		IDepartmentService {
	private ILevelDao levelDao;
	public IDepartmentServiceImpl() {
		this.dao = new IDepartmentDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
}
