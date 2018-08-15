package com.saleoa.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.saleoa.common.utils.ModuleName;


public class LevelPanel extends JPanel {
	public LevelPanel() {
		this.setName(ModuleName.LEVEL);
		init();
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("新增");
		toolBar.add(addBtn);
		
		String[] columnNames = {"名称","升级所需积分"};// 定义表格列名数组
        // 定义表格数据数组
        String[][] tableValues = {{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}};
        TableModel model = new DefaultTableModel();
        
        // 创建指定列名和数据的表格
        JTable table = new JTable();
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        // 将滚动面板添加到边界布局的中间
        this.add(scrollPane, BorderLayout.CENTER);
	}
}
