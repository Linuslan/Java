package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import util.SendSms;

public class Task implements Runnable {
	private Socket socket;
	
	public Task(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			handleSocket();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void handleSocket() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String temp;
		int index;
		while((temp=br.readLine()) != null) {
			if((index = temp.indexOf("eof")) != -1) {
				sb.append(temp.substring(0, index));
				break;
			}
			sb.append(temp);
		}
		String msg = sb.toString();
		if(null != msg && !"".equals(msg.trim()) && msg.indexOf("=") >= 0) {
			String[] msgArr = msg.split("=");
			String phone = msgArr[0];
			String content = msgArr[1];
			SendSms.Send(phone, content);
		}
		br.close();
		socket.close();
	}
}
