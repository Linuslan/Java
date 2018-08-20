package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.ISaleDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Sale;
import com.saleoa.model.Level;

public class ISaleServiceImpl extends IBaseServiceImpl<Sale> implements
		ISaleService {
	private ILevelDao levelDao;
	public ISaleServiceImpl() {
		this.dao = new ISaleDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
	
	public boolean add(Sale sale) throws Exception {
		boolean success = false;
		/*Level maxLevel = this.levelDao.selectExtremeLevel(true);
		Long rewardPoints = 1L;
		Level minLevel = this.levelDao.selectExtremeLevel(false);
		sale.setLevelId(minLevel.getId());
		sale.setLevelName(minLevel.getName());
		sale.setRewardPoints(rewardPoints);
		sale.setIsDelete(0);
		sale.setSalary(0l);
		sale.setCreateDate(new Date());
		sale.setUpdateDate(new Date());
		sale.setStatus(0);
		if(null == sale.getLeaderId()) {
			sale.setLeaderId(0L);
			sale.setLeaderName("");
		}
		long bonus = maxLevel.getBonus();
		Long introducerId = sale.getIntroducerId();
		List<Sale> updates = new ArrayList<Sale> ();
		if(0l < introducerId) {
			Sale introducer = this.dao.selectById(introducerId);
			introducer.setRewardPoints(introducer.getRewardPoints()+1);
			Long introducerLevelId = introducer.getLevelId();
			Level introducerLevel = this.levelDao.selectById(introducerLevelId);
			long introducerSalary = introducer.getSalary()+introducerLevel.getBonus();
			introducer.setSalary(introducerSalary);
			Level nextLevel = this.levelDao.selectByPoint(introducer.getRewardPoints());
			if(null == nextLevel) {
				ExceptionUtil.throwExcep("未查询到介绍人积分对应的等级，积分："+introducer.getRewardPoints());
			}
			introducer.setLevelId(nextLevel.getId());
			introducer.setLevelName(nextLevel.getName());
			//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
			if(bonus-introducerLevel.getBonus() > 0 && 0l < introducer.getId()) {
				upgradeIntroducer(introducer, bonus, updates);
			}
			updates.add(introducer);
		}
		this.dao.add(sale);
		if(!updates.isEmpty()) {
			this.dao.updateBatch(updates);
		}
		success = true;*/
		return success;
	}
	
	public boolean upgradeIntroducer(Sale sale, long bonus, List<Sale> updates) throws Exception {
		boolean success = false;
		/*Long introducerId = sale.getIntroducerId();
		if(0l >= introducerId) {
			return true;
		}
		Long levelId = sale.getLevelId();
		Level level = this.levelDao.selectById(levelId);
		Sale introducer = this.dao.selectById(introducerId);
		introducer.setRewardPoints(introducer.getRewardPoints()+1);
		Long introducerLevelId = introducer.getLevelId();
		Level introducerLevel = this.levelDao.selectById(introducerLevelId);
		//有等级差才有奖金，且奖金有剩余
		if(introducerLevel.getLevel() > level.getLevel() && bonus-introducerLevel.getBonus() > 0) {
			long introducerBonus = introducerLevel.getBonus() - level.getBonus();
			long introducerSalary = introducer.getSalary()+introducerBonus;
			introducer.setSalary(introducerSalary);
		}
		Level nextLevel = this.levelDao.selectByPoint(introducer.getRewardPoints());
		if(null == nextLevel) {
			ExceptionUtil.throwExcep("未查询到介绍人积分对应的等级，积分："+introducer.getRewardPoints());
		}
		introducer.setLevelId(nextLevel.getId());
		introducer.setLevelName(nextLevel.getName());
		//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
		if(bonus-introducerLevel.getBonus() > 0) {
			upgradeIntroducer(introducer, bonus, updates);
		}
		updates.add(introducer);*/
		success = true;
		return success;
	}
}
