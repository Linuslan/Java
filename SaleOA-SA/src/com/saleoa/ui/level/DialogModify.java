package com.saleoa.ui.level;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.dao.ILevelDao;
import com.saleoa.dao.ILevelDaoImpl;
import com.saleoa.model.Level;
import com.saleoa.ui.MainEntry;

public class DialogModify {
	private static Dimension screenSize = MainEntry.getScreanSize();
	ILevelDao levelDao = new ILevelDaoImpl();
	private Long id;
	private String name = "";
	private Long minPoint=0L;
	private Long maxPoint=0L;
	private Integer level=0;
	private Long bonus = 0L;

	public void initDialog(final Level level, final LevelPanel parent) {
		if(null != level) {
			id = level.getId();
			name = level.getName();
			minPoint = level.getMinPoint();
			maxPoint = level.getMaxPoint();
			this.level = level.getLevel();
			bonus = level.getBonus();
		}
		final JDialog dialog = new JDialog();
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
		JLabel nameLbl = new JLabel("等级名称：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText(name);
		
		JLabel levelLbl = new JLabel("等级数字：");
		levelLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(levelLbl);
		levelLbl.setLocation(FormCss.getLocation(null, nameLbl));
		final JFormattedTextField levelIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		levelIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(levelIpt);
		levelIpt.setLocation(FormCss.getLocation(levelLbl, nameIpt));
		levelIpt.setText(String.valueOf(this.level));
		
		JLabel minPointLbl = new JLabel("最小积分：");
		minPointLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(minPointLbl);
		minPointLbl.setLocation(FormCss.getLocation(null, levelLbl));
		final JFormattedTextField minPointIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		minPointIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		minPointIpt.setLocation(FormCss.getLocation(minPointLbl, levelIpt));
		panel.add(minPointIpt);
		minPointIpt.setText(String.valueOf(minPoint));
		
		JLabel maxPointLbl = new JLabel("最大积分：");
		maxPointLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(maxPointLbl);
		maxPointLbl.setLocation(FormCss.getLocation(null, minPointLbl));
		final JFormattedTextField maxPointIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		maxPointIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		maxPointIpt.setLocation(FormCss.getLocation(maxPointLbl, minPointIpt));
		panel.add(maxPointIpt);
		maxPointIpt.setText(String.valueOf(maxPoint));
		
		JLabel bonusLbl = new JLabel("奖励金：");
		bonusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(bonusLbl);
		bonusLbl.setLocation(FormCss.getLocation(null, maxPointLbl));
		final JFormattedTextField bonusIpt = new JFormattedTextField(NumberFormat.getNumberInstance());
		bonusIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		bonusIpt.setLocation(FormCss.getLocation(bonusLbl, maxPointLbl));
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
				String name = nameIpt.getText();
				String minPointStr = minPointIpt.getText();
				minPointStr = minPointStr.replaceAll(",", "");
				String maxPointStr = maxPointIpt.getText();
				maxPointStr = maxPointStr.replaceAll(",", "");
				String bonusStr = bonusIpt.getText();
				bonusStr = bonusStr.replaceAll(",", "");
				Long minPoint = Long.parseLong(minPointStr);
				Long maxPoint = Long.parseLong(maxPointStr);
				Integer levelNum = Integer.parseInt(levelIpt.getText());
				Double bonuslf = Double.parseDouble(bonusStr);
				Long bonus = (long) (bonuslf*100);
				if(StringUtil.isEmpty(name)) {
					JOptionPane.showMessageDialog(dialog, "请输入等级名称", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == minPoint || 0 >= minPoint) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的最小积分", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(maxPoint <= minPoint) {
					JOptionPane.showMessageDialog(dialog, "最大积分不能小于或等于最小积分", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == levelNum || 0 >= levelNum) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的等级数字", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == bonus || 0 >= bonus) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的奖金", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				Level temp = new Level();
				if(null != level && null != level.getId()) {
					temp.setId(level.getId());
				}
				if(null != level && null != level.getCreateDate()) {
					temp.setCreateDate(level.getCreateDate());
				} else {
					temp.setCreateDate(new Date());
				}
				temp.setName(name);
				temp.setMinPoint(minPoint);
				temp.setMaxPoint(maxPoint);
				temp.setLevel(levelNum);
				temp.setBonus(bonus);
				boolean success = false;
				if(null == temp.getId()) {
					success = levelDao.add(temp);
				} else {
					success = levelDao.update(temp);
				}
				if(success) {
					dialog.dispose();
					parent.initGrid();
					JOptionPane.showMessageDialog(dialog, "保存成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
				}
				System.out.println("接收到的数据为：name="+name+", point="+minPoint);
			}
			
		});
	}
}
