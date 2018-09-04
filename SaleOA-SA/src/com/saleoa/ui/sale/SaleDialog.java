package com.saleoa.ui.sale;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
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
import com.saleoa.ui.plugin.JAutoCompleteComboBox;

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
		
		JLabel employeeLbl = new JLabel("归属人：");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(employeeLbl);
		employeeLbl.setLocation(FormCss.getLocation(null, null));
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
		employeeComb.setLocation(FormCss.getLocation(employeeLbl, null));
		panel.add(employeeComb);
		
		
		JLabel saleNoLbl = new JLabel("售出编号：");
		saleNoLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(saleNoLbl);
		saleNoLbl.setLocation(FormCss.getLocation(null, employeeLbl));
		final JFormattedTextField saleNoIpt = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		saleNoIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(saleNoIpt);
		saleNoIpt.setLocation(FormCss.getLocation(saleNoLbl, employeeComb));
		saleNoIpt.setText(name);
		
		JLabel saleDateLbl = new JLabel("售出时间：");
		saleDateLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		panel.add(saleDateLbl);
		saleDateLbl.setLocation(FormCss.getLocation(null, saleNoLbl));
		panel.add(saleDateLbl);
		
		String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
		// 当前时间
        Date date = new Date();
        // 字体
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker datePicker = new DatePicker(date, DefaultFormat, font, dimension);
		datePicker.setLocation(FormCss.getLocation(saleDateLbl, saleNoIpt));
		datePicker.setLocale(Locale.CHINA);
        // 设置时钟面板可见
		datePicker.setTimePanleVisible(true);
		panel.add(datePicker);
		
		JLabel lastSaleLbl = new JLabel("推荐人：");
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
		
		employeeComb.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent event) {
				// TODO Auto-generated method stub
				if(event.getStateChange() == ItemEvent.SELECTED) {
					Employee employee = (Employee) event.getItem();
					long maxSaleNo = saleService.getMaxNoByEmployeeId(employee.getId());
					if(maxSaleNo > 0) {
						maxSaleNo ++;
					}
					saleNoIpt.setText(String.valueOf(maxSaleNo));
					
					Map<String, Object> paramMap = new HashMap<String, Object> ();
					paramMap.put("employeeId", employee.getId());
					paramMap.put("orderby", " ORDER BY id DESC");
					List<Sale> saleList = null;
					try {
						saleList = saleService.select(paramMap);
						if(null == saleList || saleList.isEmpty()) {
							saleList = saleService.select(null);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						saleList = new ArrayList<Sale>();
					}
					lastSaleComb.removeAllItems();
					for(int i = 0; i < saleList.size(); i ++) {
						lastSaleComb.addItem(saleList.get(i));
					}
				}
			}
			
		});
		
		lastSaleComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		lastSaleComb.setLocation(FormCss.getLocation(lastSaleLbl, datePicker));
		panel.add(lastSaleComb);
		JButton saveBtn = new JButton("保存");
		saveBtn.setSize(60, 40);
		panel.add(saveBtn);
		Point p = FormCss.getLocation(null, lastSaleComb);
		p.x = (dialogWidth-saveBtn.getSize().width)/2;
		System.out.println("saveBtn position: x="+p.x+", y="+p.y);
		saveBtn.setLocation(p);
		saveBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if(null == employeeComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "请选择归属人", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				Employee employee = (Employee)employeeComb.getSelectedItem();
				if(0L == employee.getId()) {
					JOptionPane.showMessageDialog(dialog, "请选择归属人", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(StringUtil.isEmpty(saleNoIpt.getText())) {
					JOptionPane.showMessageDialog(dialog, "请输入编号", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == datePicker.getValue()) {
					JOptionPane.showMessageDialog(dialog, "请选择售出时间", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(null == lastSaleComb.getSelectedItem()) {
					JOptionPane.showMessageDialog(dialog, "请选择推荐人", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				Sale lastSale = (Sale) lastSaleComb.getSelectedItem();
				/*if(lastSale.getId().longValue() <= 0l) {
					JOptionPane.showMessageDialog(dialog, "请选择推荐人", "温馨提示",JOptionPane.WARNING_MESSAGE);
					return;
				}*/
				Sale temp = new Sale();
				if(null != sale) {
					BeanUtil.copyBean(sale, temp);
				} else {
					temp.setCreateDate(new Date());
				}
				int saleNo = Integer.parseInt(saleNoIpt.getText());
				String name = employee.getName()+saleNo;
				String nameEn = PinyinUtil.getStringPinYin(name);
				temp.setName(name);
				temp.setNameEn(nameEn);
				temp.setSaleNo(saleNo);
				temp.setEmployeeId(employee.getId());
				temp.setEmployeeName(employee.getName());
				
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
					parent.refresh();
					JOptionPane.showMessageDialog(dialog, "保存成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
		});
	}
}
