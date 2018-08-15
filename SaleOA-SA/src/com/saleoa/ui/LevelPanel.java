package com.saleoa.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.saleoa.common.utils.ModuleName;
import com.saleoa.model.Level;


public class LevelPanel extends JPanel {
	public LevelPanel() {
		this.setName(ModuleName.LEVEL);
		init();
	}
	
	public void init() {
		final Vector<Vector<String>> row = new Vector<Vector<String>> ();
		final Vector<String> cols = new Vector<String>();
		cols.add("column1");
		cols.add("column2");
		DefaultTableModel model = new DefaultTableModel();
		final JTable table = new JTable(model);
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("新增");
		addBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				Vector<String> newRow = new Vector<String> ();
				newRow.add("测试");
				newRow.add("10");
				row.add(newRow);
				DefaultTableModel model = new DefaultTableModel(row, cols);
				table.setModel(model);
			}
			
		});
		toolBar.add(addBtn);
		
		String[] columnNames = {"名称","升级所需积分"};// 定义表格列名数组
        // 定义表格数据数组
        String[][] tableValues = {{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}};
        
        
        // 创建指定列名和数据的表格
        
        
        /*Vector vRow1 = new Vector();
        vRow1.add("cell 2 0");
        vRow1.add("cell 2 1");
        vData.add(vRow1);
        model = new DefaultTableModel(vData, vName);
        table.setModel(model);*/
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        // 将滚动面板添加到边界布局的中间
        this.add(scrollPane, BorderLayout.CENTER);
	}
}
