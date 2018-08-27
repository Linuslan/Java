package com.saleoa.service;

import com.saleoa.base.IBaseService;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Sale;

public interface IManagerLevelService extends IBaseService<ManagerLevel> {

	/**
	 * ��ѯ���۵�������ѯ��Ӧ�ĵȼ�
	 * @param point
	 * @return
	 */
	public ManagerLevel selectBySale(long sale);
}
