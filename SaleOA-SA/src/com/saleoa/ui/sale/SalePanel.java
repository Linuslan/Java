package com.saleoa.ui.sale;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.DateUtil;
import com.saleoa.model.Sale;
import com.saleoa.service.ISaleService;
import com.saleoa.service.ISaleServiceImpl;
import com.saleoa.ui.MainEntry;

public class SalePanel extends JPanel {
	ISaleService saleService = new ISaleServiceImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	public SalePanel() {
		this.setName(ModuleName.SALE);
		init();
	}
	
	public void init() {
		final SalePanel ep = this;
		cols.add("编号");
		cols.add("产品");
		cols.add("归属人");
		cols.add("上套产品");
		cols.add("售出时间");
		cols.add("积分");
		cols.add("等级");
		cols.add("奖金");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("新增");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				SaleDialog dialog = new SaleDialog();
				dialog.initDialog(null, ep);
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
					//boolean success = levelDao.delete(level);
					if(true/*success*/) {
						ep.initGrid();
						JOptionPane.showMessageDialog(ep, "删除成功", "温馨提示",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
			}
			
		});
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        // 将滚动面板添加到边界布局的中间
        this.add(scrollPane, BorderLayout.CENTER);
        initGrid();
	}
	
	public void initGrid() {
		try {
			row.clear();
        	List<Sale> list = saleService.select(null);
        	for(int i = 0; i < list.size(); i ++) {
        		Sale sale = list.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(sale.getId()));
				newRow.add(sale.getName());
				newRow.add(sale.getEmployeeName());
				newRow.add(sale.getLastSaleName());
				newRow.add(DateUtil.formatFullDate(sale.getSaleDate()));
				newRow.add(String.valueOf(sale.getRewardPoints()));
				newRow.add(String.valueOf(sale.getLevelName()));
				newRow.add(String.valueOf(sale.getSalary()/100.0));
				row.add(newRow);
        	}
        	model = new DefaultTableModel(row, cols);
			table.setModel(model);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
}
