package com.saleoa.ui.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.PinyinUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Department;
import com.saleoa.model.Employee;
import com.saleoa.model.EmployeeRole;
import com.saleoa.service.IDepartmentService;
import com.saleoa.service.IDepartmentServiceImpl;
import com.saleoa.service.IEmployeeRoleService;
import com.saleoa.service.IEmployeeRoleServiceImpl;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;

public class EmployeeDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	IDepartmentService departmentService = new IDepartmentServiceImpl();
	IEmployeeRoleService employeeRoleService = new IEmployeeRoleServiceImpl();
	private Long id;
	private String name = "";
	private Integer status = 0;
	private Long employeeRoleId = 0L;
	private String idNumber = "";
	private String address = "";
	private String inheritor = "";
	private String inheritorPhone = "";
	private Long departmentId = 0L;

	public void initDialog(final Employee employee, final EmployeePanel parent) {
		if(null != employee) {
			id = employee.getId();
			name = employee.getName();
			status = employee.getStatus();
			employeeRoleId = employee.getEmployeeRoleId();
			idNumber = employee.getIdNumber();
			address = employee.getAddress();
			inheritor = employee.getInheritor();
			inheritorPhone = employee.getInheritorPhone();
			departmentId = employee.getDepartmentId();
		}
		final JDialog dialog = new JDialog(MainEntry.main);
		dialog.setBackground(Color.WHITE);
		int dialogWidth = 480;
		int dialogHeight = 450;
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
		
		JLabel registerDateLbl = new JLabel("��ְʱ�䣺");
		registerDateLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(registerDateLbl);
		//registerDateLbl.setLocation(FormCss.getLocation(null, introducerLbl));
		registerDateLbl.setLocation(FormCss.getLocation(null, nameLbl));
		panel.add(registerDateLbl);
		
		String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
		// ��ǰʱ��
        Date date = new Date();
        // ����
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker datePicker = new DatePicker(date, DefaultFormat, font, dimension);
		//datePicker.setLocation(FormCss.getLocation(registerDateLbl, introducerComb));
		datePicker.setLocation(FormCss.getLocation(registerDateLbl, nameIpt));
		datePicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		datePicker.setTimePanleVisible(true);
		panel.add(datePicker);
		
		JLabel employeeRoleLbl = new JLabel("ְ��");
		employeeRoleLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(employeeRoleLbl);
		employeeRoleLbl.setLocation(FormCss.getLocation(null, registerDateLbl));
		panel.add(employeeRoleLbl);
		
		final JAutoCompleteComboBox<EmployeeRole> employeeRoleComb = new JAutoCompleteComboBox<EmployeeRole>();
		employeeRoleComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		employeeRoleComb.setLocation(FormCss.getLocation(employeeRoleLbl, datePicker));
		panel.add(employeeRoleComb);
		try {
			List<EmployeeRole> employeeRoleList = this.employeeRoleService.select(null);
			int selectedIdx = 0;
			for(int i = 0; i < employeeRoleList.size(); i ++) {
				EmployeeRole role = employeeRoleList.get(i);
				employeeRoleComb.addItem(role);
				if(null != employeeRoleId && role.getId() == employeeRoleId) {
					selectedIdx = i;
				}
			}
			employeeRoleComb.setSelectedIndex(selectedIdx);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel departmentLbl = new JLabel("�༶��");
		departmentLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(departmentLbl);
		departmentLbl.setLocation(FormCss.getLocation(null, employeeRoleLbl));
		panel.add(departmentLbl);
		final JAutoCompleteComboBox<Department> departmentComb = new JAutoCompleteComboBox<Department>();
		departmentComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		departmentComb.setLocation(FormCss.getLocation(departmentLbl, employeeRoleComb));
		panel.add(departmentComb);
		try {
			List<Department> departmentList = this.departmentService.select(null);
			int selectedIdx = 0;
			for(int i = 0; i < departmentList.size(); i ++) {
				Department dept = departmentList.get(i);
				departmentComb.addItem(dept);
				if(null != departmentId && dept.getId() == departmentId) {
					selectedIdx = i;
				}
			}
			departmentComb.setSelectedIndex(selectedIdx);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel statusLbl = new JLabel("״̬��");
		statusLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(statusLbl);
		statusLbl.setLocation(FormCss.getLocation(null, departmentLbl));
		final JAutoCompleteComboBox<String> statusComb = new JAutoCompleteComboBox<String>();
		statusComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		statusComb.setLocation(FormCss.getLocation(statusLbl, departmentComb));
		panel.add(statusComb);
		statusComb.addItem("��ְ");
		statusComb.addItem("��ְ");
		if(1==status) {
			statusComb.setSelectedIndex(1);
		} else {
			statusComb.setSelectedIndex(0);
		}
		
		JLabel idNumberLbl = new JLabel("���֤�ţ�");
		idNumberLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(idNumberLbl);
		idNumberLbl.setLocation(FormCss.getLocation(null, statusLbl));
		final JTextField idNumberIpt = new JTextField();
		idNumberIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(idNumberIpt);
		idNumberIpt.setLocation(FormCss.getLocation(idNumberLbl, statusComb));
		idNumberIpt.setText(idNumber);
		
		JLabel addressLbl = new JLabel("��ͥסַ��");
		addressLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(addressLbl);
		addressLbl.setLocation(FormCss.getLocation(null, idNumberLbl));
		final JTextField addressIpt = new JTextField();
		addressIpt.setSize(300, FormCss.HEIGHT);
		panel.add(addressIpt);
		addressIpt.setLocation(FormCss.getLocation(addressLbl, idNumberIpt));
		addressIpt.setText(address);
		
		JLabel inheritorLbl = new JLabel("�̳��ˣ�");
		inheritorLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(inheritorLbl);
		inheritorLbl.setLocation(FormCss.getLocation(null, addressLbl));
		final JTextField inheritorIpt = new JTextField();
		inheritorIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(inheritorIpt);
		inheritorIpt.setLocation(FormCss.getLocation(inheritorLbl, addressIpt));
		inheritorIpt.setText(inheritor);
		
		JLabel inheritorPhoneLbl = new JLabel("�̳��˵绰��");
		inheritorPhoneLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(inheritorPhoneLbl);
		inheritorPhoneLbl.setLocation(FormCss.getLocation(null, inheritorLbl));
		final JTextField inheritorPhoneIpt = new JTextField();
		inheritorPhoneIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(inheritorPhoneIpt);
		inheritorPhoneIpt.setLocation(FormCss.getLocation(inheritorPhoneLbl, inheritorIpt));
		inheritorPhoneIpt.setText(inheritorPhone);
		
		/*JLabel leaderLbl = new JLabel("�ϼ��쵼��");
		leaderLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(leaderLbl);
		leaderLbl.setLocation(FormCss.getLocation(null, datePicker));
		final JAutoCompleteComboBox<Employee> leaderComb = new JAutoCompleteComboBox<Employee>();
		for(int i = 0; i < employeeList.size(); i ++) {
			leaderComb.addItem(employeeList.get(i));
		}
		
		leaderComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		leaderComb.setLocation(FormCss.getLocation(leaderLbl, datePicker));
		panel.add(leaderComb);*/
		JButton saveBtn = new JButton("����");
		saveBtn.setSize(60, 40);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, inheritorPhoneIpt);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if(StringUtil.isEmpty(nameIpt.getText())) {
					JOptionPane.showMessageDialog(dialog, "����������", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == datePicker.getValue()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ�����ʱ��", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == employeeRoleComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ��ְ��", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == departmentComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ��༶", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
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
				/*Employee leader = null;
				if(null != leaderComb.getSelectedItem()) {
					leader = (Employee) leaderComb.getSelectedItem();
					temp.setLeaderId(leader.getId());
					temp.setLeaderName(leader.getName());
				}*/
				temp.setRegisterDate((Date) datePicker.getValue());
				temp.setStatus(statusComb.getSelectedIndex());
				temp.setAddress(addressIpt.getText());
				Department department = (Department) departmentComb.getSelectedItem();
				temp.setDepartmentId(department.getId());
				temp.setDepartmentName(department.getName());
				EmployeeRole employeeRole = (EmployeeRole) employeeRoleComb.getSelectedItem();
				temp.setEmployeeRoleId(employeeRole.getId());
				temp.setEmployeeRoleName(employeeRole.getName());
				temp.setIdNumber(idNumberIpt.getText());
				temp.setInheritor(inheritorIpt.getText());
				temp.setInheritorPhone(inheritorPhoneIpt.getText());
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = employeeService.add(temp);
					} else {
						if(temp.getStatus() == 1) {
							temp.setFireDate(new Date());
						} else {
							temp.setFireDate(DateUtil.parseFullDate("1000-01-01 00:00:00"));
						}
						success = employeeService.update(temp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(success) {
					dialog.dispose();
					parent.refresh();
					JOptionPane.showMessageDialog(dialog, "����ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
	}
}
