package com.saleoa.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.saleoa.common.constant.ModuleName;
import com.saleoa.service.IEmployeeService;
import com.saleoa.service.IEmployeeServiceImpl;
import com.saleoa.service.ISaleLogService;
import com.saleoa.service.ISaleLogServiceImpl;
import com.saleoa.service.ISaleService;
import com.saleoa.service.ISaleServiceImpl;
import com.saleoa.ui.balanceLevel.BalanceLevelPanel;
import com.saleoa.ui.department.DepartmentPanel;
import com.saleoa.ui.employee.EmployeePanel;
import com.saleoa.ui.employeeRole.EmployeeRolePanel;
import com.saleoa.ui.level.LevelPanel;
import com.saleoa.ui.managerLevel.ManagerLevelPanel;
import com.saleoa.ui.salary.SalaryPanel;
import com.saleoa.ui.salaryConfig.SalaryConfigPanel;
import com.saleoa.ui.sale.SalePanel;
import com.saleoa.ui.saleSalary.SaleSalaryPanel;

public class MainEntry {
	private JTabbedPane tabPanel = null;
	private static Dimension screenSize = MainEntry.getScreanSize();
	final private static String miniIcon = System.getProperty("user.dir").replace("\\", "/")+"/img/comments.png";
	public final static JFrame main = new JFrame();
	private ISaleService saleService = new ISaleServiceImpl();
	private ISaleLogService saleLogService = new ISaleLogServiceImpl();
	private IEmployeeService employeeService = new IEmployeeServiceImpl();
	
	//系统托盘图标
	private static TrayIcon trayIcon;
	
	//系统托盘
	private static SystemTray systemTray;
	public void createMain() {
		int width = (int) (screenSize.width*0.8);
		int height = (int) (screenSize.height*0.8);
		
		/*try {
			this.employeeService.select(null);
			this.saleService.select(null);
			this.saleLogService.select(null);
		} catch(Exception ex) {
			System.out.println("初始化数据异常");
			ex.printStackTrace();
		}*/
		
		JMenuBar menuBar = new JMenuBar();
		main.setJMenuBar(menuBar);
		JMenu menu = new JMenu("模块菜单");
		menuBar.add(menu);
		
		JMenu sysMenu = new JMenu("系统菜单");
		menuBar.add(sysMenu);
		JMenuItem employeeItem = new JMenuItem(ModuleName.EMPLOYEE);
		employeeItem.setName(ModuleName.EMPLOYEE);
		employeeItem.addActionListener(menuItemListener());
		menu.add(employeeItem);
		JMenuItem levelItem = new JMenuItem(ModuleName.LEVEL);
		levelItem.setName(ModuleName.LEVEL);
		levelItem.addActionListener(menuItemListener());
		sysMenu.add(levelItem);
		JMenuItem salaryItem = new JMenuItem(ModuleName.SALARY);
		salaryItem.setName(ModuleName.SALARY);
		salaryItem.addActionListener(menuItemListener());
		menu.add(salaryItem);
		JMenuItem saleItem = new JMenuItem(ModuleName.SALE);
		saleItem.setName(ModuleName.SALE);
		saleItem.addActionListener(menuItemListener());
		menu.add(saleItem);
		
		JMenuItem departmentItem = new JMenuItem(ModuleName.DEPARTMENT);
		departmentItem.setName(ModuleName.DEPARTMENT);
		departmentItem.addActionListener(menuItemListener());
		sysMenu.add(departmentItem);
		
		JMenuItem saleSalaryItem = new JMenuItem(ModuleName.SALESALARY);
		saleSalaryItem.setName(ModuleName.SALESALARY);
		saleSalaryItem.addActionListener(menuItemListener());
		menu.add(saleSalaryItem);
		
		JMenuItem managerLevelItem = new JMenuItem(ModuleName.MANAGERLEVEL);
		managerLevelItem.setName(ModuleName.MANAGERLEVEL);
		managerLevelItem.addActionListener(menuItemListener());
		sysMenu.add(managerLevelItem);
		
		JMenuItem employeeRoleItem = new JMenuItem(ModuleName.EMPLOYEEROLE);
		employeeRoleItem.setName(ModuleName.EMPLOYEEROLE);
		employeeRoleItem.addActionListener(menuItemListener());
		sysMenu.add(employeeRoleItem);
		
		JMenuItem salaryConfigItem = new JMenuItem(ModuleName.SALARYCONFIG);
		salaryConfigItem.setName(ModuleName.SALARYCONFIG);
		salaryConfigItem.addActionListener(menuItemListener());
		sysMenu.add(salaryConfigItem);
		
		JMenuItem balanceLevelItem = new JMenuItem(ModuleName.BALANCELEVEL);
		balanceLevelItem.setName(ModuleName.BALANCELEVEL);
		balanceLevelItem.addActionListener(menuItemListener());
		sysMenu.add(balanceLevelItem);
		//JPanel panel = new JPanel();
		
		//获得系统托盘实例
//		systemTray = SystemTray.getSystemTray();
//		
//		try {
//			trayIcon = new TrayIcon(ImageIO.read(new File(miniIcon)));
//			systemTray.add(trayIcon);
//		} catch(Exception ex) {
//			ex.printStackTrace();
//		}
		
//		main.addWindowListener(new WindowAdapter() {
//			public void windowIconified(WindowEvent e) {
//				main.dispose();//窗口最小化时，dispose该窗口
//			}
//		});
//		
//		trayIcon.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				
//				/*
//				 * 双击显示正常窗口
//				 */
//				if(e.getClickCount() == 2) {
//					main.setExtendedState(JFrame.NORMAL);
//					main.setVisible(true);
//				}
//			}
//		});
		
		/*final JButton startBtn = new JButton("启动服务");
		startBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		JButton stopBtn = new JButton("停止服务");
		stopBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		panel.add(startBtn);
		panel.add(stopBtn);
		main.add(panel);*/
		
		main.setSize(width, height);
		main.setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tabPanel = new JTabbedPane();
		main.add(tabPanel);
		/*LevelPanel levelPanel = new LevelPanel();
		tabpane.add(levelPanel);*/
	}
	
