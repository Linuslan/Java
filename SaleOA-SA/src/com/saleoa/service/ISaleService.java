package com.saleoa.service;

import com.saleoa.base.IBaseService;
import com.saleoa.model.Sale;

public interface ISaleService extends IBaseService<Sale> {

	/**
	 * ��ȡĳ���û������۳�����
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id);
}
