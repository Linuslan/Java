package com.saleoa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.dao.IDepartmentDao;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.IEmployeeDao;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.IManagerLevelDaoImpl;
import com.saleoa.dao.ISaleDao;
import com.saleoa.dao.ISaleDaoImpl;
import com.saleoa.dao.ISaleSalaryDao;
import com.saleoa.dao.ISaleSalaryDaoImpl;
import com.saleoa.model.Employee;
import com.saleoa.model.Level;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Sale;
import com.saleoa.model.SaleSalary;

public class ISaleServiceImpl extends IBaseServiceImpl<Sale> implements
		ISaleService {
	private ILevelDao levelDao;
	private ISaleDao saleDao;
	private ISaleSalaryDao saleSalaryDao;
	private IEmployeeDao employeeDao;
	private IDepartmentDao departmentDao;
	private IManagerLevelDao managerLevelDao;
	public ISaleServiceImpl() {
		saleDao = new ISaleDaoImpl();
		levelDao = new ILevelDaoImpl();
		this.dao = saleDao;
		employeeDao = new IEmployeeDaoImpl();
		departmentDao = new IDepartmentDaoImpl();
		managerLevelDao = new IManagerLevelDaoImpl();
		saleSalaryDao = new ISaleSalaryDaoImpl();
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
		List<SaleSalary> addSaleLogs = new ArrayList<SaleSalary> ();
		List<SaleSalary> updateSaleLogs = new ArrayList<SaleSalary> ();
		Long employeeId = sale.getEmployeeId();
		Employee employee = this.employeeDao.selectById(employeeId);
		sale.setDepartmentId(employee.getDepartmentId());
		sale.setDepartmentName(employee.getDepartmentName());
		if(0l < lastSaleId) {
			Sale lastSale = this.dao.selectById(lastSaleId);
			SaleSalary lastSaleLog = this.saleSalaryDao.selectByEmployeeOnSaleDate(lastSale.getEmployeeId(), sale.getSaleDate());
			if(null == lastSaleLog) {
				lastSaleLog = this.saleSalaryDao.getInstance(lastSale);
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
			//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
			if(bonus > 0 && 0l < lastSale.getId()) {
				upgradeSale(lastSale, bonus, updates, addSaleLogs, updateSaleLogs);
			}
			Level nextLevel = this.levelDao.selectByPoint(lastSale.getRewardPoints());
			if(null == nextLevel) {
				ExceptionUtil.throwExcep("未查询到介绍人积分对应的等级，积分："+lastSale.getRewardPoints());
			}
			lastSale.setLevelId(nextLevel.getId());
			lastSale.setLevelName(nextLevel.getName());
			updates.add(lastSale);
		}
		Long departmentId = employee.getDepartmentId();
		List<Employee> managers = this.employeeDao.selectManagerByDepartment(departmentId);
		int saleCount = this.saleDao.getSaleCountByDepartment(departmentId, sale.getSaleDate());
		saleCount ++;	//本次也算一次销售数
		ManagerLevel managerLevel = this.managerLevelDao.selectBySale(saleCount);
		Employee manager = null;
		for(int i = 0; i < managers.size(); i ++) {
			manager = managers.get(i);
			SaleSalary saleLog = this.saleSalaryDao.selectByEmployeeOnSaleDate(manager.getId(), sale.getSaleDate());
			if(null == saleLog) {
				Sale managerSale = new Sale();
				BeanUtil.copyBean(sale, managerSale);
				managerSale.setEmployeeId(manager.getId());
				managerSale.setEmployeeName(manager.getName());
				saleLog = this.saleSalaryDao.getInstance(managerSale);
				saleLog.setSaleId(null);
				addSaleLogs.add(saleLog);
			} else {
				updateSaleLogs.add(saleLog);
			}
			long basicSalary = managerLevel.getBasicSalary();
			//long commission = managerLevel.getCommission()*saleCount;
			//saleLog.setSalary(basicSalary+commission);
			saleLog.setSalary(basicSalary);
		}
		this.dao.add(sale);
		if(!updates.isEmpty()) {
			this.dao.updateBatch(updates);
		}
		if(!addSaleLogs.isEmpty()) {
			this.saleSalaryDao.addBatch(addSaleLogs);
		}
		if(!updateSaleLogs.isEmpty()) {
			this.saleSalaryDao.updateBatch(updateSaleLogs);
		}
		success = true;
		return success;
	}
	
	public boolean upgradeSale(Sale sale, long bonus, List<Sale> updates, List<SaleSalary> addSaleLogs, List<SaleSalary> updateSaleLogs) throws Exception {
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
		//有等级差才有奖金，且奖金有剩余
		if(lastSaleLevel.getLevel() > level.getLevel() && bonus > 0) {
			long lastSaleBonus = lastSaleLevel.getBonus() - level.getBonus();
			long lastSaleSalary = lastSale.getSalary()+lastSaleBonus;
			lastSale.setSalary(lastSaleSalary);
			bonus = bonus - lastSaleBonus;
			SaleSalary lastSaleLog = this.saleSalaryDao.selectByEmployeeOnSaleDate(lastSale.getEmployeeId(), sale.getSaleDate());
			if(null == lastSaleLog) {
				lastSaleLog = this.saleSalaryDao.getInstance(lastSale);
				addSaleLogs.add(lastSaleLog);
			} else {
				updateSaleLogs.add(lastSaleLog);
			}
		}
		//剩余奖金大于0，则接着找下个有层级查的介绍人计算奖金
		if(bonus > 0) {
			upgradeSale(lastSale, bonus, updates, addSaleLogs, updateSaleLogs);
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
