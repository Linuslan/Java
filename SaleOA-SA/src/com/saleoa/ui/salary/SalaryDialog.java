package com.saleoa.ui.salary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.saleoa.common.constant.EmployeeRoleConst;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Employee;
import com.saleoa.model.Salary;
import com.saleoa.service.IEmployeeRoleService;
import com.saleoa.service.IEmployeeRoleServiceImpl;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISalaryService;
import com.saleoa.service.ISalaryServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;

public class SalaryDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	ISalaryService salaryService = new ISalaryServiceImpl();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	IEmployeeRoleService employeeRoleService = new IEmployeeRoleServiceImpl();
	private Long id;
	private int year;
	private int month;
	private String userName = "";
	private Long money=0l;
	private Long deductMoney = 0L;
	private String memo="";
	private Long totalMoney = 0L;	//最终工资
	private Integer status = 0;
	private Long reachGoalBonus = 0L;
	private Long overGoalBonus = 0L;
	private Long officeManageBonus = 0L;
	private Long fullDutyBonus = 0L;
	private Long totalReachGoalBonus = 0L;
	private Long amercement = 0L;
	private Long companyLend = 0L;
	private Long tax = 0L;
	private Long supposedMoney = 0L;
	private Long directSellMoney = 0L;
	private Long balanceMoney = 0L;
	final JFormattedTextField salaryIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField directSellMoneyIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField balanceMoneyIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField deductMoneyIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField reachGoalBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField overGoalBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField officeManageBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField fullDutyBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField totalReachGoalBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField amercementIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField companyLendIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JLabel supposedMoneyIpt = new JLabel();
	final JLabel taxIpt = new JLabel();
	final JAutoCompleteComboBox<Employee> employeeComb = new JAutoCompleteComboBox<Employee>();
	final JDialog dialog = new JDialog(MainEntry.main);
	final JLabel totalMoneyIpt = new JLabel(String.valueOf(totalMoney/100.0));
	
	public void initDialog(final Salary salary, final SalaryPanel parent) {
		if(null != salary) {
			id = salary.getId();
			userName = salary.getUserName();
			this.money = salary.getMoney();
			this.deductMoney = salary.getDeductMoney();
			this.year = salary.getYear();
			this.month = salary.getMonth();
			this.memo = salary.getMemo();
			this.totalMoney = salary.getTotalMoney();
			status = salary.getStatus();
			this.reachGoalBonus = salary.getReachGoalBonus();
			this.overGoalBonus = salary.getOverGoalBonus();
			this.officeManageBonus = salary.getOfficeManageBonus();
			this.fullDutyBonus = salary.getFullDutyBonus();
			this.totalReachGoalBonus = salary.getTotalReachGoalBonus();
			this.amercement = salary.getAmercement();
			this.companyLend = salary.getCompanyLend();
			this.tax = salary.getTax();
			this.supposedMoney = salary.getSupposedMoney();
			this.directSellMoney = salary.getDirectSellMoney();
			this.balanceMoney = salary.getBalanceMoney();
		}
		
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 400;
		int dialogHeight = 800;
		dialog.setSize(dialogWidth, dialogHeight);
		dialog.setLocation((screenSize.width-dialogWidth)/2, (screenSize.height-dialogHeight)/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
		dialog.add(panel, BorderLayout.CENTER);
		JLabel yearLbl = new JLabel("年：");
		yearLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		yearLbl.setLocation(FormCss.getLocation(null, null));
		panel.add(yearLbl);
		final JComboBox<Integer> yearComb = new JComboBox<Integer>();
		yearComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		yearComb.setLocation(FormCss.getLocation(yearLbl, null));
		panel.add(yearComb);
		int year = DateUtil.getYear(new Date()) - 5;
		int yearSelectedIdx = 0;
		for(int i = 0; i < 50; i ++) {
			year ++;
			yearComb.addItem(year);
			if(null != salary && year == salary.getYear()) {
				yearSelectedIdx = i;
			}
		}
		yearComb.setSelectedIndex(yearSelectedIdx);
		panel.add(yearComb);
		if(null != salary) {
			yearComb.setEnabled(false);
		}
		
		JLabel monthLbl = new JLabel("月：");
		panel.add(monthLbl);
		monthLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		monthLbl.setLocation(FormCss.getLocation(null, yearLbl));
		final JComboBox<Integer> monthComb = new JComboBox<Integer>();
		monthComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		monthComb.setLocation(FormCss.getLocation(monthLbl, yearComb));
		panel.add(monthComb);
		int month = 0;
		int monthSelectedIdx = 0;
		for(int i = 0; i < 12; i ++) {
			month ++;
			monthComb.addItem(month);
			if(null != salary && month == salary.getMonth()) {
				monthSelectedIdx = i;
			}
		}
		monthComb.setSelectedIndex(monthSelectedIdx);
		panel.add(monthComb);
		if(null != salary) {
			monthComb.setEnabled(false);
		}
		
		JLabel nameLbl = new JLabel("员工：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, monthLbl));
		
		List<Employee> employeeList = null;
		try {
			employeeList = this.employeeService.select(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			employeeList = new ArrayList<Employee>();
		}
		Employee employee = null;
		int selectIdx = 0;
		for(int i = 0; i < employeeList.size(); i ++) {
			employee = employeeList.get(i);
			employeeComb.addItem(employee);
			if(null != salary && employee.getId().longValue() == salary.getUserId().longValue()) {
				selectIdx = i;
			}
		}
		employeeComb.setSelectedIndex(selectIdx);
		employeeComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		employeeComb.setLocation(FormCss.getLocation(nameLbl, monthComb));
		panel.add(employeeComb);
		if(null != salary) {
			employeeComb.setEnabled(false);
		}
		
		JLabel salaryLbl = new JLabel("基础工资：");
		salaryLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(salaryLbl);
		salaryLbl.setLocation(FormCss.getLocation(null, nameLbl));
		
		salaryIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(salaryIpt);
		salaryIpt.setLocation(FormCss.getLocation(salaryLbl, employeeComb));
		salaryIpt.setText(String.valueOf(this.money/100.0));
		
		JLabel reachGoalBonusLbl = new JLabel("达标奖：");
		reachGoalBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(reachGoalBonusLbl);
		reachGoalBonusLbl.setLocation(FormCss.getLocation(null, salaryLbl));
		
		reachGoalBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(reachGoalBonusIpt);
		reachGoalBonusIpt.setLocation(FormCss.getLocation(reachGoalBonusLbl, salaryIpt));
		reachGoalBonusIpt.setText(String.valueOf(this.reachGoalBonus/100.0));
		reachGoalBonusIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel directSellMoneyLbl = new JLabel("直销奖：");
		directSellMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(directSellMoneyLbl);
		directSellMoneyLbl.setLocation(FormCss.getLocation(null, reachGoalBonusLbl));
		
		directSellMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(directSellMoneyIpt);
		directSellMoneyIpt.setLocation(FormCss.getLocation(directSellMoneyLbl, reachGoalBonusIpt));
		directSellMoneyIpt.setText(String.valueOf(this.directSellMoney/100.0));
		directSellMoneyIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel balanceMoneyLbl = new JLabel("差额工资：");
		balanceMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(balanceMoneyLbl);
		balanceMoneyLbl.setLocation(FormCss.getLocation(null, directSellMoneyLbl));
		
		balanceMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(balanceMoneyIpt);
		balanceMoneyIpt.setLocation(FormCss.getLocation(balanceMoneyLbl, directSellMoneyIpt));
		balanceMoneyIpt.setText(String.valueOf(this.balanceMoney/100.0));
		
		balanceMoneyIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel overGoalBonusLbl = new JLabel("达标超额奖：");
		overGoalBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(overGoalBonusLbl);
		overGoalBonusLbl.setLocation(FormCss.getLocation(null, balanceMoneyLbl));
		
		overGoalBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(overGoalBonusIpt);
		overGoalBonusIpt.setLocation(FormCss.getLocation(overGoalBonusLbl, balanceMoneyIpt));
		overGoalBonusIpt.setText(String.valueOf(this.overGoalBonus/100.0));
		overGoalBonusIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel officeManageBonusLbl = new JLabel("内勤管理奖：");
		officeManageBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(officeManageBonusLbl);
		officeManageBonusLbl.setLocation(FormCss.getLocation(null, overGoalBonusLbl));
		
		officeManageBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(officeManageBonusIpt);
		officeManageBonusIpt.setLocation(FormCss.getLocation(officeManageBonusLbl, overGoalBonusIpt));
		officeManageBonusIpt.setText(String.valueOf(this.officeManageBonus/100.0));
		officeManageBonusIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel totalReachGoalBonusLbl = new JLabel("总达标奖：");
		totalReachGoalBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(totalReachGoalBonusLbl);
		totalReachGoalBonusLbl.setLocation(FormCss.getLocation(null, officeManageBonusLbl));
		
		totalReachGoalBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(totalReachGoalBonusIpt);
		totalReachGoalBonusIpt.setLocation(FormCss.getLocation(totalReachGoalBonusLbl, officeManageBonusIpt));
		totalReachGoalBonusIpt.setText(String.valueOf(this.totalReachGoalBonus/100.0));
		totalReachGoalBonusIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel fullDutyBonusLbl = new JLabel("满勤奖：");
		fullDutyBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(fullDutyBonusLbl);
		fullDutyBonusLbl.setLocation(FormCss.getLocation(null, totalReachGoalBonusLbl));
		
		fullDutyBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(fullDutyBonusIpt);
		fullDutyBonusIpt.setLocation(FormCss.getLocation(fullDutyBonusLbl, totalReachGoalBonusIpt));
		fullDutyBonusIpt.setText(String.valueOf(this.fullDutyBonus/100.0));
		fullDutyBonusIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel deductMoneyLbl = new JLabel("应扣款：");
		deductMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(deductMoneyLbl);
		deductMoneyLbl.setLocation(FormCss.getLocation(null, fullDutyBonusLbl));
		
		deductMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		deductMoneyIpt.setLocation(FormCss.getLocation(deductMoneyLbl, fullDutyBonusIpt));
		panel.add(deductMoneyIpt);
		deductMoneyIpt.setText(String.valueOf(deductMoney/100.0));
		deductMoneyIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel supposedMoneyLbl = new JLabel("应发工资：");
		supposedMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(supposedMoneyLbl);
		supposedMoneyLbl.setLocation(FormCss.getLocation(null, deductMoneyLbl));
		
		supposedMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		supposedMoneyIpt.setLocation(FormCss.getLocation(supposedMoneyLbl, deductMoneyIpt));
		panel.add(supposedMoneyIpt);
		supposedMoneyIpt.setText(String.valueOf(supposedMoney/100.0));
		supposedMoneyIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel taxLbl = new JLabel("个税：");
		taxLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(taxLbl);
		taxLbl.setLocation(FormCss.getLocation(null, supposedMoneyLbl));
		
		taxIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		taxIpt.setLocation(FormCss.getLocation(taxLbl, supposedMoneyIpt));
		panel.add(taxIpt);
		taxIpt.setText(String.valueOf(tax/100.0));
		taxIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel amercementLbl = new JLabel("本月罚款：");
		amercementLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(amercementLbl);
		amercementLbl.setLocation(FormCss.getLocation(null, taxLbl));
		
		amercementIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		amercementIpt.setLocation(FormCss.getLocation(amercementLbl, taxIpt));
		panel.add(amercementIpt);
		amercementIpt.setText(String.valueOf(amercement/100.0));
		amercementIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel companyLendLbl = new JLabel("公司借款：");
		companyLendLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(companyLendLbl);
		companyLendLbl.setLocation(FormCss.getLocation(null, amercementLbl));
		
		companyLendIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		companyLendIpt.setLocation(FormCss.getLocation(companyLendLbl, amercementIpt));
		panel.add(companyLendIpt);
		companyLendIpt.setText(String.valueOf(companyLend/100.0));
		companyLendIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel totalMoneyLbl = new JLabel("最终工资：");
		totalMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(totalMoneyLbl);
		totalMoneyLbl.setLocation(FormCss.getLocation(null, companyLendLbl));
		
		calculateTotalMoney();
		
		totalMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		totalMoneyIpt.setLocation(FormCss.getLocation(totalMoneyLbl, companyLendIpt));
		panel.add(totalMoneyIpt);
		
		salaryIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		deductMoneyIpt.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				calculateTotalMoney();
			}
			
		});
		
		JLabel statusLbl = new JLabel("状态：");
		statusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(statusLbl);
		statusLbl.setLocation(FormCss.getLocation(null, totalMoneyLbl));
		final JAutoCompleteComboBox<String> statusComb = new JAutoCompleteComboBox<String>();
		statusComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		statusComb.setLocation(FormCss.getLocation(statusLbl, totalMoneyIpt));
		panel.add(statusComb);
		statusComb.addItem("待审核");
		statusComb.addItem("已生效");
		if(1==status) {
			statusComb.setSelectedIndex(1);
		} else {
			statusComb.setSelectedIndex(0);
		}
		
		JLabel memoLbl = new JLabel("备注：");
		memoLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(memoLbl);
		memoLbl.setLocation(FormCss.getLocation(null, statusLbl));
		final JTextArea memoTxt = new JTextArea();
		memoTxt.setSize(FormCss.FORM_WIDTH, 50);
		memoTxt.setLocation(FormCss.getLocation(memoLbl, statusComb));
		panel.add(memoTxt);
		memoTxt.setText(memo);
		
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 30);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, memoTxt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String moneyStr = salaryIpt.getText();
				if(StringUtil.isEmpty(moneyStr)) {
					JOptionPane.showMessageDialog(dialog, "请输入应得工资", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				moneyStr = moneyStr.replaceAll(",", "");
				Double moneyDl = Double.parseDouble(moneyStr);
				Long money = (long) (moneyDl*100);
				String deductMoneyStr = deductMoneyIpt.getText();
				Long deductMoney = 0L;
				if(!StringUtil.isEmpty(deductMoneyStr)) {
					deductMoneyStr = deductMoneyStr.replaceAll(",", "");
					Double deductMoneyDl = Double.parseDouble(deductMoneyStr);
					if(moneyDl < deductMoneyDl) {
						JOptionPane.showMessageDialog(dialog, "应得工资少于应扣款，无法扣除", "温馨提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					deductMoney = (long) (deductMoneyDl*100);
				}
				Long totalMoney = money-deductMoney;
				totalMoneyIpt.setText(String.valueOf(totalMoney/100.0));
				//总达标奖
				String totalReachGoalBonusStr = totalReachGoalBonusIpt.getText();
				totalReachGoalBonusStr = totalReachGoalBonusStr.replaceAll(",", "");
				Double totalReachGoalBonusD = Double.parseDouble(totalReachGoalBonusStr);
				Long totalReachGoalBonus = (long) (totalReachGoalBonusD*100);

				//达标奖
				String reachGoalBonusStr = reachGoalBonusIpt.getText();
				reachGoalBonusStr = reachGoalBonusStr.replaceAll(",", "");
				Double reachGoalBonusD = Double.parseDouble(reachGoalBonusStr);
				Long reachGoalBonus = (long) (reachGoalBonusD*100);

				//超额奖
				String overGoalBonusStr = overGoalBonusIpt.getText();
				overGoalBonusStr = overGoalBonusStr.replaceAll(",", "");
				Double overGoalBonusD = Double.parseDouble(overGoalBonusStr);
				Long overGoalBonus = (long) (overGoalBonusD*100);

				//内勤管理奖
				String officeManageBonusStr = officeManageBonusIpt.getText();
				officeManageBonusStr = officeManageBonusStr.replaceAll(",", "");
				Double officeManageBonusD = Double.parseDouble(officeManageBonusStr);
				Long officeManageBonus = (long) (officeManageBonusD*100);

				//满勤奖
				String fullDutyBonusStr = fullDutyBonusIpt.getText();
				fullDutyBonusStr = fullDutyBonusStr.replaceAll(",", "");
				Double fullDutyBonusD = Double.parseDouble(fullDutyBonusStr);
				Long fullDutyBonus = (long) (fullDutyBonusD*100);

				//本月罚款
				String amercementStr = amercementIpt.getText();
				amercementStr = amercementStr.replaceAll(",", "");
				Double amercementD = Double.parseDouble(amercementStr);
				Long amercement = (long) (amercementD*100);

				//公司借款
				String companyLendStr = companyLendIpt.getText();
				companyLendStr = companyLendStr.replaceAll(",", "");
				Double companyLendD = Double.parseDouble(companyLendStr);
				Long companyLend = (long) (companyLendD*100);

				//直销奖
				String directSellMoneyStr = directSellMoneyIpt.getText();
				directSellMoneyStr = directSellMoneyStr.replaceAll(",", "");
				Double directSellMoneyD = Double.parseDouble(directSellMoneyStr);
				Long directSellMoney = (long) (directSellMoneyD*100);

				//差额奖
				String balanceMoneyStr = balanceMoneyIpt.getText();
				balanceMoneyStr = balanceMoneyStr.replaceAll(",", "");
				Double balanceMoneyD = Double.parseDouble(balanceMoneyStr);
				Long balanceMoney = (long) (balanceMoneyD*100);
				Salary temp = new Salary();
				if(null != salary) {
					BeanUtil.copyBean(salary, temp);
				} else {
					temp.setCreateDate(new Date());
					if(null == employeeComb.getSelectedItem()) {
						JOptionPane.showMessageDialog(dialog, "请选择人员", "温馨提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					Employee employee = (Employee) employeeComb.getSelectedItem();
					if(0 == employee.getId()) {
						JOptionPane.showMessageDialog(dialog, "请选择人员", "温馨提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					temp.setUserId(employee.getId());
					temp.setUserName(employee.getName());
					temp.setStatus(0);
					if(null == yearComb.getSelectedItem()) {
						JOptionPane.showMessageDialog(dialog, "请选择年份", "温馨提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					temp.setYear((Integer) yearComb.getSelectedItem());
					
					if(null == monthComb.getSelectedItem()) {
						JOptionPane.showMessageDialog(dialog, "请选择月份", "温馨提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					temp.setMonth((Integer) monthComb.getSelectedItem());
				}
				temp.setMoney(money);
				temp.setDeductMoney(deductMoney);
				temp.setTotalMoney(totalMoney);
				temp.setUpdateDate(new Date());
				temp.setTotalReachGoalBonus(totalReachGoalBonus);
				temp.setAmercement(amercement);
				temp.setBalanceMoney(balanceMoney);
				temp.setFullDutyBonus(fullDutyBonus);
				temp.setOfficeManageBonus(officeManageBonus);
				temp.setOverGoalBonus(overGoalBonus);
				temp.setReachGoalBonus(reachGoalBonus);
				temp.setDirectSellMoney(directSellMoney);
				temp.setCompanyLend(companyLend);
				int status = statusComb.getSelectedIndex();
				temp.setStatus(status);
				temp.setMemo(memoTxt.getText());
				temp.setSupposedMoney(salaryService.getSupposedMoney(temp));
				temp.setTotalMoney(salaryService.getTotalSalary(temp));
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = salaryService.add(temp);
					} else {
						success = salaryService.update(temp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(success) {
					dialog.dispose();
					parent.refresh();
					JOptionPane.showMessageDialog(dialog, "保存成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
	}
	
	public void calculateTotalMoney() {
		
		if(null == employeeComb.getSelectedItem()) {
			JOptionPane.showMessageDialog(dialog, "请选择人员", "温馨提示",JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		Employee employee = (Employee) employeeComb.getSelectedItem();
		Long employeeRoleId = employee.getEmployeeRoleId();
		
		String moneyStr = salaryIpt.getText();
		if(StringUtil.isEmpty(moneyStr)) {
			moneyStr = "0";
		}
		moneyStr = moneyStr.replaceAll(",", "");
		Double moneyDl = Double.parseDouble(moneyStr);
		Long money = (long) (moneyDl*100);
		String deductMoneyStr = deductMoneyIpt.getText();
		Long deductMoney = 0L;
		if(!StringUtil.isEmpty(deductMoneyStr)) {
			deductMoneyStr = deductMoneyStr.replaceAll(",", "");
			Double deductMoneyDl = Double.parseDouble(deductMoneyStr);
			if(moneyDl < deductMoneyDl) {
				JOptionPane.showMessageDialog(dialog, "应得工资少于应扣款，无法扣除", "温馨提示",JOptionPane.WARNING_MESSAGE);
				return;
			}
			deductMoney = (long) (deductMoneyDl*100);
		}
		
		String reachGoalBonusStr = reachGoalBonusIpt.getText();
		reachGoalBonusStr = reachGoalBonusStr.replaceAll(",", "");
		Double reachGoalBonusDl = Double.parseDouble(reachGoalBonusStr);
		Long reachGoalBonus = (long) (reachGoalBonusDl*100);
		
		String directSellMoneyStr = directSellMoneyIpt.getText();
		directSellMoneyStr = directSellMoneyStr.replaceAll(",", "");
		Double directSellMoneyDl = Double.parseDouble(directSellMoneyStr);
		Long directSellMoney = (long) (directSellMoneyDl*100);
		
		String balanceMoneyStr = balanceMoneyIpt.getText();
		balanceMoneyStr = balanceMoneyStr.replaceAll(",", "");
		Double balanceMoneyDl = Double.parseDouble(balanceMoneyStr);
		Long balanceMoney = (long) (balanceMoneyDl*100);
		
		String overGoalBonusStr = overGoalBonusIpt.getText();
		overGoalBonusStr = overGoalBonusStr.replaceAll(",", "");
		Double overGoalBonusDl = Double.parseDouble(overGoalBonusStr);
		Long overGoalBonus = (long) (overGoalBonusDl*100);
		
		String officeManageBonusStr = officeManageBonusIpt.getText();
		officeManageBonusStr = officeManageBonusStr.replaceAll(",", "");
		Double officeManageBonusDl = Double.parseDouble(officeManageBonusStr);
		Long officeManageBonus = (long) (officeManageBonusDl*100);
		
		String fullDutyBonusStr = fullDutyBonusIpt.getText();
		fullDutyBonusStr = fullDutyBonusStr.replaceAll(",", "");
		Double fullDutyBonusDl = Double.parseDouble(fullDutyBonusStr);
		Long fullDutyBonus = (long) (fullDutyBonusDl*100);
		
		String totalReachGoalBonusStr = totalReachGoalBonusIpt.getText();
		totalReachGoalBonusStr = totalReachGoalBonusStr.replaceAll(",", "");
		Double totalReachGoalBonusDl = Double.parseDouble(totalReachGoalBonusStr);
		Long totalReachGoalBonus = (long) (totalReachGoalBonusDl*100);
		
		String amercementStr = amercementIpt.getText();
		amercementStr = amercementStr.replaceAll(",", "");
		Double amercementDl = Double.parseDouble(amercementStr);
		Long amercement = (long) (amercementDl*100);
		
		String companyLendStr = companyLendIpt.getText();
		companyLendStr = companyLendStr.replaceAll(",", "");
		Double companyLendDl = Double.parseDouble(companyLendStr);
		Long companyLend = (long) (companyLendDl*100);
		
		Long supposedMoney = reachGoalBonus + directSellMoney + balanceMoney + overGoalBonus + officeManageBonus
				+ fullDutyBonus + totalReachGoalBonus + money - deductMoney;
		supposedMoneyIpt.setText(String.valueOf(supposedMoney/100.0));
		Long tax = 0L;
		if(employeeRoleId.longValue() == EmployeeRoleConst.MANAGER) {	//经理级别需要扣税
			tax = salaryService.getTax(supposedMoney);
			taxIpt.setText(String.valueOf(tax/100.0));
		}
		
		Long totalMoney = supposedMoney - tax - amercement - companyLend;
		totalMoneyIpt.setText(String.valueOf(totalMoney/100.0));
	}
}
