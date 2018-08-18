package com.saleoa.common.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.saleoa.common.utils.BeanUtil;
import com.saleoa.common.utils.PinyinUtil;
import com.saleoa.model.Employee;

public class JAutoCompleteComboBox<E> extends JComboBox<E> {
	private AutoCompleter<E> completer;

	public JAutoCompleteComboBox() {
		super();
		addCompleter();
	}

	public JAutoCompleteComboBox(ComboBoxModel<E> cm) {
		super(cm);
		addCompleter();
	}

	/*public JAutoCompleteComboBox(Object[] items) {
		super(items);
		addCompleter();
	}*/

	public JAutoCompleteComboBox(List<E> v) {
		super((Vector<E>) v);
		addCompleter();
	}

	private void addCompleter() {
		setEditable(true);
		completer = new AutoCompleter<E>(this);
	}

	public void autoComplete(String str) {
		this.completer.autoComplete(str, str.length());
	}

	public String getText() {
		return ((JTextField) getEditor().getEditorComponent()).getText();
	}

	public void setText(String text) {
		((JTextField) getEditor().getEditorComponent()).setText(text);
	}

	public boolean containsItem(String itemString) {
		for (int i = 0; i < this.getModel().getSize(); i++) {
			String _item = " " + this.getModel().getElementAt(i).toString();
			if (_item.equals(itemString)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Object[] items = new Object[] { "abc ", "aab ", "aba ", "hpp ", "pp ",
				"hlp ", "abdfefg" };
		/*DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		JComboBox<String> cmb = new JAutoCompleteComboBox<String>(model);
		model.addElement("张三");
		model.addElement("李四");
		model.addElement("王五");
		model.addElement("赵六");
		model.addElement("孙七");
		model.addElement("周八");
		model.addElement("吴九");
		model.addElement("郑十");
		model.addElement("abc ");
		model.addElement("aab ");
		model.addElement("aba ");
		model.addElement("hpp ");
		model.addElement("pp ");
		model.addElement("hlp ");
		model.addElement("abdfefg");
		DefaultComboBoxModel<Employee> model = new DefaultComboBoxModel<Employee>();
		JComboBox<Employee> cmb = new JAutoCompleteComboBox<Employee>(model);
		Employee employee = new Employee();
		employee.setName("张三");
		employee.setNameEn("zhangsan");
		model.addElement(employee);
		employee = new Employee();
		employee.setName("李四");
		employee.setNameEn("lisi");
		model.addElement(employee);
		employee = new Employee();
		employee.setName("王五");
		employee.setNameEn("wangwu");
		model.addElement(employee);
		employee = new Employee();
		employee.setName("赵六");
		employee.setNameEn("zhaoliu");
		model.addElement(employee);
		employee = new Employee();
		employee.setName("孙七");
		employee.setNameEn("sunqi");
		model.addElement(employee);
		employee = new Employee();
		employee.setName("周八");
		employee.setNameEn("zhouba");
		model.addElement(employee);
		
		frame.getContentPane().add(cmb);
		frame.setSize(400, 80);
		frame.setVisible(true);*/
		
		//ChangeToPinyin pinyin = new ChangeToPinyin();
		
		String test = "您好";
		String str = "你在做什么？what are you nong sa lie?";

        System.out.println(PinyinUtil.getStringPinYin(str));
	}
}

/**
 * 自动完成器。自动找到最匹配的项目，并排在列表的最前面。
 * 
 * @author Turbo Chen
 */

class AutoCompleter<E> implements KeyListener, ItemListener {

	private JComboBox<E> owner = null;
	private JTextField editor = null;
	private int keyCount = 0;
	private boolean isCn = false;

	private ComboBoxModel<E> model = null;

	public AutoCompleter(JComboBox<E> comboBox) {
		owner = comboBox;
		editor = (JTextField) comboBox.getEditor().getEditorComponent();
		editor.addKeyListener(this);
		model = comboBox.getModel();
		owner.addItemListener(this);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		keyCount ++;
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch)
				|| ch == KeyEvent.VK_DELETE) {
			return;
		}

		int caretPosition = editor.getCaretPosition();
		String str = editor.getText();
		if (str.length() == 0) {
			return;
		}
		autoComplete(str, caretPosition);
	}

	/**
	 * 自动完成。根据输入的内容，在列表中找到相似的项目.
	 */
	protected void autoComplete(String strf, int caretPosition) {
		Vector<E> list = getMatchingOptions(strf);
		if (owner != null) {
			model = new DefaultComboBoxModel<E>(list);
			owner.setModel(model);
		}
		if (list.size() > 0) {
			String str = list.get(0).toString();
			//editor.setCaretPosition(caretPosition);
			editor.setText(strf);
			if (owner != null) {
				try {
					owner.showPopup();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * 找到相似的项目, 并且将之排列到数组的最前面。
	 * 
	 * @param str
	 * @return 返回所有项目的列表。
	 */
	protected Vector<E> getMatchingOptions(String str) {
		Vector<E> v = new Vector<E>();
		Vector<E> v1 = new Vector<E>();
		for (int k = 0; k < model.getSize(); k++) {
			Object itemObj = model.getElementAt(k);
			if (itemObj != null) {
				String item = itemObj.toString().toLowerCase();
				String en = BeanUtil.getValue(itemObj, "nameEn").toString();
				if (item.startsWith(str.toLowerCase()) || en.startsWith(str.toLowerCase())) {
					v.add(model.getElementAt(k));
				} else {
					v1.add(model.getElementAt(k));
				}
			} else {
				v1.add(model.getElementAt(k));
			}
		}
		for (int i = 0; i < v1.size(); i++) {
			v.add(v1.get(i));
		}
		/*if (v.isEmpty())
			v.add(e);*/
		return v;
	}

	public void itemStateChanged(ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED) {
			int caretPosition = editor.getCaretPosition();
			if (caretPosition != -1) {
				try {
					editor.moveCaretPosition(caretPosition);
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}