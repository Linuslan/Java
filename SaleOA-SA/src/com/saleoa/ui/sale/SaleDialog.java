package com.saleoa.ui.sale;

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
import com.saleoa.model.Sale;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISaleService;
import com.saleoa.service.ISaleServiceImpl;
import com.saleoa.ui.MainEntry;

public class SaleDialog {
	private static Dimension screenSize = MainEntry.getScreanSize();
	ISaleService saleService = new ISaleServiceImpl();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private Long id;
	private String name = "";
	private Long point=0L;

	public void initDialog(final Sale sale, final SalePanel parent) {
		if(null != sale) {
			id = sale.getId();
			name = sale.getName();
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
		JLabel employeeLbl = new JLabel("�����ˣ�");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(employeeLbl);
		employeeLbl.setLocation(FormCss.getLocation(null, nameLbl));
		//final JComboBox<Sale> employeeComb = new JComboBox<Sale>();
		final JAutoCompleteComboBox<Employee> employeeComb = new JAutoCompleteComboBox<Employee>();
		List<Employee> employeeList = null;
		try {
			employeeList = this.employeeService.select(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			employeeList = new ArrayList<Employee>();
		}
		for(int i = 0; i < employeeList.size(); i ++) {
			employeeComb.addItem(employeeList.get(i));
		}
		employeeComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		employeeComb.setLocation(FormCss.getLocation(employeeLbl, nameIpt));
		panel.add(employeeComb);
		
		JLabel saleDateLbl = new JLabel("�۳�ʱ�䣺");
		saleDateLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(saleDateLbl);
		saleDateLbl.setLocation(FormCss.getLocation(null, employeeLbl));
		panel.add(saleDateLbl);
		
		String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
		// ��ǰʱ��
        Date date = new Date();
        // ����
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker datePicker = new DatePicker(date, DefaultFormat, font, dimension);
		datePicker.setLocation(FormCss.getLocation(saleDateLbl, employeeComb));
		datePicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		datePicker.setTimePanleVisible(true);
		panel.add(datePicker);
		
		JLabel lastSaleLbl = new JLabel("��һ�ף�");
		lastSaleLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(lastSaleLbl);
		lastSaleLbl.setLocation(FormCss.getLocation(null, datePicker));
		List<Sale> saleList = null;
		try {
			saleList = this.saleService.select(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saleList = new ArrayList<Sale>();
		}
		final JAutoCompleteComboBox<Sale> lastSaleComb = new JAutoCompleteComboBox<Sale>();
		for(int i = 0; i < saleList.size(); i ++) {
			lastSaleComb.addItem(saleList.get(i));
		}
		
		lastSaleComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		lastSaleComb.setLocation(FormCss.getLocation(lastSaleLbl, datePicker));
		panel.add(lastSaleComb);
		JButton saveBtn = new JButton("����");
		saveBtn.setSize(60, 40);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, lastSaleComb);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if(null == employeeComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ�������", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(StringUtil.isEmpty(nameIpt.getText())) {
					JOptionPane.showMessageDialog(dialog, "����������", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == datePicker.getValue()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ���۳�ʱ��", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == lastSaleComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "��ѡ����һ���۳�", "��ܰ��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}
				Sale temp = new Sale();
				if(null != sale) {
					BeanUtil.copyBean(sale, temp);
				} else {
					temp.setCreateDate(new Date());
				}
				String name = nameIpt.getText();
				String nameEn = PinyinUtil.getStringPinYin(name);
				temp.setName(name);
				temp.setNameEn(nameEn);
				Sale employee = (Sale)employeeComb.getSelectedItem();
				temp.setEmployeeId(employee.getId());
				temp.setEmployeeName(employee.getName());
				Sale lastSale = (Sale) lastSaleComb.getSelectedItem();
				temp.setLastSaleId(lastSale.getId());
				temp.setLastSaleName(lastSale.getName());
				temp.setSaleDate((Date) datePicker.getValue());
				
				boolean success = false;
				try {
					if(null == temp.getId()) {
						success = saleService.add(temp);
					} else {
						success = saleService.update(temp);
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