package com.saleoa.ui.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.model.Employee;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.ui.MainEntry;

public class EmployeeDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private Long id;
	private String name = "";
	private Long point=0L;

	public void initDialog(final Employee employee, final EmployeePanel parent) {
		if(null != employee) {
			id = employee.getId();
			name = employee.getName();
			point = employee.getRewardPoints();
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
		JLabel nameLbl = new JLabel("姓名：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText(name);
		JLabel introducerLbl = new JLabel("介绍人：");
		introducerLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(introducerLbl);
		introducerLbl.setLocation(FormCss.getLocation(null, nameLbl));
		final JComboBox<Employee> employeeComb = new JComboBox<Employee>();
		List<Employee> employeeList = this.employeeService.select(null);
		for(int i = 0; i < employeeList.size(); i ++) {
			employeeComb.addItem(employee);
		}
		
		employeeComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		employeeComb.setLocation(FormCss.getLocation(introducerLbl, nameIpt));
		panel.add(employeeComb);
		
		JLabel leaderLbl = new JLabel("上级领导：");
		leaderLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(leaderLbl);
		leaderLbl.setLocation(FormCss.getLocation(null, introducerLbl));
		final JComboBox<Employee> leaderComb = new JComboBox<Employee>();
		for(int i = 0; i < employeeList.size(); i ++) {
			leaderComb.addItem(employee);
		}
		
		leaderComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		leaderComb.setLocation(FormCss.getLocation(leaderLbl, employeeComb));
		panel.add(leaderComb);
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 50);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, leaderComb);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Employee selectedEmp = (Employee)employeeComb.getSelectedItem();
				System.out.println(selectedEmp.getId().toString());
				// TODO Auto-generated method stub
				/*String name = nameIpt.getText();
				String pointStr = employeeComb.getSelectedItem()
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
				Employee temp = new Employee();
				if(null != employee && null != employee.getId()) {
					temp.setId(employee.getId());
				}
				if(null != employee && null != employee.getCreateDate()) {
					temp.setCreateDate(employee.getCreateDate());
				} else {
					temp.setCreateDate(new Date());
				}
				temp.setName(name);
				temp.setRewardPoints(point);
				boolean success = false;
				if(null == temp.getId()) {
					success = employeeService.add(temp);
				} else {
					success = employeeService.update(temp);
				}
				if(success) {
					dialog.dispose();
					parent.initGrid();
					JOptionPane.showMessageDialog(dialog, "保存成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
				}
				System.out.println("接收到的数据为：name="+name+", point="+point);
				*/
			}
			
		});
	}
}
