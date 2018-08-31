package com.saleoa.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.saleoa.base.IBaseServiceImpl;
import com.saleoa.common.constant.EmployeeRoleConst;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.dao.IDepartmentDao;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.dao.IEmployeeDao;
import com.saleoa.dao.IEmployeeDaoImpl;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.IManagerLevelDaoImpl;
import com.saleoa.dao.ISalaryConfigDao;
import com.saleoa.dao.ISalaryConfigDaoImpl;
import com.saleoa.dao.ISalaryDao;
import com.saleoa.dao.ISalaryDaoImpl;
import com.saleoa.dao.ISaleDao;
import com.saleoa.dao.ISaleDaoImpl;
import com.saleoa.model.Employee;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.Salary;
import com.saleoa.model.SalaryConfig;

public class ISalaryServiceImpl extends IBaseServiceImpl<Salary> implements
		ISalaryService {
	private ISalaryDao salaryDao;
	private IDepartmentDao departmentDao;
	private IEmployeeDao employeeDao;
	private ISaleDao saleDao;
	private IManagerLevelDao managerLevelDao;
	private ISalaryConfigDao salaryConfigDao;
	private SalaryConfig salaryConfig;
	public ISalaryServiceImpl() {
		salaryDao = new ISalaryDaoImpl();
		this.dao = salaryDao;
		departmentDao = new IDepartmentDaoImpl();
		employeeDao = new IEmployeeDaoImpl();
		salaryConfigDao = new ISalaryConfigDaoImpl();
		this.saleDao = new ISaleDaoImpl();
		managerLevelDao = new IManagerLevelDaoImpl();
	}
	
	/**
	 * 创建工资
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean createSalary(int year, int month) throws Exception {
		boolean success = false;
		try {
			List<Salary> salaryList = this.salaryDao.createSalary(year, month);
			Salary salary = null;
			Map<Long, List<Salary>> departmentSalaryMap = new HashMap<Long, List<Salary>> ();
			for(int i = 0; i < salaryList.size(); i ++) {
				salary = salaryList.get(i);
				Employee employee = this.employeeDao.selectById(salary.getUserId());
				Long departmentId = employee.getDepartmentId();
				salary.setDepartmentId(departmentId);
				salary.setDepartmentName(employee.getDepartmentName());
				List<Salary> deptSalarys = departmentSalaryMap.get(departmentId.longValue());
				if(null == deptSalarys) {
					deptSalarys = new ArrayList<Salary> ();
					departmentSalaryMap.put(departmentId.longValue(), deptSalarys);
				}
				deptSalarys.add(salary);
				
				//如果是高管，则工资的算法不同
				if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.EMPLOYEE.longValue()) {
					continue;
				}
			}
			Iterator<Entry<Long, List<Salary>>> iter = departmentSalaryMap.entrySet().iterator();
			Entry<Long, List<Salary>> entry = null;
			//超额达标数，如果超额达标数等于部门数量，则每个经理都有总达标奖
			int overGoalCount = 0;
			salaryConfig = this.salaryConfigDao.selectById(1L);
			Map<Long, List<Salary>> mngSalaryMap = new HashMap<Long, List<Salary>> ();
			while(iter.hasNext()) {
				entry = iter.next();
				Long departmentId = entry.getKey();
				List<Salary> salarys = entry.getValue();
				String dateStr = DateUtil.getCustomStartDateStr(year, month);
				int saleCount = this.saleDao.getSaleCountByDepartment(departmentId, DateUtil.parseFullDate(dateStr));
				List<Employee> managers = this.employeeDao.selectManagerByDepartment(departmentId);
				for(int i = 0; i < managers.size(); i ++) {
					Employee employee = managers.get(i);
					Salary mngSalary = new Salary();
					ManagerLevel managerLevel = this.managerLevelDao.selectBySale(saleCount);
					mngSalary.setCreateDate(new Date());
					mngSalary.setYear(year);
					mngSalary.setMonth(month);
					mngSalary.setAmercement(0l);
					mngSalary.setCompanyLend(0l);
					mngSalary.setDeductMoney(0l);
					mngSalary.setDepartmentId(employee.getDepartmentId());
					mngSalary.setDepartmentName(employee.getDepartmentName());
					mngSalary.setFullDutyBonus(0l);
					mngSalary.setIsDelete(0);
					mngSalary.setMemo("");
					mngSalary.setMoney(managerLevel.getBasicSalary());
					mngSalary.setOverGoalBonus(0L);
					mngSalary.setReachGoalBonus(0L);
					mngSalary.setStatus(0);
					mngSalary.setSupposedMoney(0L);
					mngSalary.setTax(0L);
					mngSalary.setTotalMoney(0L);
					mngSalary.setTotalReachGoalBonus(0L);
					mngSalary.setUpdateDate(new Date());
					mngSalary.setUserId(employee.getId());
					mngSalary.setUserName(employee.getName());
					//销售数大于最大的销售数，则有超额达标奖
					if(saleCount > managerLevel.getMaxSale()) {
						mngSalary.setReachGoalBonus(managerLevel.getReachGoalBonus());
						overGoalCount ++;
						mngSalary.setOverGoalBonus(managerLevel.getCommission()*saleCount);
					}
					mngSalary.setOfficeManageBonus(30000l);
					List<Salary> mngSalarys = mngSalaryMap.get(departmentId.longValue());
					if(null == mngSalarys) {
						mngSalarys = new ArrayList<Salary> ();
						mngSalaryMap.put(departmentId.longValue(), mngSalarys);
					}
					mngSalarys.add(mngSalary);
				}
			}
			Iterator<Entry<Long, List<Salary>>> mngIter = mngSalaryMap.entrySet().iterator();
			Entry<Long, List<Salary>> mngEntry = null;
			while(mngIter.hasNext()) {
				mngEntry = mngIter.next();
				Long deptId = mngEntry.getKey();
				List<Salary> salarys = mngEntry.getValue();
				for(int i = 0; i < salarys.size(); i ++) {
					Salary mngSalary = salarys.get(i);
					//总达标奖
					if(overGoalCount >= departmentSalaryMap.size()) {
						mngSalary.setTotalReachGoalBonus(salaryConfig.getTotalReachGoalBonus());
					}
					departmentSalaryMap.get(deptId.longValue()).add(0, mngSalary);
				}
			}
			
			
			//计算税额以及应发和最终工资
			List<Salary> salarys = new ArrayList<Salary> ();
			Iterator<Entry<Long, List<Salary>>> deptSalaryIter = departmentSalaryMap.entrySet().iterator();
			Entry<Long, List<Salary>> deptSalaryEntry = null;
			while(deptSalaryIter.hasNext()) {
				deptSalaryEntry = deptSalaryIter.next();
				List<Salary> deptSalarys = deptSalaryEntry.getValue();
				Salary sal = null;
				for(int i = 0; i < deptSalarys.size(); i ++) {
					sal = deptSalarys.get(i);
					sal.setSupposedMoney(getSupposedMoney(sal));
					Employee employee = this.employeeDao.selectById(sal.getUserId());
					if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.MANAGER.longValue()) {
						Long tax = getTax(salary.getSupposedMoney());
						sal.setTax(tax);
					}
					Long totalMoney = getTotalSalary(sal);
					sal.setTotalMoney(totalMoney);
					salarys.add(sal);
				}
			}
			success = this.salaryDao.addBatch(salarys);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return success;
	}
	
	private Long getSupposedMoney(Salary salary) {
		return salary.getMoney() + salary.getReachGoalBonus() + salary.getOverGoalBonus() + salary.getOfficeManageBonus() + salary.getFullDutyBonus() + salary.getTotalReachGoalBonus()
				-salary.getDeductMoney();
	}
	
	public Long getTax(Long supposedMoney) {
		if(null == salaryConfig) {
			this.salaryConfig = this.salaryConfigDao.selectById(1L);
		}
		Long tax = 0L;
		Long taxThreshold = salaryConfig.getTaxThreshold();
		int taxRate = salaryConfig.getTaxRate();
		if(supposedMoney > taxThreshold) {
			Long taxMoney = supposedMoney - taxThreshold;
			tax = taxMoney*taxRate/100;
		}
		return tax;
	}
	
	private Long getTotalSalary(Salary salary) {
		return salary.getSupposedMoney()
				- salary.getAmercement() - salary.getCompanyLend() - salary.getTax();
	}
	
	/**
	 * 批量审核
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public boolean auditBatch(List<Long> idList) throws Exception {
		return this.salaryDao.auditBatch(idList);
	}
	
	public void export(int year, int month) {
		try {
//			HSSFWorkbook wb = this.salaryService.createExcel(list);
//			fileName = URLEncoder.encode(year+"年"+month+"月员工薪资.xls", "UTF-8");
			List<Salary> salaryList = this.salaryDao.queryByYearAndMonth(year, month);
			Map<Long, List<Salary>> deptMap = new HashMap<Long, List<Salary>> ();
			List<Salary> mngList = new ArrayList<Salary> ();
			Salary salary = null;
			for(int i = 0; i < salaryList.size(); i ++) {
				salary = salaryList.get(i);
				Long departmentId = salary.getDepartmentId();
				Long employeeId = salary.getUserId();
				Employee employee = this.employeeDao.selectById(employeeId);
				List<Salary> deptList = deptMap.get(departmentId.longValue());
				if(null == deptList) {
					deptList = new ArrayList<Salary> ();
					deptMap.put(departmentId.longValue(), deptList);
				}
				deptList.add(salary);
				if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.MANAGER.longValue()) {
					mngList.add(salary);
				}
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public HSSFWorkbook createExcel(Map<Long, List<Salary>> deptMap) {
		HSSFWorkbook wb = new HSSFWorkbook();
		if(null != deptMap) {
			Set<Entry<Long, List<Salary>>> entrySet = deptMap.entrySet();
			Iterator<Entry<Long, List<Salary>>> iter = entrySet.iterator();
			Entry<Long, List<Salary>> entry = null;
			while(iter.hasNext()) {
				entry = iter.next();
				Long deptId = entry.getKey();
				List<Salary> deptList = entry.getValue();
				
			}
//			Iterator<DepartmentSalary> iter = list.iterator();
//			DepartmentSalary ds = null;
//			Sheet sheet = null;
//			while(iter.hasNext()) {
//				ds = iter.next();
//				sheet = wb.createSheet(ds.getName());
//				sheet.setDefaultRowHeightInPoints(30);
//				sheet.setColumnWidth(0, 200*10);
//				sheet.setColumnWidth(1, 300*15);
//				
//				CellStyle cs = wb.createCellStyle();
//				Font font = wb.createFont();
//				font.setFontHeightInPoints((short) 12);
//				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//				cs.setFont(font);
//				cs.setAlignment(CellStyle.ALIGN_CENTER);
//				
//				int rowIndex = 0;
//				
//				Row titleRow = sheet.createRow(rowIndex ++);
//				titleRow.setHeightInPoints(30);
//				Cell titleCell = titleRow.createCell(0);
//				titleCell.setCellValue(ds.getName()+"员工薪资列表");
//				titleCell.setCellStyle(cs);
//				
//				Row infoRow = sheet.createRow(rowIndex ++);
//				
//				Row infoRow2 = sheet.createRow(rowIndex ++);
//				
//				infoRow.setHeightInPoints(30);
//				CellStyle cs2 = wb.createCellStyle();
//				Font font2 = wb.createFont();
//				font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
//				cs2.setFont(font2);
//				cs2.setAlignment(CellStyle.ALIGN_CENTER);
//				int cellIndex = 0;
//				
//				//姓名列占两行
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				Cell cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("姓名");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("应出勤");
//				cell.setCellStyle(cs2);
//				
//				//实出勤占一行两列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 1));
//				cell = infoRow.createCell(cellIndex);
//				cell.setCellValue("实出勤");
//				cell.setCellStyle(cs2);
//				
//				Cell subCell1 = infoRow2.createCell(cellIndex ++);
//				subCell1.setCellValue("天");
//				subCell1.setCellStyle(cs2);
//				Cell subCell2 = infoRow2.createCell(cellIndex ++);
//				subCell2.setCellValue("时");
//				subCell2.setCellStyle(cs2);
//				
//				//基本工资占两行
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("基本工资");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("岗位工资");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("绩效标准");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("绩效分数");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("实发绩效");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("抽成");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("满勤奖");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("加班费");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("工龄工资");
//				cell.setCellStyle(cs2);
//				
//				//实出勤占一行三列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 2));
//				cell = infoRow.createCell(cellIndex);
//				cell.setCellValue("出勤扣款");
//				cell.setCellStyle(cs2);
//				
//				subCell1 = infoRow2.createCell(cellIndex ++);
//				subCell1.setCellValue("病假");
//				subCell1.setCellStyle(cs2);
//				subCell2 = infoRow2.createCell(cellIndex ++);
//				subCell2.setCellValue("事假");
//				subCell2.setCellStyle(cs2);
//				Cell subCell3 = infoRow2.createCell(cellIndex ++);
//				subCell3.setCellValue("迟到");
//				subCell3.setCellStyle(cs2);
//				
//				//处罚金占两行一列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("处罚金");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("应发合计");
//				cell.setCellStyle(cs2);
//				
//				//个人应扣款占一行三列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 2));
//				cell = infoRow.createCell(cellIndex);
//				cell.setCellValue("个人应扣款");
//				cell.setCellStyle(cs2);
//				
//				subCell1 = infoRow2.createCell(cellIndex ++);
//				subCell1.setCellValue("社保");
//				subCell1.setCellStyle(cs2);
//				subCell2 = infoRow2.createCell(cellIndex ++);
//				subCell2.setCellValue("医保");
//				subCell2.setCellStyle(cs2);
//				subCell3 = infoRow2.createCell(cellIndex ++);
//				subCell3.setCellValue("总计");
//				subCell3.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("实发工资");
//				cell.setCellStyle(cs2);
//				
//				//补贴占一行四列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 3));
//				cell = infoRow.createCell(cellIndex);
//				cell.setCellValue("补贴");
//				cell.setCellStyle(cs2);
//				
//				subCell1 = infoRow2.createCell(cellIndex ++);
//				subCell1.setCellValue("话补");
//				subCell1.setCellStyle(cs2);
//				subCell2 = infoRow2.createCell(cellIndex ++);
//				subCell2.setCellValue("餐补");
//				subCell2.setCellStyle(cs2);
//				subCell3 = infoRow2.createCell(cellIndex ++);
//				subCell3.setCellValue("车补");
//				subCell3.setCellStyle(cs2);
//				Cell subCell4 = infoRow2.createCell(cellIndex ++);
//				subCell4.setCellValue("住房补贴");
//				subCell4.setCellStyle(cs2);
//				
//				//其他占两行一列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("其他");
//				cell.setCellStyle(cs2);
//				
//				//其他占两行一列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("税前工资");
//				cell.setCellStyle(cs2);
//				
//				//其他占两行一列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("个税");
//				cell.setCellStyle(cs2);
//				
//				//公司投保占一行两列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 2, cellIndex, cellIndex + 1));
//				cell = infoRow.createCell(cellIndex);
//				cell.setCellValue("公司投保");
//				cell.setCellStyle(cs2);
//				
//				subCell1 = infoRow2.createCell(cellIndex ++);
//				subCell1.setCellValue("社保");
//				subCell1.setCellStyle(cs2);
//				subCell2 = infoRow2.createCell(cellIndex ++);
//				subCell2.setCellValue("医保");
//				subCell2.setCellStyle(cs2);
//				
//				//备注占两行一列
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 2, rowIndex - 1, cellIndex, cellIndex));
//				cell = infoRow.createCell(cellIndex ++);
//				cell.setCellValue("备注");
//				cell.setCellStyle(cs2);
//				
//				//首行标题的合并
//				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellIndex - 1));
//				
//				//List<Map<String, Object>> salaries = ds.getMapList();
//				List<Salary> salaries = ds.getList();
//				//Iterator<Map<String, Object>> iter2 = salaries.iterator();
//				Iterator<Salary> iter2 = salaries.iterator();
//				//Map<String, Object> map = null;
//				Salary map = null;
//				Row row = null;
//				BigDecimal supposedTotal = new BigDecimal(0);
//				BigDecimal actualTotal = new BigDecimal(0);
//				BigDecimal telChargeTotal = new BigDecimal(0);
//				BigDecimal mealSubsidyTotal = new BigDecimal(0);
//				BigDecimal travelAllowanceTotal = new BigDecimal(0);
//				BigDecimal housingSubsidyTotal = new BigDecimal(0);
//				BigDecimal companySocialInsuranceTotal = new BigDecimal(0);
//				BigDecimal companyHealthInsuranceTotal = new BigDecimal(0);
//				BigDecimal benefitTotal = new BigDecimal(0);
//				BigDecimal companyInsuranceTotal = new BigDecimal(0);
//				BigDecimal totalSalary = new BigDecimal(0);
//				while(iter2.hasNext()) {
//					map = iter2.next();
//					cellIndex = 0;
//					row = sheet.createRow(rowIndex ++);
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("USER_NAME")));
//					cell.setCellValue(map.getUserName());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("SUPPOSED_DUTY_DAY")));
//					cell.setCellValue(map.getSupposedDutyDay());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_DUTY_DAY")));
//					cell.setCellValue(map.getActualDutyDay());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_DUTY_HOUR")));
//					cell.setCellValue(map.getActualDutyHour());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("BASIC_SALARY")));
//					cell.setCellValue(CodeUtil.parseString(map.getBasicSalary()));
//					
//					cell = row.createCell(cellIndex++);
//					cell.setCellValue(CodeUtil.parseString(map.getPostSalary()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACHIEVEMENT_SALARY")));
//					cell.setCellValue(CodeUtil.parseString(map.getAchievementSalary()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACHIEVEMENT_SCORE")));
//					cell.setCellValue(map.getAchievementScore());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_ACHIEVEMENT_SALARY")));
//					cell.setCellValue(CodeUtil.parseString(map.getActualAchievementSalary()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("COMMISSION")));
//					cell.setCellValue(CodeUtil.parseString(map.getCommission()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("FULL_ATTENDANCE_AWARD")));
//					cell.setCellValue(CodeUtil.parseString(map.getFullAttendanceAward()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("OVERTIME_PAY")));
//					cell.setCellValue(CodeUtil.parseString(map.getOvertimePay()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("SENIORITY_PAY")));
//					cell.setCellValue(CodeUtil.parseString(map.getSeniorityPay()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("SICK_LEAVE_DEDUCT")));
//					cell.setCellValue(CodeUtil.parseString(map.getSickLeaveDeduct()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("AFFAIR_LEAVE_DEDUCT")));
//					cell.setCellValue(CodeUtil.parseString(map.getAffairLeaveDeduct()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("LATE_DEDUCT")));
//					cell.setCellValue(CodeUtil.parseString(map.getLateDeduct()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("PUNISH_DEDUCT")));
//					cell.setCellValue(CodeUtil.parseString(map.getPunishDeduct()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("SUPPOSED_TOTAL_SALARY")));
//					cell.setCellValue(CodeUtil.parseString(map.getSupposedTotalSalary()));
//					
//					//supposedTotal = supposedTotal.add(CodeUtil.parseBigDecimal(map.get("SUPPOSED_TOTAL_SALARY")));
//					supposedTotal = supposedTotal.add(map.getSupposedTotalSalary());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("SOCIAL_INSURANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getSocialInsurance()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("HEALTH_INSURANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getHealthInsurance()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("TOTAL_INSURANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getTotalInsurance()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("ACTUAL_TOTAL_SALARY")));
//					cell.setCellValue(CodeUtil.parseString(map.getActualTotalSalary()));
//					
//					//actualTotal = actualTotal.add(CodeUtil.parseBigDecimal(map.get("ACTUAL_TOTAL_SALARY")));
//					actualTotal = actualTotal.add(map.getActualTotalSalary());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("TEL_CHARGE")));
//					cell.setCellValue(CodeUtil.parseString(map.getTelCharge()));
//					
//					//telChargeTotal = telChargeTotal.add(CodeUtil.parseBigDecimal(map.get("TEL_CHARGE")));
//					telChargeTotal = telChargeTotal.add(map.getTelCharge());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("MEAL_SUBSIDY")));
//					cell.setCellValue(CodeUtil.parseString(map.getMealSubsidy()));
//					
//					//mealSubsidyTotal = mealSubsidyTotal.add(CodeUtil.parseBigDecimal(map.get("MEAL_SUBSIDY")));
//					mealSubsidyTotal = mealSubsidyTotal.add(map.getMealSubsidy());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("TRAVEL_ALLOWANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getTravelAllowance()));
//					
//					//travelAllowanceTotal = travelAllowanceTotal.add(CodeUtil.parseBigDecimal(map.get("TRAVEL_ALLOWANCE")));
//					travelAllowanceTotal = travelAllowanceTotal.add(map.getTravelAllowance());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("HOUSING_SUBSIDY")));
//					cell.setCellValue(CodeUtil.parseString(map.getHousingSubsidy()));
//					
//					//housingSubsidyTotal = housingSubsidyTotal.add(CodeUtil.parseBigDecimal(map.get("HOUSING_SUBSIDY")));
//					housingSubsidyTotal = housingSubsidyTotal.add(map.getHousingSubsidy());
//
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
//					cell.setCellValue(CodeUtil.parseString(map.getOther()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
//					cell.setCellValue(CodeUtil.parseString(map.getPretaxSalary()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("OTHER")));
//					cell.setCellValue(CodeUtil.parseString(map.getTax()));
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("COMPANY_SOCIAL_INSURANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getCompanySocialInsurance()));
//					
//					//companySocialInsuranceTotal = companySocialInsuranceTotal.add(CodeUtil.parseBigDecimal(map.get("COMPANY_SOCIAL_INSURANCE")));
//					companySocialInsuranceTotal = companySocialInsuranceTotal.add(map.getCompanySocialInsurance());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("COMPANY_HEALTH_INSURANCE")));
//					cell.setCellValue(CodeUtil.parseString(map.getCompanyHealthInsurance()));
//					
//					//companyHealthInsuranceTotal = companyHealthInsuranceTotal.add(CodeUtil.parseBigDecimal(map.get("COMPANY_HEALTH_INSURANCE")));
//					companyHealthInsuranceTotal = companyHealthInsuranceTotal.add(map.getCompanyHealthInsurance());
//					
//					cell = row.createCell(cellIndex ++);
//					//cell.setCellValue(CodeUtil.parseString(map.get("INFO")));
//					cell.setCellValue(map.getInfo());
//				}
//				benefitTotal = benefitTotal.add(telChargeTotal).add(mealSubsidyTotal).add(travelAllowanceTotal).add(housingSubsidyTotal);
//				companyInsuranceTotal = companyInsuranceTotal.add(companySocialInsuranceTotal).add(companyHealthInsuranceTotal);
//				totalSalary = totalSalary.add(supposedTotal).add(benefitTotal).add(companyInsuranceTotal);
//				
//				Row sumRow = sheet.createRow(rowIndex ++);
//				
//				cell = sumRow.createCell(16);
//				cell.setCellValue("合计");
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(17);
//				cell.setCellValue(CodeUtil.parseString(supposedTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(21);
//				cell.setCellValue(CodeUtil.parseString(actualTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(22);
//				cell.setCellValue(CodeUtil.parseString(telChargeTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(23);
//				cell.setCellValue(CodeUtil.parseString(mealSubsidyTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(24);
//				cell.setCellValue(CodeUtil.parseString(travelAllowanceTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(25);
//				cell.setCellValue(CodeUtil.parseString(housingSubsidyTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(29);
//				cell.setCellValue(CodeUtil.parseString(companySocialInsuranceTotal));
//				cell.setCellStyle(cs2);
//				
//				cell = sumRow.createCell(30);
//				cell.setCellValue(CodeUtil.parseString(companyHealthInsuranceTotal));
//				cell.setCellStyle(cs2);
//				
//				Row benefitSumRow = sheet.createRow(rowIndex ++);
//				cell = benefitSumRow.createCell(16);
//				cell.setCellValue("福利合计");
//				cell.setCellStyle(cs2);
//				
//				cell = benefitSumRow.createCell(17);
//				cell.setCellValue(CodeUtil.parseString(supposedTotal));
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 22, 25));
//				cell = benefitSumRow.createCell(22);
//				cell.setCellValue(CodeUtil.parseString(benefitTotal));
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 29, 30));
//				cell = benefitSumRow.createCell(29);
//				cell.setCellValue(CodeUtil.parseString(companyInsuranceTotal));
//				cell.setCellStyle(cs2);
//				
//				Row totalSalaryRow = sheet.createRow(rowIndex ++);
//				
//				cell = totalSalaryRow.createCell(16);
//				cell.setCellValue("工资总计");
//				cell.setCellStyle(cs2);
//				
//				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 17, 30));
//				cell = totalSalaryRow.createCell(17);
//				cell.setCellValue(CodeUtil.parseString(totalSalary));
//				cell.setCellStyle(cs2);
//			}
		}
		return wb;
	}
}
