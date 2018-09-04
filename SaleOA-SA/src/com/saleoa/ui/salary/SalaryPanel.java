package com.saleoa.ui.salary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.common.utils.ExceptionUtil;
import com.saleoa.model.Employee;
import com.saleoa.model.Salary;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISalaryService;
import com.saleoa.service.ISalaryServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;
import com.saleoa.ui.plugin.JGridPanel;
import com.saleoa.ui.plugin.PagePanel;


public class SalaryPanel extends JGridPanel<Salary> {
	final ISalaryService salaryService = new ISalaryServiceImpl();
	IEmployeeService employeeService = new IEmployeeServiceImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	private PagePanel<Salary> pagePanel = new PagePanel<Salary>(this, salaryService);
	public SalaryPanel() {
		this.setName(ModuleName.SALARY);
		init();
	}
	
	public void init() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		final SalaryPanel lp = this;
		cols.add("���");
		cols.add("��");
		cols.add("��");
		cols.add("����");
		cols.add("��������");
		cols.add("��������");
		cols.add("��꽱��");
		cols.add("ֱ����");
		cols.add("��");
		cols.add("��곬�");
		cols.add("���ڹ�����");
		cols.add("ȫ�ڽ�");
		cols.add("Ӧ�ۿ�");
		cols.add("�ܴ�꽱");
		cols.add("Ӧ������");
		cols.add("˰��");
		cols.add("���·���");
		cols.add("��˾���");
		cols.add("���չ���");
		cols.add("״̬");
		//cols.add("��ע");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		table.setAlignmentX(CENTER_ALIGNMENT);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		//table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.setLayout(new BorderLayout(3, 3));
		
