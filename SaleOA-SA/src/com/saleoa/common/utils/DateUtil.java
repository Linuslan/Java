package com.saleoa.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String formatFullDate(Date date) {
		return sdf.format(date);
	}
	
	public static Date parseFullDate(String dateStr) {
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch(Exception ex) {
			date = null;
			ex.printStackTrace();
		}
		return date;
	}
	
	public static void main(String[] args) {
		String dateStr = "1000-01-01 00:00:00";
		Date date = parseFullDate(dateStr);
		System.out.println(date);
	}
}
