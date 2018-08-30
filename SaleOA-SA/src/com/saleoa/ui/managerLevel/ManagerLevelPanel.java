package com.saleoa.ui.managerLevel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.dao.IManagerLevelDao;
import com.saleoa.dao.IManagerLevelDaoImpl;
import com.saleoa.model.ManagerLevel;
import com.saleoa.ui.MainEntry;


public class ManagerLevelPanel extends JPanel {
	IManagerLevelDao managerLevelDao = new IManagerLevelDaoImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	public ManagerLevelPanel() {
		this.setName(ModuleName.MANAGERLEVEL);
		init();
	}
	
	public void init() {
		final ManagerLevelPanel lp = this;
		cols.add("���");
		cols.add("�ȼ�");
		cols.add("��С�۳�");
		cols.add("����۳�");
		cols.add("��������");
		cols.add("����/��");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
		JButton addBtn = new JButton("����");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				ManagerLevelDialog dialog = new ManagerLevelDialog();
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
				List<ManagerLevel> list = managerLevelDao.select(paramMap);
				ManagerLevel managerLevel = list.get(0);
				ManagerLevelDialog dialog = new ManagerLevelDialog();
				dialog.initDialog(managerLevel, lp);
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
				List<ManagerLevel> list = managerLevelDao.select(paramMap);
				ManagerLevel managerLevel = list.get(0);
				if(null == managerLevel) {
					JOptionPane.showMessageDialog(lp, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				int value = JOptionPane.showConfirmDialog(lp, "��ȷ��ɾ����ѡ������", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					//boolean success = managerLevelDao.delete(managerLevel);
					if(true/*success*/) {
						lp.initGrid();
						JOptionPane.showMessageDialog(lp, "ɾ���ɹ�", "��ܰ��ʾ",JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
			}
			
		});
        // ������ʾ���Ĺ������
        JScrollPane scrollPane = new JScrollPane(table);
        // �����������ӵ��߽粼�ֵ��м�
        this.add(scrollPane, BorderLayout.CENTER);
        initGrid();
	}
	
	public void initGrid() {
		try {
			row.clear();
        	List<ManagerLevel> managerLevels = managerLevelDao.select(null);
        	for(int i = 0; i < managerLevels.size(); i ++) {
        		ManagerLevel managerLevel = managerLevels.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(managerLevel.getId()));
				newRow.add(managerLevels.get(i).getName());
				newRow.add(String.valueOf(managerLevel.getMinSale()));
				newRow.add(String.valueOf(managerLevel.getMaxSale()));
				newRow.add(String.valueOf(managerLevel.getBasicSalary()/100.0));
				newRow.add(String.valueOf(managerLevel.getCommission()/100.0));
				row.add(newRow);
        	}
        	model = new DefaultTableModel(row, cols);
			table.setModel(model);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
}
