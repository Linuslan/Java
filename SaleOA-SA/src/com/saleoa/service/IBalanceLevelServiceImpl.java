package com.saleoa.service;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.IBalanceLevelDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.BalanceLevel;

public class IBalanceLevelServiceImpl extends IBaseServiceImpl<BalanceLevel> implements
		IBalanceLevelService {
	private ILevelDao levelDao;
	public IBalanceLevelServiceImpl() {
		this.dao = new IBalanceLevelDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
}
