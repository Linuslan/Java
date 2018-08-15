package com.saleoa.ui;

import java.awt.BorderLayout;
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
import java.io.IOException;
import java.net.ServerSocket;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainEntry {
	private static Dimension screenSize = MainEntry.getScreanSize();
	final private static String miniIcon = System.getProperty("user.dir").replace("\\", "/")+"/img/comments.png";
	
	//系统托盘图标
	private static TrayIcon trayIcon;
	
	//系统托盘
	private static SystemTray systemTray;
	public void createMain() {
		int width = 800;
		int height = 600;
		
		final JFrame main = new JFrame();
		main.setLayout(new BorderLayout(3,3)) ;
		/*main.add(new JButton("东"),BorderLayout.EAST) ;
		main.add(new JButton("西"),BorderLayout.WEST) ;
		main.add(new JButton("南"),BorderLayout.SOUTH) ;
		main.add(new JButton("北"),BorderLayout.NORTH) ;*/
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setSize(width, height);
		
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
		
		final JButton startBtn = new JButton("启动服务");
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
		//main.add(panel);
		main.add(panel,BorderLayout.CENTER) ;
		main.setPreferredSize(new Dimension(200, 300));
		main.setSize(width, height);
		main.setLocation((screenSize.width-width)/2, (screenSize.height-height)/2);
		main.setVisible(true);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
