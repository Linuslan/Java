package com.saleoa.ui.balanceLevel;

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

import com.saleoa.common.constant.FormCss;
import com.saleoa.dao.IBalanceLevelDao;
import com.saleoa.dao.IBalanceLevelDaoImpl;
import com.saleoa.model.BalanceLevel;
import com.saleoa.ui.MainEntry;

public class BalanceLevelDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	IBalanceLevelDao balanceLevelDao = new IBalanceLevelDaoImpl();
	private Long id;
	private Long bonus = 0L;
	private Integer managerCount = 0;

	public void initDialog(final BalanceLevel balanceLevel, final BalanceLevelPanel parent) {
		if(null != balanceLevel) {
			id = balanceLevel.getId();
			managerCount = balanceLevel.getManagerCount();
			bonus = balanceLevel.getBonus();
		}
		final JDialog dialog = new JDialog(MainEntry.main);
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 400;
		int dialogHeight = 300;
		dialog.setSize(dialogWidth, dialogHeight);
		dialog.setLocation((screenSize.width-dialogWidth)/2, (screenSize.height-dialogHeight)/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
		dialog.add(panel, BorderLayout.CENTER);
		
		JLabel managerCountLbl = new JLabel("经理数：");
		managerCountLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(managerCountLbl);
		managerCountLbl.setLocation(FormCss.getLocation(null, null));
		final JFormattedTextField managerCountIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		managerCountIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(managerCountIpt);
		managerCountIpt.setLocation(FormCss.getLocation(managerCountLbl, null));
		managerCountIpt.setText(String.valueOf(this.managerCount));
		
		JLabel bonusLbl = new JLabel("奖励金：");
		bonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(bonusLbl);
		bonusLbl.setLocation(FormCss.getLocation(null, managerCountLbl));
		final JFormattedTextField bonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		bonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		bonusIpt.setLocation(FormCss.getLocation(bonusLbl, managerCountIpt));
		panel.add(bonusIpt);
		bonusIpt.setText(String.valueOf(bonus/100.0));
		
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 30);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, bonusIpt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String bonusStr = bonusIpt.getText();
				bonusStr = bonusStr.replaceAll(",", "");
				Integer managerCount = Integer.parseInt(managerCountIpt.getText());
				Double bonuslf = Double.parseDouble(bonusStr);
				Long bonus = (long) (bonuslf*100);
				BalanceLevel temp = new BalanceLevel();
				if(null != balanceLevel && null != balanceLevel.getId()) {
					temp.setId(balanceLevel.getId());
				}
				if(null != balanceLevel && null != balanceLevel.getCreateDate()) {
					temp.setCreateDate(balanceLevel.getCreateDate());
				} else {
					temp.setCreateDate(new Date());
				}
				temp.setManagerCount(managerCount);
				temp.setBonus(bonus);
				boolean success = false;
				if(null == temp.getId()) {
					success = balanceLevelDao.add(temp);
				} else {
					success = balanceLevelDao.update(temp);
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
