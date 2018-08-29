package com.saleoa.dao;

import java.util.Date;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Sale;

public interface ISaleDao extends IBaseDao<Sale> {
	
	/**
	 * ��ѯ�������������ڵ���������
	 * @param departmentId
	 * @param saleDate
	 * @return
	 */
	public int getSaleCountByDepartment(Long departmentId, Date saleDate);

	/**
	 * ��ȡĳ���û������۳�����
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
}
