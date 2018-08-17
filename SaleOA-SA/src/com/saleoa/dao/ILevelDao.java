package com.saleoa.dao;

import com.saleoa.base.IBaseDao;
import com.saleoa.model.Level;

public interface ILevelDao extends IBaseDao<Level> {

	/**
	 * ��ѯ��߲㼶����Ͳ㼶
	 * @return
	 */
	public Level selectExtremeLevel(boolean isMax);
	
	/**
	 * ��ѯ���ֶ�Ӧ�ĵȼ�
	 * @param point
	 * @return
	 */
	public Level selectByPoint(long point);
}
