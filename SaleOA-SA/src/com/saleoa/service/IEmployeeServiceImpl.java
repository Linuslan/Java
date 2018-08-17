package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Employee;
import com.saleoa.model.Level;

public class IEmployeeServiceImpl extends IBaseServiceImpl<Employee> implements
		IEmployeeService {
	private ILevelDao levelDao;
	public IEmployeeServiceImpl() {
		this.dao = new IEmployeeDaoImpl();
		levelDao = new ILevelDaoImpl();
	}
	
	public boolean add(Employee employee) throws Exception {
		boolean success = false;
		Level maxLevel = this.levelDao.selectExtremeLevel(true);
		Long rewardPoints = 1L;
		Level minLevel = this.levelDao.selectExtremeLevel(false);
		employee.setLevelId(minLevel.getId());
		employee.setLevelName(minLevel.getName());
		employee.setRewardPoints(rewardPoints);
		employee.setIsDelete(0);
		employee.setSalary(0l);
		employee.setCreateDate(new Date());
		employee.setUpdateDate(new Date());
		employee.setStatus(0);
		if(null == employee.getLeaderId()) {
			employee.setLeaderId(0L);
			employee.setLeaderName("");
		}
		long bonus = maxLevel.getBonus();
		Long introducerId = employee.getIntroducerId();
		List<Employee> updates = new ArrayList<Employee> ();
		Employee introducer = this.dao.selectById(introducerId);
		introducer.setRewardPoints(introducer.getRewardPoints()+1);
		Long introducerLevelId = introducer.getLevelId();
		Level introducerLevel = this.levelDao.selectById(introducerLevelId);
		long introducerSalary = introducer.getSalary()+introducerLevel.getBonus();
		introducer.setSalary(introducerSalary);
		Level nextLevel = this.levelDao.selectByPoint(introducer.getRewardPoints());
		if(null == nextLevel) {
			ExceptionUtil.throwExcep("δ��ѯ�������˻��ֶ�Ӧ�ĵȼ������֣�"+introducer.getRewardPoints());
		}
		introducer.setLevelId(nextLevel.getId());
		introducer.setLevelName(nextLevel.getName());
		//ʣ�ཱ�����0����������¸��в㼶��Ľ����˼��㽱��
		if(bonus-introducerLevel.getBonus() > 0 && 0l < introducer.getId()) {
			upgradeIntroducer(introducer, bonus, updates);
		}
		updates.add(introducer);
		this.dao.add(employee);
		this.dao.updateBatch(updates);
		success = true;
		return success;
	}
	
	public boolean upgradeIntroducer(Employee employee, long bonus, List<Employee> updates) throws Exception {
		boolean success = false;
		Long introducerId = employee.getIntroducerId();
		Long levelId = employee.getLevelId();
		Level level = this.levelDao.selectById(levelId);
		Employee introducer = this.dao.selectById(introducerId);
		introducer.setRewardPoints(introducer.getRewardPoints()+1);
		Long introducerLevelId = introducer.getLevelId();
		Level introducerLevel = this.levelDao.selectById(introducerLevelId);
		//�еȼ�����н����ҽ�����ʣ��
		if(introducerLevel.getLevel() > level.getLevel() && bonus-introducerLevel.getBonus() > 0) {
			long introducerBonus = introducerLevel.getBonus() - level.getBonus();
			long introducerSalary = introducer.getSalary()+introducerBonus;
			introducer.setSalary(introducerSalary);
		}
		Level nextLevel = this.levelDao.selectByPoint(introducer.getRewardPoints());
		if(null == nextLevel) {
			ExceptionUtil.throwExcep("δ��ѯ�������˻��ֶ�Ӧ�ĵȼ������֣�"+introducer.getRewardPoints());
		}
		introducer.setLevelId(nextLevel.getId());
		introducer.setLevelName(nextLevel.getName());
		//ʣ�ཱ�����0����������¸��в㼶��Ľ����˼��㽱��
		if(bonus-introducerLevel.getBonus() > 0 && 0l < introducer.getId()) {
			upgradeIntroducer(introducer, bonus, updates);
		}
		updates.add(introducer);
		success = true;
		return success;
	}
}
