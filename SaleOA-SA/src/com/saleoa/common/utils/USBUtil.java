package com.saleoa.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class USBUtil {
	private static String[] snArr = {"96788F24"};
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
			// ��ȡע�����Ϣ
			Process ps = null;
			ps = Runtime.getRuntime().exec(commond);
			ps.getOutputStream().close();
			InputStreamReader i = new InputStreamReader(ps.getInputStream());
			String line;
			BufferedReader ir = new BufferedReader(i);
			// ����Ϣ�������
			while ((line = ir.readLine()) != null) {
				if (line.contains("USB\\VID")) {
					count++;
					for (String s : line.split("    ")) {
						System.out.println(s);
						if (s.contains("USB\\VID")) {
							for (String ss : s.split("\\\\")) {
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
			if(snStr.equals(sn)) {
				check = true;
			}
		}
		if(!check) {
			ExceptionUtil.throwExcep("δ��⵽��Ȩ��U��");
		}
		if(count > 1) {
			ExceptionUtil.throwExcep("��γ�δ��Ȩ��U��");
		}
		return check;
	}
	
	/*public static void main(String[] args) {
		if(checkUSB()) {
			System.out.println("��⵽��ȷ��U��");
		} else {
			System.out.println("���U���쳣");
		}
	}*/
}
