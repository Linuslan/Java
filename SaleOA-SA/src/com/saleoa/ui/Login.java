package com.saleoa.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import javax.swing.Timer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.saleoa.base.IBaseService;
import com.saleoa.common.constant.ConfigFactory;
import com.saleoa.common.constant.FormCss;
import com.saleoa.common.utils.MD5Util;
import com.saleoa.common.utils.StringUtil;
import com.saleoa.common.utils.USBUtil;
import com.saleoa.service.*;

public class Login {
	private static Dimension screenSize = MainEntry.getScreanSize();
	private static String[] userArr = {"Xxa", "Zy"};
	private static String[] pwdArr = {"ac3eaf64cb4126206febffd95658a4df", "e36cb84be0948ffba6516dd5f7f78749"};
	private final JProgressBar progressBar = new JProgressBar();
	JFrame progressFrame = new JFrame("数据加载中...");
	static int currentProgress = 0;
	
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
		/*try {
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
		}*/
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
					initConfig();
					loginFrame.dispose();

					progressFrame.setSize(350, 100);
					progressFrame.setLocationRelativeTo(null);
					progressFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

					JPanel panel = new JPanel();

					// 设置进度的 最小值 和 最大值
					progressBar.setMinimum(0);
					progressBar.setMaximum(100);

					// 设置当前进度值
					progressBar.setValue(currentProgress);

					// 绘制百分比文本（进度条中间显示的百分数）
					progressBar.setStringPainted(true);

					// 添加进度改变通知
					progressBar.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent e) {
							System.out.println("当前进度值: " + progressBar.getValue() + "; " +
									"进度百分比: " + progressBar.getPercentComplete());
						}
					});

					// 添加到内容面板
					panel.add(progressBar);

					progressFrame.setContentPane(panel);
					progressFrame.setVisible(true);

					try {
						loadData();
						//MainEntry entry = new MainEntry();
						//entry.createMain();
					} catch(Exception ex) {
						progressBar.setString("数据加载失败，请联系开发人员");
					}
				} else {
					tipLbl.setText("密码错误");
					tipLbl.setForeground(Color.RED);
				}
			}
			
		});
		
		loginFrame.setSize(400, 230);
		loginFrame.setLocation((screenSize.width-400)/2, (screenSize.height-230)/2);
		loginFrame.setVisible(true);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initConfig() {
		IBalanceLevelService balanceLevelService = new IBalanceLevelServiceImpl();
		ConfigFactory.SERVICE_MAP.put("balanceLevelService", balanceLevelService);
		IDepartmentService departmentService = new IDepartmentServiceImpl();
		ConfigFactory.SERVICE_MAP.put("departmentService", departmentService);
		IEmployeeRoleService employeeRoleService = new IEmployeeRoleServiceImpl();
		ConfigFactory.SERVICE_MAP.put("employeeRoleService", employeeRoleService);
		IEmployeeService employeeService = new IEmployeeServiceImpl();
		ConfigFactory.SERVICE_MAP.put("employeeService", employeeService);
		IManagerLevelService managerLevelService = new IManagerLevelServiceImpl();
		ConfigFactory.SERVICE_MAP.put("managerLevelService", managerLevelService);
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		ConfigFactory.SERVICE_MAP.put("salaryConfigService", salaryConfigService);
		ISalaryService salaryService = new ISalaryServiceImpl();
		ConfigFactory.SERVICE_MAP.put("salaryService", salaryService);
		ISaleLogService saleLogService = new ISaleLogServiceImpl();
		ConfigFactory.SERVICE_MAP.put("saleLogService", saleLogService);
		ISaleSalaryService saleSalaryService = new ISaleSalaryServiceImpl();
		ConfigFactory.SERVICE_MAP.put("saleSalaryService", saleSalaryService);
		ISaleService saleService = new ISaleServiceImpl();
		ConfigFactory.SERVICE_MAP.put("saleService", saleService);
		ILevelService levelService = new ILevelServiceImpl();
		ConfigFactory.SERVICE_MAP.put("levelService", levelService);
	}

	public void loadData() throws Exception {
		new Thread() {
			public void run() {
				Set<Map.Entry<String, IBaseService>> entrySet = ConfigFactory.SERVICE_MAP.entrySet();
				Iterator<Map.Entry<String, IBaseService>> iterator = entrySet.iterator();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				int val = 0;
				while(iterator.hasNext()) {
					Map.Entry<String, IBaseService> entry = iterator.next();
					IBaseService baseService = entry.getValue();
					baseService.selectAll();
					val += 10;
					progressBar.setValue(val);
					try {
						this.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				progressBar.setString("数据加载成功");
				progressFrame.dispose();
				MainEntry entry = new MainEntry();
				entry.createMain();
			}
		}.start();
	}
}
