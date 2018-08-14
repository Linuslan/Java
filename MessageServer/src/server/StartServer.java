package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer implements Runnable {
	private ServerSocket server = null;
	
	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	private boolean isContinue = true;
	
	public StartServer(ServerSocket server) {
		int port = 20005;
		try {
			this.server = server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		try {
			
			while(true) {
				Socket socket = server.accept();
				new Thread(new Task(socket)).start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public ServerSocket getServer() {
		return server;
	}
	
}
