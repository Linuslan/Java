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
	
	//ϵͳ����ͼ��
	private static TrayIcon trayIcon;
	
	//ϵͳ����
	private static SystemTray systemTray;
	public void createMain() {
		int width = 800;
		int height = 600;
		
		final JFrame main = new JFrame();
		main.setLayout(new BorderLayout(3,3)) ;
		/*main.add(new JButton("��"),BorderLayout.EAST) ;
		main.add(new JButton("��"),BorderLayout.WEST) ;
		main.add(new JButton("��"),BorderLayout.SOUTH) ;
		main.add(new JButton("��"),BorderLayout.NORTH) ;*/
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setSize(width, height);
		
		//���ϵͳ����ʵ��
		systemTray = SystemTray.getSystemTray();
		
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File(miniIcon)));
			systemTray.add(trayIcon);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		main.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				main.dispose();//������С��ʱ��dispose�ô���
			}
		});
		
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				/*
				 * ˫����ʾ��������
				 */
				if(e.getClickCount() == 2) {
					main.setExtendedState(JFrame.NORMAL);
					main.setVisible(true);
				}
			}
		});
		
		final JButton startBtn = new JButton("��������");
		startBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			}
			
		});
		JButton stopBtn = new JButton("ֹͣ����");
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
