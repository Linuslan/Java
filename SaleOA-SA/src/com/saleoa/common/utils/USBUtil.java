package com.saleoa.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class USBUtil {
	//
	private static String[] snArr = {"960483c13efc08f659a9c90c05740bb2", "f89a78304d57b5059a7599a140f84eee"};
	public static String getSerialNumber(String drive) {
		String result = "";
		try {
			File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n"
					+ "Set objDrive = colDrives.item(\""
					+ drive
					+ "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber"; // see note
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.trim();
	} 
	
	public static boolean checkUSB() throws Exception {
		String commond = "reg query HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\services\\USBSTOR\\Enum";
		String vid = "";
		String pid = "";
		String sn = "";
		int count = 0;
		try {
			// 获取注册表信息
			Process ps = null;
			ps = Runtime.getRuntime().exec(commond);
			ps.getOutputStream().close();
			InputStreamReader i = new InputStreamReader(ps.getInputStream());
			String line;
			BufferedReader ir = new BufferedReader(i);
			// 将信息分离出来
			while ((line = ir.readLine()) != null) {
				if (line.contains("USB\\VID")) {
					count++;
					for (String s : line.split("    ")) {
						System.out.println(s);
						if (s.contains("USB\\VID")) {
							for (String ss : s.split("\\\\")) {
								//System.out.println(ss);
								if (ss.contains("VID")) {
									String[] ssArr = ss.split("&");
									vid = ssArr[0];
									pid = ssArr[1];
								}else {
									sn = ss;
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean check = false;
		for(int i = 0; i < snArr.length; i ++) {
			String snStr = snArr[i];
			if(snStr.equals(MD5Util.md5(MD5Util.md5(sn)))) {
				check = true;
			}
		}
		if(!check) {
			ExceptionUtil.throwExcep("未检测到授权的U盘，开始执行销毁");
		}
		if(count > 1) {
			ExceptionUtil.throwExcep("请拔出未授权的U盘");
		}
		return check;
	}
	
	public static void main(String[] args) {
		String md5 = MD5Util.md5(MD5Util.md5("yex123456"));
		System.out.println(md5);
		/*try {
			checkUSB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
