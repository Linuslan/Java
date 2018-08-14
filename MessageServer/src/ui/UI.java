package ui;

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

import server.StartServer;

public class UI {
	private static Dimension screenSize = UI.getScreanSize();
	final private static String miniIcon = System.getProperty("user.dir").replace("\\", "/")+"/src/img/comments.png";
	
	//ϵͳ����ͼ��
	private static TrayIcon trayIcon;
	
	//ϵͳ����
	private static SystemTray systemTray;
	private ServerSocket server = null;
	private Thread thread = null;
	public void createMain() {
		int width = 300;
		int height = 80;
		
		final JFrame main = new JFrame();
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
				StartServer ss = new StartServer(server);
				server = ss.getServer();
				startBtn.setEnabled(false);
				thread = new Thread(ss);
				thread.start();
			}
			
		});
		JButton stopBtn = new JButton("ֹͣ����");
		stopBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					server.close();
					startBtn.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		panel.add(startBtn);
		panel.add(stopBtn);
		main.add(panel);
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
}