		//��ѯ��
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(0, 50));
		searchPanel.setBackground(Color.WHITE);
		JLabel employeeLbl = new JLabel("Ա����");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		//employeeLbl.setLocation(FormCss.getLocation(null, null));
		searchPanel.add(employeeLbl);
		final JAutoCompleteComboBox<Employee> employeeSearchComb = new JAutoCompleteComboBox<Employee>();
		employeeSearchComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		searchPanel.add(employeeSearchComb);
		Employee nullItem = new Employee();
		nullItem.setId(0L);
		nullItem.setName("��");
		employeeSearchComb.addItem(nullItem);
		try {
			List<Employee> employeeList = employeeService.select(null);
			for(int i = 0; i < employeeList.size(); i ++) {
				employeeSearchComb.addItem(employeeList.get(i));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel yearSearchLbl = new JLabel("��ݣ�");
		yearSearchLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(yearSearchLbl);
		final JComboBox<Integer> yearSearchComb = new JComboBox<Integer>();
		int currYear = DateUtil.getYear(new Date());
		int startYear = currYear - 5;
		int yearSelectedIdx = 0;
		for(int i = 0; i < 50; i ++) {
			startYear ++;
			yearSearchComb.addItem(startYear);
			if(startYear == currYear) {
				yearSelectedIdx = i;
			}
		}
		yearSearchComb.setSelectedIndex(yearSelectedIdx);
		searchPanel.add(yearSearchComb);
		
		JLabel monthSearchLbl = new JLabel("�·ݣ�");
		monthSearchLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(monthSearchLbl);
		final JComboBox<Integer> monthSearchComb = new JComboBox<Integer>();
		monthSearchComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		searchPanel.add(monthSearchComb);
		int month = 0;
		int currMonth = DateUtil.getMonth(new Date());
		int monthSelectedIdx = 0;
		for(int i = 0; i < 12; i ++) {
			month ++;
			monthSearchComb.addItem(month);
			if(month == currMonth) {
				monthSelectedIdx = i;
			}
		}
		monthSearchComb.setSelectedIndex(monthSelectedIdx);
		
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
						paramMap.put("userId", employee.getId());
					}
				}
				if(null != yearSearchComb.getSelectedItem()) {
					Integer year = (Integer) yearSearchComb.getSelectedItem();
					paramMap.put("year", year);
				}
				if(null != monthSearchComb.getSelectedItem()) {
					Integer month = (Integer) monthSearchComb.getSelectedItem();
					paramMap.put("month", month);
				}
				refresh();
			}
			
		});
		
		JToolBar toolBar = new JToolBar();
		//toolBar.setSize(100, 50);
		toolBar.setPreferredSize(new Dimension(0, 30));
		centerPanel.add(toolBar, BorderLayout.NORTH);
		JButton createSalaryBtn = new JButton("��������");
		createSalaryBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String yearStr = JOptionPane.showInputDialog("���������");
				String monthStr = JOptionPane.showInputDialog("�������·�");
				try{
					int year = Integer.valueOf(yearStr);
					int month = Integer.valueOf(monthStr);
					String[] options = {"ȷ��", "ȡ��"};
					int result = JOptionPane.showOptionDialog(null, "��ȷ������"+year+"-"+(month > 9 ? month : "0"+month)+"�Ĺ�����", "��ܰ����",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
					if(result == 0) {
						boolean success = salaryService.createSalary(year, month);
						if(success) {
							JOptionPane.showMessageDialog( null,"�����ɹ�","��Ϣ", JOptionPane.PLAIN_MESSAGE );
							refresh();
						}
					}
				} catch(Exception ex)  {
		            JOptionPane.showMessageDialog( null,"���ڶԻ���������ȷ������","��Ϣ", JOptionPane.PLAIN_MESSAGE );
		       }
			}
			
		});
		toolBar.add(createSalaryBtn);
		
		JButton addBtn = new JButton("����");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				SalaryDialog dialog = new SalaryDialog();
				dialog.initDialog(null, lp);
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
				List<Salary> list = null;
				try {
					list = salaryService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Salary salary = list.get(0);
				if(salary.getStatus() == 1) {
					JOptionPane.showMessageDialog(lp, "��������Ч���޷��޸�", "��ܰ��ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				SalaryDialog dialog = new SalaryDialog();
				dialog.initDialog(salary, lp);
			}
			
		});
        
		JButton auditBtn = new JButton("������Ч");
		toolBar.add(auditBtn);
		auditBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				int value = JOptionPane.showConfirmDialog(lp, "��ȷ����Ч��ѡ������", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					// TODO Auto-generated method stub
					int[] rowArr = table.getSelectedRows();
					List<Long> list = new ArrayList<Long> ();
					for(int i = 0; i < rowArr.length; i ++) {
						int row = rowArr[i];
						Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
						list.add(id);
					}
					boolean success = false;
					try {
						success = salaryService.auditBatch(list);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String msg = "������Чʧ��";
					if(success) {
						msg = "������Ч�ɹ�";
					}
					JOptionPane.showMessageDialog(lp, msg, "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					if(success) {
						refresh();
					}
					return;
				}
				
			}
			
		});
		
		JButton exportBtn = new JButton("����");
		toolBar.add(exportBtn);
		exportBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				String dateStr = JOptionPane.showInputDialog("��������-�£����磺2018-07");
				String[] dateStrArr = dateStr.split("-");
				int year = 0;
				int month = 0;
				try {
					year = Integer.parseInt(dateStrArr[0]);
					month = Integer.parseInt(dateStrArr[1]);
					if(0 >= year) {
						ExceptionUtil.throwExcep("��������ȷ�����");
					}
					if(0 >= month) {
						ExceptionUtil.throwExcep("��������ȷ���·�");
					}
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(lp, "��������ȷ�ĸ�ʽ�����磺2018-07", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					return ;
				}
				
				int value = JOptionPane.showConfirmDialog(lp, "��ȷ������"+year+"��"+month+"�µĹ�����", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					// TODO Auto-generated method stub
					salaryService.export(year, month);
					return;
				}
				
			}
			
		});
		
		JButton delBtn = new JButton("ɾ��");
		toolBar.add(delBtn);
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(lp, "��ѡ����Ҫɾ��������", "��ܰ��ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				if(null == id) {
					JOptionPane.showMessageDialog(lp, "�����쳣���޷�ɾ��", "��ܰ��ʾ",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Map<String, Object> paramMap = new HashMap<String, Object> ();
				paramMap.put("id", id);
				List<Salary> list = null;
				try {
					list = salaryService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Salary salary = list.get(0);
				if(null == salary) {
					JOptionPane.showMessageDialog(lp, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(1 == salary.getStatus()) {
					JOptionPane.showMessageDialog(lp, "��������Ч���޷�ɾ��", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int value = JOptionPane.showConfirmDialog(lp, "��ȷ��ɾ����ѡ������", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					boolean success = false;
					try {
						success = salaryService.delete(salary);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(success) {
						refresh();
						JOptionPane.showMessageDialog(lp, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
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
        		Salary salary = data.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(salary.getId()));
				newRow.add(String.valueOf(salary.getYear()));
				newRow.add(String.valueOf(salary.getMonth()));
				newRow.add(salary.getUserName());
				newRow.add(salary.getDepartmentName());
				newRow.add(String.valueOf(salary.getMoney()/100.0));
				newRow.add(String.valueOf(salary.getReachGoalBonus()/100.0));
				newRow.add(String.valueOf(salary.getDirectSellMoney()/100.0));
				newRow.add(String.valueOf(salary.getBalanceMoney()/100.0));
				newRow.add(String.valueOf(salary.getOverGoalBonus()/100.0));
				newRow.add(String.valueOf(salary.getOfficeManageBonus()/100.0));
				newRow.add(String.valueOf(salary.getFullDutyBonus()/100.0));
				newRow.add(String.valueOf(salary.getDeductMoney()/100.0));
				newRow.add(String.valueOf(salary.getTotalReachGoalBonus()/100.0));
				newRow.add(String.valueOf(salary.getSupposedMoney()/100.0));
				newRow.add(String.valueOf(salary.getTax()/100.0));
				newRow.add(String.valueOf(salary.getAmercement()/100.0));
				newRow.add(String.valueOf(salary.getCompanyLend()/100.0));
				newRow.add(String.valueOf(salary.getTotalMoney()/100.0));
				newRow.add(salary.getStatus()==0?"�����":"����Ч");
				//newRow.add(salary.getMemo());
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
