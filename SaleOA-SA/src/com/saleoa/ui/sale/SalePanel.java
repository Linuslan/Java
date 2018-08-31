package com.saleoa.ui.sale;

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
import com.saleoa.common.utils.StringUtil;
import com.saleoa.model.Employee;
import com.saleoa.model.Sale;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISaleService;
import com.saleoa.service.ISaleServiceImpl;
import com.saleoa.ui.MainEntry;
import com.saleoa.ui.plugin.JAutoCompleteComboBox;
import com.saleoa.ui.plugin.JGridPanel;
import com.saleoa.ui.plugin.PagePanel;

public class SalePanel extends JGridPanel<Sale> {
	ISaleService saleService = new ISaleServiceImpl();
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
	private PagePanel<Sale> pagePanel = new PagePanel<Sale>(this, saleService);
	public SalePanel() {
		this.setName(ModuleName.SALE);
		init();
	}
	
	public void init() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		final SalePanel ep = this;
		cols.add("编号");
		cols.add("产品");
		cols.add("归属人");
		cols.add("推荐人");
		cols.add("售出时间");
		cols.add("积分");
		cols.add("等级");
		cols.add("奖金");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
		table.setDefaultRenderer(Object.class, tcr);
		table.setBackground(Color.WHITE);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout(3, 3));
		
		//查询框
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
		this.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(0, 50));
		searchPanel.setBackground(Color.WHITE);
		JLabel employeeLbl = new JLabel("归属人：");
		employeeLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		//employeeLbl.setLocation(FormCss.getLocation(null, null));
		searchPanel.add(employeeLbl);
		final JAutoCompleteComboBox<Employee> employeeSearchComb = new JAutoCompleteComboBox<Employee>();
		employeeSearchComb.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		Employee nullItem = new Employee();
		nullItem.setId(0L);
		nullItem.setName("无");
		employeeSearchComb.addItem(nullItem);
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
		JLabel saleDateStartLbl = new JLabel("售出时间始：");
		saleDateStartLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(saleDateStartLbl);
		Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		final DatePicker saleDateStartPicker = new DatePicker(DateUtil.getStartDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(saleDateStartPicker);
		saleDateStartPicker.setLocale(Locale.CHINA);
        // 设置时钟面板可见
		saleDateStartPicker.setTimePanleVisible(true);
		
		JLabel saleDateEndLbl = new JLabel("售出时间止：");
		saleDateEndLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		searchPanel.add(saleDateEndLbl);
		final DatePicker saleDateEndPicker = new DatePicker(DateUtil.getEndDateTime(new Date()), "yyyy-MM-dd HH:mm:ss", font, dimension);
		searchPanel.add(saleDateEndPicker);
		saleDateEndPicker.setLocale(Locale.CHINA);
        // 设置时钟面板可见
		saleDateEndPicker.setTimePanleVisible(true);
		
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
						paramMap.put("employeeId", employee.getId());
					}
				}
				if(null != saleDateStartPicker.getValue()) {
					Date saleDateStart = (Date) saleDateStartPicker.getValue();
					paramMap.put("saleDate>=", "'"+DateUtil.formatFullDate(saleDateStart)+"'");
				}
				if(null != saleDateEndPicker.getValue()) {
					Date saleDateEnd = (Date) saleDateEndPicker.getValue();
					paramMap.put("saleDate<=", "'"+DateUtil.formatFullDate(saleDateEnd)+"'");
				}
				refresh();
			}
			
		});
		
		
		JToolBar toolBar = new JToolBar();
		//toolBar.setSize(100, 40);
		toolBar.setPreferredSize(new Dimension(0, 30));
		centerPanel.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("新增");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				SaleDialog dialog = new SaleDialog();
				dialog.initDialog(null, ep);
			}
			
		});
		toolBar.add(addBtn);
		
		/*JButton editBtn = new JButton("修改");
		toolBar.add(editBtn);
		editBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				System.out.println("获取到的id为"+id);
				Map<String, Object> paramMap = new HashMap<String, Object> ();
				paramMap.put("id", id);
				List<Sale> list = null;
				try {
					list = saleService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					list = new ArrayList<Sale>();
				}
				Sale sale = list.get(0);
				SaleDialog dialog = new SaleDialog();
				dialog.initDialog(sale, ep);
			}
			
		});
        
		JButton delBtn = new JButton("删除");
		toolBar.add(delBtn);
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(ep, "请选择需要删除的数据", "温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Long id = BeanUtil.parseLong(table.getValueAt(row, 0));
				if(null == id) {
					JOptionPane.showMessageDialog(ep, "数据异常，无法删除", "温馨提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				Map<String, Object> paramMap = new HashMap<String, Object> ();
				paramMap.put("id", id);
				List<Sale> list = null;
				try {
					list = saleService.select(paramMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					list = new ArrayList<Sale>();
				}
				Sale sale = list.get(0);
				if(null == sale) {
					JOptionPane.showMessageDialog(ep, "删除成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int value = JOptionPane.showConfirmDialog(ep, "您确定删除所选数据吗？", "温馨提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					boolean success = levelDao.delete(level);
					if(success) {
						ep.initGrid();
						JOptionPane.showMessageDialog(ep, "删除成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
			}
			
		});*/
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setBackground(Color.WHITE);
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
        		Sale sale = data.get(i);
        		if(null != sale.getId() && 0 == sale.getId()) {
        			continue;
        		}
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(null == sale.getId() ? "" : String.valueOf(sale.getId()));
				newRow.add(sale.getName());
				newRow.add(StringUtil.isEmpty(sale.getEmployeeName()) ? "" : sale.getEmployeeName());
				newRow.add(StringUtil.isEmpty(sale.getLastSaleName()) ? "" : sale.getLastSaleName());
				newRow.add(null == sale.getSaleDate() ? "" : DateUtil.formatFullDate(sale.getSaleDate()));
				newRow.add(null == sale.getRewardPoints() ? "" : String.valueOf(sale.getRewardPoints()));
				newRow.add(StringUtil.isEmpty(sale.getLevelName()) ? "" : sale.getLevelName());
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
