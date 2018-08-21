package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.ISaleDao;
import com.saleoa.dao.ISaleDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Sale;
import com.saleoa.model.Level;

public class ISaleServiceImpl extends IBaseServiceImpl<Sale> implements
		ISaleService {
	private ILevelDao levelDao;
	private ISaleDao saleDao;
	public ISaleServiceImpl() {
		saleDao = new ISaleDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = saleDao;
	}
	
	/**
	 * 获取某个用户最大的售出套数
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
		if(0l < lastSaleId) {
			Sale lastSale = this.dao.selectById(lastSaleId);
			lastSale.setRewardPoints(lastSale.getRewardPoints()+1);
			Long lastSaleLevelId = lastSale.getLevelId();
			Level lastSaleLevel = this.levelDao.selectById(lastSaleLevelId);
			long lastSaleSalary = lastSale.getSalary()+lastSaleLevel.getBonus();
			lastSale.setSalary(lastSaleSalary);
			//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
			if(bonus-lastSaleLevel.getBonus() > 0 && 0l < lastSale.getId()) {
				upgradeSale(lastSale, bonus, updates);
			}
			Level nextLevel = this.levelDao.selectByPoint(lastSale.getRewardPoints());
			if(null == nextLevel) {
				ExceptionUtil.throwExcep("未查询到介绍人积分对应的等级，积分："+lastSale.getRewardPoints());
			}
			lastSale.setLevelId(nextLevel.getId());
			lastSale.setLevelName(nextLevel.getName());
			updates.add(lastSale);
		}
		this.dao.add(sale);
		if(!updates.isEmpty()) {
			this.dao.updateBatch(updates);
		}
		success = true;
		return success;
	}
	
	public boolean upgradeSale(Sale sale, long bonus, List<Sale> updates) throws Exception {
		boolean success = false;
		Long lastSaleId = sale.getLastSaleId();
		if(0l >= lastSaleId) {
			return true;
		}
		if(sale.getId() == 4l) {
			System.out.println("===========");
		}
		Long levelId = sale.getLevelId();
		Level level = this.levelDao.selectById(levelId);
		Sale lastSale = this.dao.selectById(lastSaleId);
		lastSale.setRewardPoints(lastSale.getRewardPoints()+1);
		Long lastSaleLevelId = lastSale.getLevelId();
		Level lastSaleLevel = this.levelDao.selectById(lastSaleLevelId);
		//有等级差才有奖金，且奖金有剩余
		if(lastSaleLevel.getLevel() > level.getLevel() && bonus-lastSaleLevel.getBonus() > 0) {
			long lastSaleBonus = lastSaleLevel.getBonus() - level.getBonus();
			long lastSaleSalary = lastSale.getSalary()+lastSaleBonus;
			lastSale.setSalary(lastSaleSalary);
		}
		//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
		if(bonus-lastSaleLevel.getBonus() > 0) {
			upgradeSale(lastSale, bonus, updates);
		}
		Level nextLevel = this.levelDao.selectByPoint(lastSale.getRewardPoints());
		if(null == nextLevel) {
			ExceptionUtil.throwExcep("未查询到介绍人积分对应的等级，积分："+lastSale.getRewardPoints());
		}
		lastSale.setLevelId(nextLevel.getId());
		lastSale.setLevelName(nextLevel.getName());
		updates.add(lastSale);
		success = true;
		return success;
	}
}
