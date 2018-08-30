package com.saleoa.ui.salaryConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.ManagerLevel;
import com.saleoa.model.SalaryConfig;
import com.saleoa.service.IManagerLevelService;
import com.saleoa.service.IManagerLevelServiceImpl;
import com.saleoa.service.ISalaryConfigService;
import com.saleoa.service.ISalaryConfigServiceImpl;
import com.saleoa.ui.MainEntry;

public class SalaryConfigDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
	private Long id;
	private Long totalReachGoalBonus = 0L;
	private int taxRate=0;
	private Long taxThreshold=0L;
	private int salaryStartDay = 1;
	private int salaryEndDay = 1;

	public void initDialog(final SalaryConfig salaryConfig, final SalaryConfigPanel parent) {
		if(null != salaryConfig) {
			id = salaryConfig.getId();
			totalReachGoalBonus = salaryConfig.getTotalReachGoalBonus();
			this.taxRate = salaryConfig.getTaxRate();
			this.taxThreshold = salaryConfig.getTaxThreshold();
			this.salaryStartDay = salaryConfig.getSalaryStartDay();
			this.salaryEndDay = salaryConfig.getSalaryEndDay();
		}
		final JDialog dialog = new JDialog();
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 400;
		int dialogHeight = 350;
		dialog.setSize(dialogWidth, dialogHeight);
		dialog.setLocation((screenSize.width-dialogWidth)/2, (screenSize.height-dialogHeight)/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
		dialog.add(panel, BorderLayout.CENTER);
		JLabel totalReachGoalBonusLbl = new JLabel("总达标奖：");
		totalReachGoalBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(totalReachGoalBonusLbl);
		totalReachGoalBonusLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField totalReachGoalBonusIpt = new JTextField();
		totalReachGoalBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(totalReachGoalBonusIpt);
		totalReachGoalBonusIpt.setLocation(FormCss.getLocation(totalReachGoalBonusLbl, null));
		totalReachGoalBonusIpt.setText(String.valueOf(totalReachGoalBonus/100.0));
		
		JLabel taxRateLbl = new JLabel("税率：");
		taxRateLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(taxRateLbl);
		taxRateLbl.setLocation(FormCss.getLocation(null, totalReachGoalBonusLbl));
		final JFormattedTextField taxRateIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		taxRateIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		taxRateIpt.setLocation(FormCss.getLocation(taxRateLbl, totalReachGoalBonusIpt));
		panel.add(taxRateIpt);
		taxRateIpt.setText(String.valueOf(taxRate));
		
		JLabel taxThresholdLbl = new JLabel("起征点：");
		taxThresholdLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(taxThresholdLbl);
		taxThresholdLbl.setLocation(FormCss.getLocation(null, taxRateLbl));
		final JFormattedTextField taxThresholdIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		taxThresholdIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		taxThresholdIpt.setLocation(FormCss.getLocation(taxRateLbl, taxRateIpt));
		panel.add(taxThresholdIpt);
		taxThresholdIpt.setText(String.valueOf(taxThreshold/100.0));
		
		JLabel salaryStartDayLbl = new JLabel("工资结算日期始：");
		salaryStartDayLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(salaryStartDayLbl);
		salaryStartDayLbl.setLocation(FormCss.getLocation(null, taxThresholdLbl));
		final JFormattedTextField salaryStartDayIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		salaryStartDayIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		salaryStartDayIpt.setLocation(FormCss.getLocation(salaryStartDayLbl, taxThresholdIpt));
		panel.add(salaryStartDayIpt);
		salaryStartDayIpt.setText(String.valueOf(salaryStartDay));
		
		JLabel salaryEndDayLbl = new JLabel("工资结算日期止：");
		salaryEndDayLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(salaryEndDayLbl);
		salaryEndDayLbl.setLocation(FormCss.getLocation(null, salaryStartDayLbl));
		final JFormattedTextField salaryEndDayIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		salaryEndDayIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		salaryEndDayIpt.setLocation(FormCss.getLocation(salaryEndDayLbl, salaryStartDayIpt));
		panel.add(salaryEndDayIpt);
		salaryEndDayIpt.setText(String.valueOf(salaryEndDay));
		
		JLabel monthStepLbl = new JLabel("是否跨月：");
		monthStepLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(monthStepLbl);
		monthStepLbl.setLocation(FormCss.getLocation(null, salaryStartDayLbl));
		final JComboBox<String> monthStepComb = new JComboBox<String> ();
		monthStepComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		monthStepComb.setLocation(FormCss.getLocation(monthStepLbl, salaryEndDayIpt));
		panel.add(monthStepComb);
		monthStepComb.addItem("不跨月");
		monthStepComb.addItem("跨月");
		if(null != salaryConfig) {
			monthStepComb.setSelectedIndex(salaryConfig.getMonthStep());
		}
		
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 30);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, salaryEndDayIpt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String totalReachGoalBonusStr = totalReachGoalBonusIpt.getText();
				totalReachGoalBonusStr = totalReachGoalBonusStr.replaceAll(",", "");
				Long totalReachGoalBonus = (long)(Double.parseDouble(totalReachGoalBonusStr)*100);
				String taxRateStr = taxRateIpt.getText();
				taxRateStr = taxRateStr.replaceAll(",", "");
				int taxRate = Integer.parseInt(taxRateStr);
				String taxThresholdStr = taxThresholdIpt.getText();
				taxThresholdStr = taxThresholdStr.replaceAll(",", "");
				Long taxThreshold = (long)(Double.parseDouble(taxThresholdStr)*100);
				String salaryStartDayStr = salaryStartDayIpt.getText();
				Integer salaryStartDay = Integer.parseInt(salaryStartDayStr);
				String salaryEndDayStr = salaryEndDayIpt.getText();
				Integer salaryEndDay = Integer.parseInt(salaryEndDayStr);
				
				SalaryConfig temp = new SalaryConfig();
				if(null != salaryConfig) {
					BeanUtil.copyBean(salaryConfig, temp);
				}
				temp.setSalaryEndDay(salaryEndDay);
				temp.setSalaryStartDay(salaryStartDay);
				temp.setTaxRate(taxRate);
				temp.setTaxThreshold(taxThreshold);
				temp.setTotalReachGoalBonus(totalReachGoalBonus);
				temp.setMonthStep(monthStepComb.getSelectedIndex());
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = salaryConfigService.add(temp);
					} else {
						success = salaryConfigService.update(temp);
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				if(success) {
					dialog.dispose();
					parent.initGrid();
					JOptionPane.showMessageDialog(dialog, "保存成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
	}
}