	public ActionListener menuItemListener() {
		ActionListener listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				JMenuItem item = (JMenuItem) event.getSource();
				Component[] components = tabPanel.getComponents();
				for(int i = 0; i < components.length; i ++) {
					if(item.getName().equals(components[i].getName())) {
						return;
					}
				}
				if(ModuleName.LEVEL.equals(item.getName())) {
					LevelPanel levelPanel = new LevelPanel();
					tabPanel.add(levelPanel);
				} else if(ModuleName.EMPLOYEE.equals(item.getName())) {
					EmployeePanel employeePanel = new EmployeePanel();
					tabPanel.add(employeePanel);
				} else if(ModuleName.SALE.equals(item.getName())) {
					SalePanel salePanel = new SalePanel();
					tabPanel.add(salePanel);
				} else if(ModuleName.SALARY.equals(item.getName())) {
					SalaryPanel salaryPanel = new SalaryPanel();
					tabPanel.add(salaryPanel);
				} else if(ModuleName.DEPARTMENT.equals(item.getName())) {
					DepartmentPanel departmentPanel = new DepartmentPanel();
					tabPanel.add(departmentPanel);
				} else if(ModuleName.SALESALARY.equals(item.getName())) {
					SaleSalaryPanel saleSalaryPanel = new SaleSalaryPanel();
					tabPanel.add(saleSalaryPanel);
				} else if(ModuleName.MANAGERLEVEL.equals(item.getName())) {
					ManagerLevelPanel managerLevelPanel = new ManagerLevelPanel();
					tabPanel.add(managerLevelPanel);
				} else if(ModuleName.EMPLOYEEROLE.equals(item.getName())) {
					EmployeeRolePanel employeeRolePanel = new EmployeeRolePanel();
					tabPanel.add(employeeRolePanel);
				} else if(ModuleName.SALARYCONFIG.equals(item.getName())) {
					SalaryConfigPanel salaryConfigPanel = new SalaryConfigPanel();
					tabPanel.add(salaryConfigPanel);
				} else if(ModuleName.BALANCELEVEL.equals(item.getName())) {
					BalanceLevelPanel balanceLevelPanel = new BalanceLevelPanel();
					tabPanel.add(balanceLevelPanel);
				}
			}
		};
		return listener;
	}
	
	public static Dimension getScreanSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize;
	}
	
	public static void main(String[] args) throws Exception {
		//JFrame.setDefaultLookAndFeelDecorated(true);
		//JDialog.setDefaultLookAndFeelDecorated(true);
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		try {
			//BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			//BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
			//org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		//MainEntry entry = new MainEntry();
		//entry.createMain();
		Login login = new Login();
		login.init();
	}
}
