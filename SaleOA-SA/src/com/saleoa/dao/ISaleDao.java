package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Sale;

public interface ISaleDao extends IBaseDao<Sale> {

	/**
	 * ��ȡĳ���û������۳�����
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
}
