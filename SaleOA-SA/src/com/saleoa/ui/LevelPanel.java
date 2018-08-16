package com.saleoa.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import com.saleoa.common.utils.JdbcHelper;
import com.saleoa.common.utils.ModuleName;
import com.saleoa.model.Level;


public class LevelPanel extends JPanel {
	private static Dimension screenSize = MainEntry.getScreanSize();
	public LevelPanel() {
		this.setName(ModuleName.LEVEL);
		init();
	}
	
	public void init() {
		final Vector<Vector<String>> row = new Vector<Vector<String>> ();
		final Vector<String> cols = new Vector<String>();
		cols.add("编号");
		cols.add("等级");
		cols.add("积分");
		DefaultTableModel model = new DefaultTableModel(row, cols);
		final JTable table = new JTable(model);
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("新增");
		addBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JDialog dialog = new JDialog();
				int dialogWidth = 300;
				int dialogHeight = 300;
				dialog.setSize(dialogWidth, dialogHeight);
				dialog.setLocation((screenSize.width-dialogWidth)/2, (screenSize.height-dialogHeight)/2);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
			
		});
		toolBar.add(addBtn);
        
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        // 将滚动面板添加到边界布局的中间
        this.add(scrollPane, BorderLayout.CENTER);
        try {
        	List<Level> levels = (List<Level>) JdbcHelper.select("SELECT * FROM tbl_oa_level", Level.class);
        	for(int i = 0; i < levels.size(); i ++) {
        		Level level = levels.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(level.getId()));
				newRow.add(levels.get(i).getName());
				newRow.add(String.valueOf(level.getRewardPoints()));
				row.add(newRow);
        	}
        	model = new DefaultTableModel(row, cols);
			table.setModel(model);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
}
