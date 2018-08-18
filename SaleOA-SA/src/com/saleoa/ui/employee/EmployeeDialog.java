package com.saleoa.ui.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.ui.JAutoCompleteComboBox;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.PinyinUtil;
import com.saleoa.common.utils.StringUtil;
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
		int dialogHeight = 300;
		dialog.setSize(dialogWidth, dialogHeight);
		dialog.setLocation((screenSize.width-dialogWidth)/2, (screenSize.height-dialogHeight)/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		dialog.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(null);
		dialog.add(panel, BorderLayout.CENTER);
		JLabel nameLbl = new JLabel("������");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(nameLbl);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText(name);
		JLabel introducerLbl = new JLabel("�����ˣ�");
		introducerLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(introducerLbl);
		introducerLbl.setLocation(FormCss.getLocation(null, nameLbl));
		//final JComboBox<Employee> introducerComb = new JComboBox<Employee>();
		final JAutoCompleteComboBox<Employee> introducerComb = new JAutoCompleteComboBox<Employee>();
		List<Employee> employeeList = null;
		try {
			employeeList = this.employeeService.select(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			employeeList = new ArrayList<Employee>();
		}
		for(int i = 0; i < employeeList.size(); i ++) {
			introducerComb.addItem(employeeList.get(i));
		}
		introducerComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		introducerComb.setLocation(FormCss.getLocation(introducerLbl, nameIpt));
		panel.add(introducerComb);
		
		JLabel registerDateLbl = new JLabel("��ְʱ�䣺");
		registerDateLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(registerDateLbl);
		registerDateLbl.setLocation(FormCss.getLocation(null, introducerLbl));
		panel.add(registerDateLbl);
		
		String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
		// ��ǰʱ��
        Date date = new Date();
        // ����
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker datePicker = new DatePicker(date, DefaultFormat, font, dimension);
		datePicker.setLocation(FormCss.getLocation(registerDateLbl, introducerComb));
		datePicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		datePicker.setTimePanleVisible(true);
		panel.add(datePicker);
		
		JLabel leaderLbl = new JLabel("�ϼ��쵼��");
		leaderLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(leaderLbl);
		leaderLbl.setLocation(FormCss.getLocation(null, datePicker));
		final JAutoCompleteComboBox<Employee> leaderComb = new JAutoCompleteComboBox<Employee>();
		for(int i = 0; i < employeeList.size(); i ++) {
			leaderComb.addItem(employeeList.get(i));
		}
		
		leaderComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		leaderComb.setLocation(FormCss.getLocation(leaderLbl, datePicker));
		panel.add(leaderComb);
		JButton saveBtn = new JButton("����");
		saveBtn.setSize(60, 40);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, leaderComb);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if(null == introducerComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ�������", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(StringUtil.isEmpty(nameIpt.getText())) {
					JOptionPane.showMessageDialog(dialog, "����������", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == datePicker.getValue()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ�����ʱ��", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				Employee temp = new Employee();
				if(null != employee) {
					BeanUtil.copyBean(employee, temp);
				} else {
					temp.setCreateDate(new Date());
				}
				String name = nameIpt.getText();
				String nameEn = PinyinUtil.getStringPinYin(name);
				temp.setName(name);
				temp.setNameEn(nameEn);
				Employee introducer = (Employee)introducerComb.getSelectedItem();
				temp.setIntroducerId(introducer.getId());
				temp.setIntroducerName(introducer.getName());
				Employee leader = null;
				if(null != leaderComb.getSelectedItem()) {
					leader = (Employee) leaderComb.getSelectedItem();
					temp.setLeaderId(leader.getId());
					temp.setLeaderName(leader.getName());
				}
				temp.setRegisterDate((Date) datePicker.getValue());
				
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = employeeService.add(temp);
					} else {
						success = employeeService.update(temp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(success) {
					dialog.dispose();
					parent.initGrid();
					JOptionPane.showMessageDialog(dialog, "����ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
	}
}
