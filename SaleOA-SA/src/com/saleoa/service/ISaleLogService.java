package com.saleoa.service;

import java.util.Date;

import com.saleoa.base.IBaseService;
import com.saleoa.model.SaleLog;

public interface ISaleLogService extends IBaseService<SaleLog> {
	
	/**
	 * ͨ��Ա��id������ʱ���ѯ����ʱ���ڵĽ���
	 * @param employeeId
	 * @param saleDate
	 * @return
	 */
	public SaleLog selectByEmployeeOnSaleDate(Long employeeId, Date saleDate);
	
}
