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
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.dao.ISalaryDao;
import com.saleoa.dao.ISalaryDaoImpl;
import com.saleoa.model.Employee;
import com.saleoa.model.Salary;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;

public class SalaryDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	ISalaryDao salaryDao = new ISalaryDaoImpl();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private Long id;
	private int year;
	private int month;
	private String userName = "";
	private Long money=0l;
	private Long deductMoney = 0L;
	private String memo="";
	private Long totalMoney = 0L;	//最终工资
	private Integer status = 0;
	final JFormattedTextField salaryIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JFormattedTextField deductMoneyIpt = new JFormattedTextField(NumberFormat.getNumberInstance(Locale.CHINA));
	final JDialog dialog = new JDialog();
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
		}
		
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 400;
		int dialogHeight = 500;
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
		final JAutoCompleteComboBox<Employee> employeeComb = new JAutoCompleteComboBox<Employee>();
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
		
		JLabel salaryLbl = new JLabel("应得工资：");
		salaryLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(salaryLbl);
		salaryLbl.setLocation(FormCss.getLocation(null, nameLbl));
		
		salaryIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(salaryIpt);
		salaryIpt.setLocation(FormCss.getLocation(salaryLbl, employeeComb));
		salaryIpt.setText(String.valueOf(this.money/100.0));
		
		
		JLabel deductMoneyLbl = new JLabel("应扣款：");
		deductMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(deductMoneyLbl);
		deductMoneyLbl.setLocation(FormCss.getLocation(null, salaryLbl));
		
		deductMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		deductMoneyIpt.setLocation(FormCss.getLocation(deductMoneyLbl, salaryIpt));
		panel.add(deductMoneyIpt);
		deductMoneyIpt.setText(String.valueOf(deductMoney/100.0));
		
		JLabel totalMoneyLbl = new JLabel("最终工资：");
		totalMoneyLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(totalMoneyLbl);
		totalMoneyLbl.setLocation(FormCss.getLocation(null, deductMoneyLbl));
		
		calculateTotalMoney();
		
		totalMoneyIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		totalMoneyIpt.setLocation(FormCss.getLocation(totalMoneyLbl, deductMoneyIpt));
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
		memoTxt.setSize(FormCss.FORM_WIDTH, 80);
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
					temp.setIsDelete(0);
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
				int status = statusComb.getSelectedIndex();
				temp.setStatus(status);
				temp.setMemo(memoTxt.getText());
				boolean success = false;
				if(null == temp.getId()) {
					success = salaryDao.add(temp);
				} else {
					success = salaryDao.update(temp);
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
		Long totalMoney = money-deductMoney;
		totalMoneyIpt.setText(String.valueOf(totalMoney/100.0));
	}
}
