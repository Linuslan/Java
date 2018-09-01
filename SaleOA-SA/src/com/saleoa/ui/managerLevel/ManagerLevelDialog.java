package com.saleoa.ui.managerLevel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.JButton;
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
import com.saleoa.service.IManagerLevelService;
import com.saleoa.service.IManagerLevelServiceImpl;
import com.saleoa.ui.MainEntry;

public class ManagerLevelDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	IManagerLevelService managerLevelService = new IManagerLevelServiceImpl();
	private Long id;
	private String name = "";
	private Integer minSale=0;
	private Integer maxSale=0;
	private Long basicSalary = 0L;
	private Long reachGoalBonus = 0L;

	public void initDialog(final ManagerLevel level, final ManagerLevelPanel parent) {
		if(null != level) {
			id = level.getId();
			name = level.getName();
			this.minSale = level.getMinSale();
			this.maxSale = level.getMaxSale();
			this.basicSalary = level.getBasicSalary();
			reachGoalBonus = level.getReachGoalBonus();
		}
		final JDialog dialog = new JDialog(MainEntry.main);
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
		JLabel nameLbl = new JLabel("等级名称：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText(name);
		
		JLabel minSaleLbl = new JLabel("最低售出：");
		minSaleLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(minSaleLbl);
		minSaleLbl.setLocation(FormCss.getLocation(null, nameLbl));
		final JFormattedTextField minSaleIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		minSaleIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		minSaleIpt.setLocation(FormCss.getLocation(minSaleLbl, nameIpt));
		panel.add(minSaleIpt);
		minSaleIpt.setText(String.valueOf(minSale));
		
		JLabel maxSaleLbl = new JLabel("最高售出：");
		maxSaleLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(maxSaleLbl);
		maxSaleLbl.setLocation(FormCss.getLocation(null, minSaleLbl));
		final JFormattedTextField maxSaleIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		maxSaleIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		maxSaleIpt.setLocation(FormCss.getLocation(maxSaleLbl, minSaleIpt));
		panel.add(maxSaleIpt);
		maxSaleIpt.setText(String.valueOf(maxSale));
		
		JLabel basicSalaryLbl = new JLabel("基础工资：");
		basicSalaryLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(basicSalaryLbl);
		basicSalaryLbl.setLocation(FormCss.getLocation(null, maxSaleLbl));
		final JFormattedTextField basicSalaryIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		basicSalaryIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		basicSalaryIpt.setLocation(FormCss.getLocation(basicSalaryLbl, maxSaleIpt));
		panel.add(basicSalaryIpt);
		basicSalaryIpt.setText(String.valueOf(basicSalary/100.0));
		
		JLabel commissionLbl = new JLabel("奖金/套：");
		commissionLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(commissionLbl);
		commissionLbl.setLocation(FormCss.getLocation(null, basicSalaryLbl));
		final JFormattedTextField commissionIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		commissionIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		commissionIpt.setLocation(FormCss.getLocation(commissionLbl, basicSalaryIpt));
		panel.add(commissionIpt);
		commissionIpt.setText(String.valueOf(basicSalary/100.0));
		
		JLabel reachGoalBonusLbl = new JLabel("达标奖：");
		reachGoalBonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(reachGoalBonusLbl);
		reachGoalBonusLbl.setLocation(FormCss.getLocation(null, commissionLbl));
		final JFormattedTextField reachGoalBonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		reachGoalBonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		reachGoalBonusIpt.setLocation(FormCss.getLocation(reachGoalBonusLbl, commissionIpt));
		panel.add(reachGoalBonusIpt);
		reachGoalBonusIpt.setText(String.valueOf(reachGoalBonus/100.0));
		
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 30);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, reachGoalBonusIpt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String name = nameIpt.getText();
				String minSaleStr = minSaleIpt.getText();
				minSaleStr = minSaleStr.replaceAll(",", "");
				String maxSaleStr = maxSaleIpt.getText();
				maxSaleStr = maxSaleStr.replaceAll(",", "");
				String basicSalaryStr = basicSalaryIpt.getText();
				basicSalaryStr = basicSalaryStr.replaceAll(",", "");
				Integer minSale = Integer.parseInt(minSaleStr);
				Integer maxSale = Integer.parseInt(maxSaleStr);
				String commissionStr = commissionIpt.getText();
				commissionStr = commissionStr.replaceAll(",", "");
				Double commissionDl = Double.parseDouble(commissionStr);
				Double baiscSalaryDl = Double.parseDouble(basicSalaryStr);
				Long basicSalary = (long) (baiscSalaryDl*100);
				Long commission = (long) (commissionDl*100);
				String reachGoalBonusStr = reachGoalBonusIpt.getText();
				reachGoalBonusStr = reachGoalBonusStr.replaceAll(",", "");
				Long reachGoalBonus = (long)(Double.parseDouble(reachGoalBonusStr)*100);
				if(StringUtil.isEmpty(name)) {
					JOptionPane.showMessageDialog(dialog, "请输入等级名称", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == minSale || 0 >= minSale) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的最小售出", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(maxSale <= minSale) {
					JOptionPane.showMessageDialog(dialog, "最大售出不能小于或等于最小售出", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == basicSalary || 0 >= basicSalary) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的基础工资", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				ManagerLevel temp = new ManagerLevel();
				if(null != level) {
					BeanUtil.copyBean(level, temp);
				} else {
					temp.setCreateDate(new Date());
				}
				temp.setBasicSalary(basicSalary);
				temp.setCommission(commission);
				temp.setMaxSale(maxSale);
				temp.setMinSale(minSale);
				temp.setName(name);
				temp.setReachGoalBonus(reachGoalBonus);
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = managerLevelService.add(temp);
					} else {
						success = managerLevelService.update(temp);
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
