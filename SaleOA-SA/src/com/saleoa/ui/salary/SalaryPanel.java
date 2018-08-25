package com.saleoa.ui.salary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.eltima.components.ui.DatePicker;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
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
		cols.add("编号");
		cols.add("年");
		cols.add("月");
		cols.add("姓名");
		cols.add("应得工资");
		cols.add("应扣款");
		cols.add("最终工资");
		cols.add("状态");
		cols.add("备注");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		table.setAlignmentX(CENTER_ALIGNMENT);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
		table.setDefaultRenderer(Object.class, tcr);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout(3, 3));
		
		//查询框
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(0, 50));
		searchPanel.setBackground(Color.WHITE);
		JLabel employeeLbl = new JLabel("员工：");
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
		JLabel yearSearchLbl = new JLabel("年份：");
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
		
		JLabel monthSearchLbl = new JLabel("月份：");
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
		
		JButton searchBtn = new JButton("查询");
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
		JButton createSalaryBtn = new JButton("创建工资");
		createSalaryBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String yearStr = JOptionPane.showInputDialog("请输入年份");
				String monthStr = JOptionPane.showInputDialog("请输入月份");
				try{
					int year = Integer.valueOf(yearStr);
					int month = Integer.valueOf(monthStr);
					String[] options = {"确定", "取消"};
					int result = JOptionPane.showOptionDialog(null, "您确定创建"+year+"-"+(month > 9 ? month : "0"+month)+"的工资吗？", "温馨提醒",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
					if(result == 0) {
						boolean success = salaryService.createSalary(year, month);
						if(success) {
							JOptionPane.showMessageDialog( null,"创建成功","消息", JOptionPane.PLAIN_MESSAGE );
							initGrid();
						}
					}
				} catch(Exception ex)  {
		            JOptionPane.showMessageDialog( null,"请在对话框输入正确的整数","消息", JOptionPane.PLAIN_MESSAGE );
		       }
			}
			
		});
		toolBar.add(createSalaryBtn);
		
		JButton addBtn = new JButton("新增");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				SalaryDialog dialog = new SalaryDialog();
				dialog.initDialog(null, lp);
			}
			
		});
		toolBar.add(addBtn);
		
		JButton editBtn = new JButton("修改");
		toolBar.add(editBtn);
		editBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				System.out.println("获取到的id为"+id);
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
				SalaryDialog dialog = new SalaryDialog();
				dialog.initDialog(salary, lp);
			}
			
		});
        
		JButton delBtn = new JButton("删除");
		toolBar.add(delBtn);
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(lp, "请选择需要删除的数据", "温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				if(null == id) {
					JOptionPane.showMessageDialog(lp, "数据异常，无法删除", "温馨提示",JOptionPane.ERROR_MESSAGE);
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
					JOptionPane.showMessageDialog(lp, "删除成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int value = JOptionPane.showConfirmDialog(lp, "您确定删除所选数据吗？", "温馨提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					//boolean success = salaryDao.delete(salary);
					if(true/*success*/) {
						lp.initGrid();
						JOptionPane.showMessageDialog(lp, "删除成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
			}
			
		});
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        // 将滚动面板添加到边界布局的中间
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.setBackground(Color.WHITE);
        // 将滚动面板添加到边界布局的中间
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
				newRow.add(String.valueOf(salary.getMoney()/100.0));
				newRow.add(String.valueOf(salary.getDeductMoney()/100.0));
				newRow.add(String.valueOf(salary.getTotalMoney()/100.0));
				newRow.add(salary.getStatus()==0?"待审核":"已生效");
				newRow.add(salary.getMemo());
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
