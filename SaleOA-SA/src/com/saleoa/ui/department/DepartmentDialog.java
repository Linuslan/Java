package com.saleoa.ui.department;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.dao.IDepartmentDao;
import com.saleoa.dao.IDepartmentDaoImpl;
import com.saleoa.model.Department;
import com.saleoa.ui.MainEntry;

public class DepartmentDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	IDepartmentDao departmentDao = new IDepartmentDaoImpl();
	private Long id;
	private String name = "";
	private String memo = "";

	public void initDialog(final Department department, final DepartmentPanel parent) {
		if(null != department) {
			id = department.getId();
			name = department.getName();
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
		JLabel nameLbl = new JLabel("班级名称：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText(name);
		
		JLabel memoLbl = new JLabel("备注：");
		memoLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(memoLbl);
		memoLbl.setLocation(FormCss.getLocation(null, nameLbl));
		final JTextArea memoTxt = new JTextArea();
		memoTxt.setSize(FormCss.FORM_WIDTH, 80);
		memoTxt.setLocation(FormCss.getLocation(memoLbl, nameIpt));
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
				String name = nameIpt.getText();
				String memo = memoTxt.getText();
				if(StringUtil.isEmpty(name)) {
					JOptionPane.showMessageDialog(dialog, "请输入班级名称", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				Department temp = new Department();
				if(null != department && null != department.getId()) {
					temp.setId(department.getId());
				}
				if(null != department && null != department.getCreateDate()) {
					temp.setCreateDate(department.getCreateDate());
				} else {
					temp.setCreateDate(new Date());
				}
				temp.setName(name);
				if(!StringUtil.isEmpty(memo)) {
					temp.setMemo(memo);
				}
				boolean success = false;
				if(null == temp.getId()) {
					success = departmentDao.add(temp);
				} else {
					success = departmentDao.update(temp);
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
