package com.saleoa.ui.salary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.saleoa.common.constant.ModuleName;
import com.saleoa.common.constant.TableCss;
import com.saleoa.common.utils.BeanUtil;
import com.saleoa.model.Salary;
import com.saleoa.service.ISalaryService;
import com.saleoa.service.ISalaryServiceImpl;
import com.saleoa.ui.MainEntry;


public class SalaryPanel extends JPanel {
	final ISalaryService salaryService = new ISalaryServiceImpl();
	private static Dimension screenSize = MainEntry.getScreanSize();
	final Vector<Vector<String>> row = new Vector<Vector<String>> ();
	final Vector<String> cols = new Vector<String>();
	DefaultTableModel model = null;
	JTable table = null;
	public SalaryPanel() {
		this.setName(ModuleName.SALARY);
		init();
	}
	
	public void init() {
		final SalaryPanel lp = this;
		cols.add("���");
		cols.add("��");
		cols.add("��");
		cols.add("����");
		cols.add("Ӧ�ù���");
		cols.add("Ӧ�ۿ�");
		cols.add("���չ���");
		cols.add("״̬");
		cols.add("��ע");
		model = new DefaultTableModel(row, cols);
		table = new JTable(model);
		table.setAlignmentX(CENTER_ALIGNMENT);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		table.setRowHeight(TableCss.ROW_HEIGHT);
		table.getTableHeader().setSize(0, TableCss.ROW_HEIGHT);
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.setSize(100, 50);
		this.add(toolBar, BorderLayout.NORTH);
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
							initGrid();
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
				SalaryDialog dialog = new SalaryDialog();
				dialog.initDialog(salary, lp);
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
				int value = JOptionPane.showConfirmDialog(lp, "��ȷ��ɾ����ѡ������", "��ܰ��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(value == JOptionPane.YES_OPTION) {
					//boolean success = salaryDao.delete(salary);
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
        	List<Salary> salarys = salaryService.select(null);
        	for(int i = 0; i < salarys.size(); i ++) {
        		Salary salary = salarys.get(i);
        		Vector<String> newRow = new Vector<String> ();
				newRow.add(String.valueOf(salary.getId()));
				newRow.add(String.valueOf(salary.getYear()));
				newRow.add(String.valueOf(salary.getMonth()));
				newRow.add(salary.getUserName());
				newRow.add(String.valueOf(salary.getMoney()/100.0));
				newRow.add(String.valueOf(salary.getDeductMoney()/100.0));
				newRow.add(String.valueOf(salary.getTotalMoney()/100.0));
				newRow.add(salary.getStatus()==0?"�����":"����Ч");
				newRow.add(salary.getMemo());
				row.add(newRow);
        	}
        	model = new DefaultTableModel(row, cols);
			table.setModel(model);
        } catch(Exception ex) {
        	ex.printStackTrace();
        }
	}
}
