package com.saleoa.ui.level;

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
	private Long point=0L;

	public void initDialog(final Level level, final LevelPanel parent) {
		if(null != level) {
			id = level.getId();
			name = level.getName();
			point = level.getRewardPoints();
		}
		final JDialog dialog = new JDialog();
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 400;
		int dialogHeight = 250;
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
		JLabel pointLbl = new JLabel("升级积分：");
		pointLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(pointLbl);
		pointLbl.setLocation(FormCss.getLocation(null, nameLbl));
		final JFormattedTextField pointIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		pointIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		pointIpt.setLocation(FormCss.getLocation(pointLbl, nameIpt));
		panel.add(pointIpt);
		pointIpt.setText(String.valueOf(point));
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 50);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, pointIpt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String name = nameIpt.getText();
				String pointStr = pointIpt.getText();
				pointStr = pointStr.replaceAll(",", "");
				Long point = Long.parseLong(pointStr);
				if(StringUtil.isEmpty(name)) {
					JOptionPane.showMessageDialog(dialog, "请输入等级名称", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == point || 0 >= point) {
					JOptionPane.showMessageDialog(dialog, "请输入有效的升级积分", "温馨提示",JOptionPane.WARNING_MESSAGE);
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
				temp.setRewardPoints(point);
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
				System.out.println("接收到的数据为：name="+name+", point="+point);
			}
			
		});
	}
}
