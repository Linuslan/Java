package com.saleoa.service;

import com.saleoa.base.IBaseDaoImpl;
import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.IManagerLevelDaoImpl;
import com.saleoa.model.Level;

public class ILevelServiceImpl extends IBaseServiceImpl<Level> implements ILevelService {
    private ILevelDao levelDao;
    public ILevelServiceImpl() {
        levelDao = new ILevelDaoImpl();
        this.dao = levelDao;
    }
}
