package com.saleoa.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.MD5Util;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.common.utils.USBUtil;

public class Login {
	private static Dimension screenSize = MainEntry.getScreanSize();
	private static String[] userArr = {"Xxa", "Zy"};
	private static String[] pwdArr = {"ac3eaf64cb4126206febffd95658a4df", "e36cb84be0948ffba6516dd5f7f78749"};
	
	public void delete(String name) {
		File file = new File(name);
		File[] files = file.listFiles();
		if(null == files || 0 >= files.length) {
			file.delete();
			return;
		}
		for(int i = 0; i < files.length; i ++) {
			File subFile = files[i];
			if(subFile.isDirectory()) {
				System.out.println(subFile.getPath());
				delete(subFile.getPath());
			}
			try {
				subFile.delete();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		file.delete();
	}
	
	public void init() {
		try {
			boolean check = USBUtil.checkUSB();
		} catch(Exception ex) {
			if(ex.getMessage().indexOf("销毁")>=0) {
				this.delete("export");
				this.delete("lib");
				this.delete("jre");
				this.delete("img");
			}
			JOptionPane.showMessageDialog(null, ex.getMessage(), "温馨提示",JOptionPane.WARNING_MESSAGE);
			return;
		}
		final JLabel tipLbl = new JLabel();
		int width = (int) (screenSize.width*0.4);
		int height = (int) (screenSize.height*0.4);
		final JFrame loginFrame = new JFrame();
		JPanel panel = new JPanel();
		panel.setLayout(null);
		loginFrame.add(panel);
		loginFrame.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				loginFrame.dispose();//窗口最小化时，dispose该窗口
			}
		});
		JLabel nameLbl = new JLabel("登录名：");
		nameLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		nameLbl.setLocation(FormCss.getLocation(null, null));
		panel.add(nameLbl);
		
		final JTextField nameIpt = new JTextField();
		nameIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(nameIpt);
		nameIpt.setLocation(FormCss.getLocation(nameLbl, null));
		nameIpt.setText("");
		nameIpt.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				tipLbl.setText("");
			}
		});
		
		JLabel pwdLbl = new JLabel("密码：");
		pwdLbl.setSize(FormCss.LABEL_WIDTH, FormCss.HEIGHT);
		pwdLbl.setLocation(FormCss.getLocation(null, nameIpt));
		panel.add(pwdLbl);
		
		final JPasswordField pwdIpt = new JPasswordField();
		pwdIpt.setSize(FormCss.FORM_WIDTH, FormCss.HEIGHT);
		panel.add(pwdIpt);
		pwdIpt.setLocation(FormCss.getLocation(pwdLbl, nameIpt));
		pwdIpt.setText("");
		pwdIpt.addFocusListener(new FocusListener() {
			
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				tipLbl.setText("");
			}
		});
		
		JButton loginBtn = new JButton("登录");
		loginBtn.setSize(60, 40);
		panel.add(loginBtn);
		Point p = FormCss.getLocation(null, pwdIpt);
		loginBtn.setLocation(new Point(150, 130));
		
		
		panel.add(tipLbl);
		tipLbl.setSize(300, 30);
		tipLbl.setLocation(new Point(120, 90));
		loginBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String name = nameIpt.getText();
				String pwd = new String(pwdIpt.getPassword());
				if(StringUtil.isEmpty(name)) {
					tipLbl.setText("请输入用户名");
					tipLbl.setForeground(Color.RED);
					return ;
				}
				if(StringUtil.isEmpty(pwd)) {
					tipLbl.setText("请输入密码");
					tipLbl.setForeground(Color.RED);
					return ;
				}
				pwd = MD5Util.md5(MD5Util.md5(pwd));
				boolean isSuccess = false;
				for(int i = 0; i < userArr.length; i ++) {
					String userName = userArr[i];
					String password = pwdArr[i];
					if(userName.equals(name) && password.equals(pwd)) {
						isSuccess = true;
						break;
					}
				}
				if(isSuccess) {
					loginFrame.dispose();
					MainEntry entry = new MainEntry();
					entry.createMain();
				} else {
					tipLbl.setText("密码错误");
					tipLbl.setForeground(Color.RED);
				}
			}
			
		});
		
		loginFrame.setSize(400, 230);
		loginFrame.setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
