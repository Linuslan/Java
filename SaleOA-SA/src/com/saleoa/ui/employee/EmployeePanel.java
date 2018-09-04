package com.saleoa.ui.employee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Employee;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JGridPanel;
import com.saleoa.ui.plugin.PagePanel;

public class EmployeePanel extends JGridPanel<Employee> {
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	private PagePanel<Employee> pagePanel = new PagePanel<Employee>(this, employeeService);
	public EmployeePanel() {
		this.setName(ModuleName.EMPLOYEE);
		init();
	}
	
	public void init() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		final EmployeePanel ep = this;
		cols.add("���");
		cols.add("����");
		cols.add("��ְʱ��");
		cols.add("��ְ״̬");
		cols.add("ְ��");
		cols.add("�༶");
		/*cols.add("�ȼ�");
		cols.add("��ǰ����");
		cols.add("���¹���");
		cols.add("��ְʱ��");
		cols.add("������");
		cols.add("�ϼ�");*/
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout(3, 3));
		
		//��ѯ��
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(0, 50));
		searchPanel.setBackground(Color.WHITE);
		
		JLabel nameSearchLbl = new JLabel("������");
		nameSearchLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		//employeeLbl.setLocation(FormCss.getLocation(null, null));
		searchPanel.add(nameSearchLbl);
		final JTextField nameSearchIpt = new JTextField();
		nameSearchIpt.setPreferredSize(new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT));
		searchPanel.add(nameSearchIpt);
		
		JLabel employeeLbl = new JLabel("״̬��");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		//employeeLbl.setLocation(FormCss.getLocation(null, null));
		searchPanel.add(employeeLbl);
		final JComboBox<String> statusSearchComb = new JComboBox<String>();
		statusSearchComb.addItem("��ְ");
		statusSearchComb.addItem("��ְ");
		statusSearchComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		searchPanel.add(statusSearchComb);
		
		JLabel registerDateStartLbl = new JLabel("��ְʱ��ʼ��");
		registerDateStartLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(registerDateStartLbl);
		Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker registerDateStartPicker = new DatePicker(DateUtil.getStartDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(registerDateStartPicker);
		registerDateStartPicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		registerDateStartPicker.setTimePanleVisible(true);
		
		JLabel registerDateEndLbl = new JLabel("��ְʱ��ֹ��");
		registerDateEndLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(registerDateEndLbl);
		final DatePicker registerDateEndPicker = new DatePicker(DateUtil.getEndDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(registerDateEndPicker);
		registerDateEndPicker.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
		registerDateEndPicker.setTimePanleVisible(true);
		
		JButton searchBtn = new JButton("��ѯ");
		searchBtn.setSize(60, 50);
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				paramMap.clear();
				String name = nameSearchIpt.getText();
				if(!StringUtil.isEmpty(name)) {
					paramMap.put("name LIKE ", "%"+name+"%");
				}
				if(null != statusSearchComb.getSelectedItem()) {
					int status = statusSearchComb.getSelectedIndex();
					paramMap.put("status", status);
				}
				if(null != registerDateStartPicker.getValue()) {
					Date saleDateStart = (Date) registerDateStartPicker.getValue();
					paramMap.put("registerDate>=", DateUtil.formatFullDate(saleDateStart));
				}
				if(null != registerDateEndPicker.getValue()) {
					Date saleDateEnd = (Date) registerDateEndPicker.getValue();
					paramMap.put("registerDate<=", DateUtil.formatFullDate(saleDateEnd));
				}
				refresh();
			}
			
		});
		
		JToolBar toolBar = new JToolBar();
		toolBar.setPreferredSize(new Dimension(0, 30));
		centerPanel.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("����");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				EmployeeDialog dialog = new EmployeeDialog();
				dialog.initDialog(null, ep);
			}
			
		});
		toolBar.add(addBtn);
		
		JButton editBtn = new JButton("�޸�");
		toolBar.add(editBtn);
		editBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				System.out.println("��ȡ����idΪ"+id);
				Map<String, Object> paramMap = new HashMap<String, Object> ();
				paramMap.put("id", id);
				List<Employee> list = null;
				try {
					list = employeeService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					list = new ArrayList<Employee>();
				}
				Employee employee = list.get(0);
				EmployeeDialog dialog = new EmployeeDialog();
				dialog.initDialog(employee, ep);
			}
			
		});
        
		JButton delBtn = new JButton("ɾ��");
		toolBar.add(delBtn);
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(ep, "��ѡ����Ҫɾ��������", "��ܰ��ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				if(null == id) {
					JOptionPane.showMessageDialog(ep, "�����쳣���޷�ɾ��", "��ܰ��ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Map<String, Object> paramMap = new HashMap<String, Object> ();
				paramMap.put("id", id);
				List<Employee> list = null;
				try {
					list = employeeService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					list = new ArrayList<Employee>();
				}
				Employee employee = list.get(0);
				if(null == employee) {
					JOptionPane.showMessageDialog(ep, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int value = JOptionPane.showConfirmDialog(ep, "��ȷ��ɾ����ѡ������", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					boolean success = false;
					try {
						success = employeeService.delete(employee);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(success) {
						ep.refresh();
						JOptionPane.showMessageDialog(ep, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
			}
			
		});
        // ������ʾ���Ĺ������
        JScrollPane scrollPane = new JScrollPane(table);
     // �����������ӵ��߽粼�ֵ��м�
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
        		Employee employee = data.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(employee.getId()));
				newRow.add(employee.getName());
				newRow.add(DateUtil.formatFullDate(employee.getRegisterDate()));
				newRow.add(employee.getStatus() == 0 ? "��ְ":"��ְ");
				newRow.add(employee.getEmployeeRoleName());
				newRow.add(employee.getDepartmentName());
				/*newRow.add(employee.getLevelName());
				newRow.add(String.valueOf(employee.getRewardPoints()));
				newRow.add(String.valueOf(employee.getSalary()/100.0));
				newRow.add(DateUtil.formatFullDate(employee.getRegisterDate()));
				newRow.add(employee.getIntroducerName());
				newRow.add(StringUtil.isEmpty(employee.getLeaderName())?"":employee.getLeaderName());*/
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
}
