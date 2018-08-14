package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import ui.UI;
import util.SendSms;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UI ui = new UI();
		ui.createMain();
	}
}
