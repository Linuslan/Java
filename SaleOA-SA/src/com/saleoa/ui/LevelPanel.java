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
		JButton addBtn = new JButton("����");
		toolBar.add(addBtn);
		
		String[] columnNames = {"����","�����������"};// ��������������
        // ��������������
        String[][] tableValues = {{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}};
        TableModel model = new DefaultTableModel();
        
        // ����ָ�����������ݵı��
        JTable table = new JTable();
        // ������ʾ���Ĺ������
        JScrollPane scrollPane = new JScrollPane(table);
        // �����������ӵ��߽粼�ֵ��м�
        this.add(scrollPane, BorderLayout.CENTER);
	}
}
