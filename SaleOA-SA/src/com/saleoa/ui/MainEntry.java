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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuListener;

import com.saleoa.common.utils.ModuleName;

public class MainEntry {
	private JTabbedPane tabPanel = null;
	private static Dimension screenSize = MainEntry.getScreanSize();
	final private static String miniIcon = System.getProperty("user.dir").replace("\\", "/")+"/img/comments.png";
	
	//系统托盘图标
	private static TrayIcon trayIcon;
	
	//系统托盘
	private static SystemTray systemTray;
	public void createMain() {
		int width = (int) (screenSize.width*0.8);
		int height = (int) (screenSize.height*0.8);
				
		final JFrame main = new JFrame();
		JMenuBar menuBar = new JMenuBar();
		main.setJMenuBar(menuBar);
		JMenu menu = new JMenu("模块菜单");
		menuBar.add(menu);
		JMenuItem employeeItem = new JMenuItem(ModuleName.EMPLOYEE);
		employeeItem.setName(ModuleName.EMPLOYEE);
		employeeItem.addActionListener(menuItemListener());
		menu.add(employeeItem);
		JMenuItem levelItem = new JMenuItem(ModuleName.LEVEL);
		levelItem.setName(ModuleName.LEVEL);
		levelItem.addActionListener(menuItemListener());
		menu.add(levelItem);
		JMenuItem salaryItem = new JMenuItem(ModuleName.SALARY);
		salaryItem.setName(ModuleName.SALARY);
		salaryItem.addActionListener(menuItemListener());
		menu.add(salaryItem);
		//JPanel panel = new JPanel();
		
		//获得系统托盘实例
		systemTray = SystemTray.getSystemTray();
		
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File(miniIcon)));
			systemTray.add(trayIcon);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		main.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				main.dispose();//窗口最小化时，dispose该窗口
			}
		});
		
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				/*
				 * 双击显示正常窗口
				 */
				if(e.getClickCount() == 2) {
					main.setExtendedState(JFrame.NORMAL);
					main.setVisible(true);
				}
			}
		});
		
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
				}
			}
		};
		return listener;
	}
	
	public static Dimension getScreanSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize;
	}
	
	public static void main(String[] args) {
		MainEntry entry = new MainEntry();
		entry.createMain();
	}
}
