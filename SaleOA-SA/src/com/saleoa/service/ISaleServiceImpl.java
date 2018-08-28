package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.dao.ISaleDao;
import com.saleoa.dao.ISaleDaoImpl;
import com.saleoa.dao.ISaleLogDao;
import com.saleoa.model.Level;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleLog;

public class ISaleServiceImpl extends IBaseServiceImpl<Sale> implements
		ISaleService {
	private ILevelDao levelDao;
	private ISaleDao saleDao;
	private ISaleLogDao saleLogDao;
	public ISaleServiceImpl() {
		saleDao = new ISaleDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = saleDao;
	}
	
	/**
	 * ��ȡĳ���û������۳�����
	 * @param id
	 * @return
	 */
	public Long getMaxNoByEmployeeId(Long id) {
		return this.saleDao.getMaxNoByEmployeeId(id);
	}
	
	public boolean add(Sale sale) throws Exception {
		boolean success = false;
		Level maxLevel = this.levelDao.selectExtremeLevel(true);
		Long rewardPoints = 1L;
		Level minLevel = this.levelDao.selectExtremeLevel(false);
		sale.setLevelId(minLevel.getId());
		sale.setLevelName(minLevel.getName());
		sale.setRewardPoints(rewardPoints);
		sale.setIsDelete(0);
		sale.setSalary(0l);
		sale.setCreateDate(new Date());
		sale.setUpdateDate(new Date());
		long bonus = maxLevel.getBonus();
		Long lastSaleId = sale.getLastSaleId();
		List<Sale> updates = new ArrayList<Sale> ();
		List<SaleLog> addSaleLogs = new ArrayList<SaleLog> ();
		List<SaleLog> updateSaleLogs = new ArrayList<SaleLog> ();
		if(0l < lastSaleId) {
			Sale lastSale = this.dao.selectById(lastSaleId);
			SaleLog lastSaleLog = this.saleLogDao.selectByEmployeeOnSaleDate(lastSale.getEmployeeId(), sale.getSaleDate());
			if(null == lastSaleLog) {
				lastSaleLog = this.saleLogDao.getInstance(lastSale);
				addSaleLogs.add(lastSaleLog);
			} else {
				updateSaleLogs.add(lastSaleLog);
			}
			lastSale.setRewardPoints(lastSale.getRewardPoints()+1);
			Long lastSaleLevelId = lastSale.getLevelId();
			Level lastSaleLevel = this.levelDao.selectById(lastSaleLevelId);
			long lastSaleSalary = lastSaleLog.getSalary()+lastSaleLevel.getBonus();
			lastSaleLog.setSalary(lastSaleSalary);
			bonus = bonus-lastSaleLevel.getBonus();
			//ʣ�ཱ�����0����������¸��в㼶��Ľ����˼��㽱��
			if(bonus > 0 && 0l < lastSale.getId()) {
				upgradeSale(lastSale, bonus, updates, addSaleLogs, updateSaleLogs);
			}
			Level nextLevel = this.levelDao.selectByPoint(lastSale.getRewardPoints());
			if(null == nextLevel) {
				ExceptionUtil.throwExcep("δ��ѯ�������˻��ֶ�Ӧ�ĵȼ������֣�"+lastSale.getRewardPoints());
			}
			lastSale.setLevelId(nextLevel.getId());
			lastSale.setLevelName(nextLevel.getName());
			updates.add(lastSale);
		}
		this.dao.add(sale);
		if(!updates.isEmpty()) {
			this.dao.updateBatch(updates);
		}
		if(!addSaleLogs.isEmpty()) {
			this.saleLogDao.addBatch(addSaleLogs);
		}
		if(!updateSaleLogs.isEmpty()) {
			this.saleLogDao.updateBatch(updateSaleLogs);
		}
		success = true;
		return success;
	}
	
	public boolean upgradeSale(Sale sale, long bonus, List<Sale> updates, List<SaleLog> addSaleLogs, List<SaleLog> updateSaleLogs) throws Exception {
		boolean success = false;
		Long lastSaleId = sale.getLastSaleId();
		if(0l >= lastSaleId) {
			return true;
		}
		if(sale.getId() == 3l) {
			System.out.println("===========");
		}
		Long levelId = sale.getLevelId();
		Level level = this.levelDao.selectById(levelId);
		Sale lastSale = this.dao.selectById(lastSaleId);
		lastSale.setRewardPoints(lastSale.getRewardPoints()+1);
		Long lastSaleLevelId = lastSale.getLevelId();
		Level lastSaleLevel = this.levelDao.selectById(lastSaleLevelId);
		//�еȼ�����н����ҽ�����ʣ��
		if(lastSaleLevel.getLevel() > level.getLevel() && bonus > 0) {
			long lastSaleBonus = lastSaleLevel.getBonus() - level.getBonus();
			long lastSaleSalary = lastSale.getSalary()+lastSaleBonus;
			lastSale.setSalary(lastSaleSalary);
			bonus = bonus - lastSaleBonus;
			SaleLog lastSaleLog = this.saleLogDao.selectByEmployeeOnSaleDate(lastSale.getEmployeeId(), sale.getSaleDate());
			if(null == lastSaleLog) {
				lastSaleLog = this.saleLogDao.getInstance(lastSale);
				addSaleLogs.add(lastSaleLog);
			} else {
				updateSaleLogs.add(lastSaleLog);
			}
		}
		//ʣ�ཱ�����0����������¸��в㼶��Ľ����˼��㽱��
		if(bonus > 0) {
			upgradeSale(lastSale, bonus, updates, addSaleLogs, updateSaleLogs);
		}
		Level nextLevel = this.levelDao.selectByPoint(lastSale.getRewardPoints());
		if(null == nextLevel) {
			ExceptionUtil.throwExcep("δ��ѯ�������˻��ֶ�Ӧ�ĵȼ������֣�"+lastSale.getRewardPoints());
		}
		lastSale.setLevelId(nextLevel.getId());
		lastSale.setLevelName(nextLevel.getName());
		updates.add(lastSale);
		success = true;
		return success;
	}
}
