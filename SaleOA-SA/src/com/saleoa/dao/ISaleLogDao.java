package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.SalaryConfig;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

import java.util.Date;

public interface ISaleLogDao extends IBaseDao<SaleLog> {

	public SaleLog getInstance(Sale sale, Date saleDate);
}
