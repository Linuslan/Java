package com.saleoa.dao;

import java.util.Date;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

public interface ISaleLogDao extends IBaseDao<SaleLog> {

	public SaleLog getInstance(Sale sale);
	
	/**
	 * ͨ��Ա��id������ʱ���ѯ����ʱ���ڵĽ���
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
}
