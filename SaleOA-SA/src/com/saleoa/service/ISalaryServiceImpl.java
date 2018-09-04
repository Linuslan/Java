package com.saleoa.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
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
import com.saleoa.model.BalanceLevel;
import com.saleoa.model.Department;
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
				Department department = this.departmentDao.selectById(departmentId);
				salary.setDepartmentId(departmentId);
				salary.setDepartmentName(department.getName());
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
					try {
						ManagerLevel managerLevel = this.managerLevelDao.selectBySale(saleCount, employee.getDepartmentId());
						mngSalary.setMoney(managerLevel.getBasicSalary());
						mngSalary.setReachGoalBonus(managerLevel.getReachGoalBonus());
						mngSalary.setOverGoalBonus(managerLevel.getCommission()*(saleCount-(managerLevel.getMinSale()-1)));
						//销售数大于最大的销售数，则有超额达标奖
						if(0 < managerLevel.getReachGoalBonus() && saleCount >= managerLevel.getMinSale()) {
							overGoalCount ++;
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
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
					mngSalary.setOfficeManageBonus(30000l);
					List<Salary> mngSalarys = mngSalaryMap.get(departmentId.longValue());
					if(null == mngSalarys) {
						mngSalarys = new ArrayList<Salary> ();
						mngSalaryMap.put(departmentId.longValue(), mngSalarys);
					}
					BalanceLevel balanceLevel = this.saleDao.getBalanceLevelByEmployeeId(employee.getId());
					String endDateStr = DateUtil.getCustomEndDateStr(year, month);
					Map<String, Object> paramMap = new HashMap<String, Object> ();
					paramMap.put("saleDate>=", dateStr);
					paramMap.put("saleDate<=", endDateStr);
					long totalSaleCount = this.saleDao.getMinSaleCount(employee.getId(), paramMap);
					long balanceMoney = balanceLevel.getBonus()*totalSaleCount;
					mngSalary.setBalanceMoney(balanceMoney);
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
		return salary.getMoney() + salary.getDirectSellMoney() + salary.getBalanceMoney() + salary.getReachGoalBonus() + salary.getOverGoalBonus() + salary.getOfficeManageBonus() + salary.getFullDutyBonus() + salary.getTotalReachGoalBonus()
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
	
	/**
	 * 导出工资
	 * @param year
	 * @param month
	 */
	public void export(int year, int month) {
		try {
			String fileName = System.getProperty("user.dir").replace("\\", "/")+"/export/"+year+"年"+month+"月员工薪资.xls";
			String fileName2 = System.getProperty("user.dir").replace("\\", "/")+"/export/"+year+"年"+month+"月B级别员工薪资.xls";
			List<Salary> salaryList = this.salaryDao.queryByYearAndMonth(year, month);
			Map<String, List<Salary>> deptMap = new HashMap<String, List<Salary>> ();
			List<Salary> mngList = new ArrayList<Salary> ();
			Salary salary = null;
			for(int i = 0; i < salaryList.size(); i ++) {
				salary = salaryList.get(i);
				Long departmentId = salary.getDepartmentId();
				String departmentName = salary.getDepartmentName();
				Long employeeId = salary.getUserId();
				Employee employee = this.employeeDao.selectById(employeeId);
				List<Salary> deptList = deptMap.get(departmentName);
				if(null == deptList) {
					deptList = new ArrayList<Salary> ();
					deptMap.put(departmentName, deptList);
				}
				if(employee.getEmployeeRoleId().longValue() == EmployeeRoleConst.MANAGER.longValue()) {
					mngList.add(salary);
				} else {
					deptList.add(salary);
				}
			}
			HSSFWorkbook wb = this.createEmployeeExcel(month+"", deptMap);
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			wb.write(fos);
			fos.flush();
			fos.close();
			
			wb = this.createManagerSalaryExcel(month+"", mngList);
			fos = new FileOutputStream(new File(fileName2));
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public HSSFWorkbook createEmployeeExcel(String month, Map<String, List<Salary>> deptMap) {
		HSSFWorkbook wb = new HSSFWorkbook();
		CellStyle cs = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(font);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setBorderBottom((short)1);
		cs.setBorderLeft((short)1);
		cs.setBorderRight((short)1);
		cs.setBorderTop((short)1);
		cs.setWrapText(true);
		
		Font font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 20);
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle csNoBorder = wb.createCellStyle();
		csNoBorder.setFont(font);
		csNoBorder.setAlignment(CellStyle.ALIGN_CENTER);
		csNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		csNoBorder.setWrapText(true);
		
		CellStyle csNoBorderBigFont = wb.createCellStyle();
		csNoBorderBigFont.setFont(font2);
		csNoBorderBigFont.setAlignment(CellStyle.ALIGN_CENTER);
		csNoBorderBigFont.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		csNoBorderBigFont.setWrapText(true);
		if(null != deptMap) {
			Set<Entry<String, List<Salary>>> entrySet = deptMap.entrySet();
			Iterator<Entry<String, List<Salary>>> iter = entrySet.iterator();
			Entry<String, List<Salary>> entry = null;
			while(iter.hasNext()) {
				entry = iter.next();
				String departmentName = entry.getKey();
				List<Salary> deptList = entry.getValue();
				HSSFSheet sheet = wb.createSheet(departmentName);
				sheet.setColumnWidth(0, 18*256);
				sheet.setColumnWidth(1, 18*256);
				sheet.setColumnWidth(2, 15*256);
				sheet.setColumnWidth(3, 18*256);
				sheet.setColumnWidth(4, 25*256);
				sheet.setColumnWidth(5, 20*256);
				int count = deptList.size() % 25 == 0 ? deptList.size() / 25 : deptList.size() / 25 + 1;
				int i = 0;
				int rowIndex = 0;
				int rowHeight = 24;
				for(int k = 0; k < count; k ++) {
					sheet.setDefaultRowHeightInPoints(rowHeight);
					HSSFRow titleRow = sheet.createRow(rowIndex ++);
					titleRow.setHeightInPoints(rowHeight);
					sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 5));
					HSSFCell cell = titleRow.createCell(0);
					cell.setCellStyle(csNoBorderBigFont);
					cell.setCellValue("香港新时空（国际）集团化妆品有限公司");
					HSSFRow titleRow2 = sheet.createRow(rowIndex ++);
					titleRow2.setHeightInPoints(rowHeight);
					sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 5));
					cell = titleRow2.createCell(0);
					cell.setCellValue(month+"月工资表");
					cell.setCellStyle(csNoBorder);
					HSSFRow headerRow = sheet.createRow(rowIndex++);
					headerRow.setHeightInPoints(40);
					int headerCell = 0;
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue("姓名");
					cell.setCellStyle(cs);
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue(new HSSFRichTextString("应发工资\r\n（元）"));
					cell.setCellStyle(cs);
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue("税收");
					cell.setCellStyle(cs);
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue("实发工资\r\n（元）");
					cell.setCellStyle(cs);
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue("签收");
					cell.setCellStyle(cs);
					cell = headerRow.createCell(headerCell++);
					cell.setCellValue("备注");
					cell.setCellStyle(cs);
					int end = i + 25 > deptList.size() ? deptList.size() : i + 25;
					Long supposedMoneySum = 0L;
					Long totalMoneySum = 0L;
					for(; i < 25; i ++) {
						int cellIndex = 0;
						Row row = sheet.createRow(rowIndex ++);
						row.setHeightInPoints(rowHeight);
						Cell nameCell = row.createCell(cellIndex++);
						Cell supposedMoneyCell = row.createCell(cellIndex ++);
						Cell taxCell = row.createCell(cellIndex ++);
						Cell totalMoneyCell = row.createCell(cellIndex++);
						Cell signCell = row.createCell(cellIndex ++);
						Cell memoCell = row.createCell(cellIndex ++);
						String userName = "";
						String supposedMoney = "";
						String tax = "";
						String totalMoney = "";
						if(deptList.size()>i) {
							Salary salary = deptList.get(i);
							userName = salary.getUserName();
							supposedMoney = String.valueOf(salary.getSupposedMoney()/100.0);
							supposedMoneySum += salary.getSupposedMoney();
							tax = String.valueOf(salary.getTax()/100.0);
							totalMoney = String.valueOf(salary.getTotalMoney()/100.0);
							totalMoneySum += salary.getTotalMoney();
						}
						row.setHeightInPoints(rowHeight);
						nameCell.setCellStyle(cs);
						nameCell.setCellValue(userName);
						supposedMoneyCell.setCellValue(supposedMoney);
						supposedMoneyCell.setCellStyle(cs);
						taxCell.setCellValue(tax);
						taxCell.setCellStyle(cs);
						totalMoneyCell.setCellValue(totalMoney);
						totalMoneyCell.setCellStyle(cs);
						signCell.setCellValue("");
						signCell.setCellStyle(cs);
						memoCell.setCellValue("");
						memoCell.setCellStyle(cs);
					}
					HSSFRow sumRow = sheet.createRow(rowIndex++);
					sumRow.setHeightInPoints(rowHeight);
					cell = sumRow.createCell(0);
					cell.setCellStyle(cs);
					cell.setCellValue("本页小计");
					cell = sumRow.createCell(1);
					cell.setCellStyle(cs);
					cell.setCellValue(String.valueOf(supposedMoneySum/100.0));
					cell = sumRow.createCell(2);
					cell.setCellStyle(cs);
					cell.setCellValue(0);
					cell = sumRow.createCell(3);
					cell.setCellStyle(cs);
					cell.setCellValue(String.valueOf(totalMoneySum/100.0));
					cell = sumRow.createCell(4);
					cell.setCellStyle(cs);
					cell.setCellValue("");
					cell = sumRow.createCell(5);
					cell.setCellStyle(cs);
					cell.setCellValue("");
					
					HSSFRow tailRow = sheet.createRow(rowIndex);
					tailRow.setHeightInPoints(rowHeight);
					cell = tailRow.createCell(4);
					cell.setCellStyle(csNoBorder);
					cell.setCellValue("公司盖章");
				}
			}
		}
		return wb;
	}
	
	public HSSFWorkbook createManagerSalaryExcel(String month, List<Salary> list) {
		HSSFWorkbook wb = new HSSFWorkbook();
		CellStyle cs = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(font);
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setBorderBottom((short)1);
		cs.setBorderLeft((short)1);
		cs.setBorderRight((short)1);
		cs.setBorderTop((short)1);
		cs.setWrapText(true);
		
		Font font2 = wb.createFont();
		font2.setFontHeightInPoints((short) 20);
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		
		CellStyle csNoBorder = wb.createCellStyle();
		csNoBorder.setFont(font);
		csNoBorder.setAlignment(CellStyle.ALIGN_CENTER);
		csNoBorder.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		csNoBorder.setWrapText(true);
		
		CellStyle csNoBorderBigFont = wb.createCellStyle();
		csNoBorderBigFont.setFont(font2);
		csNoBorderBigFont.setAlignment(CellStyle.ALIGN_CENTER);
		csNoBorderBigFont.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		csNoBorderBigFont.setWrapText(true);
		int rowIndex = 0;
		HSSFSheet sheet = wb.createSheet();
		sheet.setDefaultColumnWidth(10);
		HSSFRow companyInfoRow = sheet.createRow(rowIndex ++);
		companyInfoRow.setHeightInPoints(40);
		HSSFCell cell = companyInfoRow.createCell(0);
		cell.setCellStyle(csNoBorderBigFont);
		cell.setCellValue("香港新时空（国际）集团化妆品有限公司Ｂ级别销售工资明细表");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15));
		HSSFRow titleRow = sheet.createRow(rowIndex++);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15));
		titleRow.setHeightInPoints(25);
		cell = titleRow.createCell(0);
		cell.setCellValue(month+"月工资");
		cell.setCellStyle(csNoBorder);
		int rowHeight = 20;
		HSSFRow headerRow = sheet.createRow(rowIndex++);
		headerRow.setHeightInPoints(45);
		int cellIndex = 0;
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("姓名");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("保底工资");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex++);
		cell.setCellValue("达标奖金");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("直销奖");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("差额");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("达标超额奖");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("内勤管理补助");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex++);
		cell.setCellValue("全勤奖");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex++);
		cell.setCellValue("应扣其他");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("总达标奖");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("应发工资");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("3500以上纳税20%");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("本月罚款");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("扣借公司款");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("实发工资");
		cell.setCellStyle(cs);
		cell = headerRow.createCell(cellIndex ++);
		cell.setCellValue("签名");
		cell.setCellStyle(cs);
		
		cellIndex = 0;
		for(int i = 0; i < list.size(); i ++) {
			Salary salary = list.get(i);
			HSSFRow row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(rowHeight);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(salary.getUserName());
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getMoney()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getReachGoalBonus()/100.0));
			cell.setCellStyle(cs);
			
			//直销奖
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getDirectSellMoney()/100.0));
			cell.setCellStyle(cs);
			
			//差额
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getBalanceMoney()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getOverGoalBonus()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getOfficeManageBonus()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getFullDutyBonus()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getDeductMoney()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getTotalReachGoalBonus()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getSupposedMoney()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getTax()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getAmercement()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getCompanyLend()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(salary.getTotalMoney()/100.0));
			cell.setCellStyle(cs);
			
			cell = row.createCell(cellIndex++);
			cell.setCellValue("");
			cell.setCellStyle(cs);
			
		}
		
		HSSFRow tailRow = sheet.createRow(rowIndex++);
		cell = tailRow.createCell(0);
		cell.setCellStyle(csNoBorder);
		cell.setCellValue("董事会：");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 1));
		
		cell = tailRow.createCell(6);
		cell.setCellStyle(csNoBorder);
		cell.setCellValue("总经理：");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 6, 7));
		
		cell = tailRow.createCell(12);
		cell.setCellStyle(csNoBorder);
		cell.setCellValue("财务：");
		sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1, 12, 13));
		
		return wb;
	}
}
