package com.saleoa.ui.saleSalary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.model.Employee;
import com.saleoa.model.SaleSalary;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISaleSalaryService;
import com.saleoa.service.ISaleSalaryServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;
import com.saleoa.ui.plugin.JGridPanel;
import com.saleoa.ui.plugin.PagePanel;

public class SaleSalaryPanel extends JGridPanel<SaleSalary> {
	ISaleSalaryService saleService = new ISaleSalaryServiceImpl();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	private long page = 10;
	private int limit = 15;
	private long totalCount = 0;
	long currPage=1;
	long totalPage=20;
	private PagePanel<SaleSalary> pagePanel = new PagePanel<SaleSalary>(this, saleService);
	public SaleSalaryPanel() {
		this.setName(ModuleName.SALE);
		init();
	}
	
	public void init() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		final SaleSalaryPanel ep = this;
		cols.add("������");
		cols.add("����");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		table.setBackground(Color.WHITE);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout(3, 3));
		
		//��ѯ��
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(0, 50));
		searchPanel.setBackground(Color.WHITE);
		JLabel employeeLbl = new JLabel("�����ˣ�");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		//employeeLbl.setLocation(FormCss.getLocation(null, null));
		searchPanel.add(employeeLbl);
		final JAutoCompleteComboBox<Employee> employeeSearchComb = new JAutoCompleteComboBox<Employee>();
		employeeSearchComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		searchPanel.add(employeeSearchComb);
		try {
			List<Employee> employeeList = employeeService.select(null);
			for(int i = 0; i < employeeList.size(); i ++) {
				employeeSearchComb.addItem(employeeList.get(i));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel saleDateStartLbl = new JLabel("��ʼʱ�䣺");
		saleDateStartLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(saleDateStartLbl);
		Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker saleDateStartPicker = new DatePicker(DateUtil.getStartDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(saleDateStartPicker);
		saleDateStartPicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		saleDateStartPicker.setTimePanleVisible(true);
		
		JLabel saleDateEndLbl = new JLabel("����ʱ�䣺");
		saleDateEndLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(saleDateEndLbl);
		final DatePicker saleDateEndPicker = new DatePicker(DateUtil.getEndDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(saleDateEndPicker);
		saleDateEndPicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		saleDateEndPicker.setTimePanleVisible(true);
		
		JButton searchBtn = new JButton("��ѯ");
		searchBtn.setSize(60, 50);
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				paramMap.clear();
				if(null != employeeSearchComb.getSelectedItem()) {
					Employee employee = (Employee) employeeSearchComb.getSelectedItem();
					if(employee.getId() > 0) {
						paramMap.put("employeeId", employee.getId());
					}
				}
				if(null != saleDateStartPicker.getValue()) {
					Date saleDateStart = (Date) saleDateStartPicker.getValue();
					paramMap.put("createDate>=", "'"+DateUtil.formatFullDate(saleDateStart)+"'");
				}
				if(null != saleDateEndPicker.getValue()) {
					Date saleDateEnd = (Date) saleDateEndPicker.getValue();
					paramMap.put("createDate<=", "'"+DateUtil.formatFullDate(saleDateEnd)+"'");
				}
				refresh();
			}
			
		});
        // ������ʾ���Ĺ������
        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);
        // �����������ӵ��߽粼�ֵ��м�
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(pagePanel, BorderLayout.SOUTH);
        refresh();
	}
	
	public void initGrid() {
		try {
			row.clear();
        	for(int i = 0; i < data.size(); i ++) {
        		SaleSalary sale = data.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(sale.getEmployeeName());
				newRow.add(String.valueOf(sale.getSalary()/100.0));
				row.add(newRow);
        	}
        	model = new DefaultTableModel(row, cols);
			table.setModel(model);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	public void refresh() {
		pagePanel.loadData(null);
		initGrid();
	}
	
	public void refresh(long currPage) {
		initGrid();
	}
}
