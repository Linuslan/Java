package com.saleoa.dao;

import java.util.Date;
import java.util.Map;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.BalanceLevel;
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
	
	/**
	 * ͨ��Ա��id��ѯԱ���ĵ�һ���۳�
	 * @param employeeId
	 * @return
	 */
	public Sale selectFirstSaleByEmployeeId(Long employeeId);
	
	//��ѯ����
	public int selectManagerCountBySale(Sale sale);
	
	/**
	 * 
	 * @param sale
	 * @return
	 */
	public long selectSaleCountBySale(Map<String, Object> paramMap);
	
	public BalanceLevel getBalanceLevelByEmployeeId(Long employeeId);
	
	public long getMinSaleCount(Long employeeId, Map<String, Object> paramMap);
}
