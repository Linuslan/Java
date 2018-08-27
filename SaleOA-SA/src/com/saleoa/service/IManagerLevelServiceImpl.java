package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.IManagerLevelDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Level;

public class IManagerLevelServiceImpl extends IBaseServiceImpl<ManagerLevel> implements
		IManagerLevelService {
	private ILevelDao levelDao;
	private IManagerLevelDao managerLevelDao;
	public IManagerLevelServiceImpl() {
		managerLevelDao = new IManagerLevelDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = managerLevelDao;
	}
	
	/**
	 * 获取某个用户最大的售出套数
	 * @param id
	 * @return
	 */
	public ManagerLevel selectBySale(long sale) {
		return this.managerLevelDao.selectBySale(sale);
	}
}
