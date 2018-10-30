package com.saleoa.service;

import java.util.List;
import java.util.Map;

import com.saleoa.base.IBaseService;
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Sale;

public interface ISaleService extends IBaseService<Sale> {

	/**
	 * ��ȡĳ���û������۳�����
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
	
	/**
	 * ����Ա��id����Ա�������ۼ�¼
	 * @param employeeId
	 * @return
	 */
	public List<Sale> selectByEmployeeId(Long employeeId);
	
	public BalanceLevel getBalanceLevelByEmployeeId(Long employeeId);
	
	public long getMinSaleCount(Long employeeId, Map<String, Object> paramMap);
}
