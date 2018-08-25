package com.saleoa.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
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
	
	public static Date getStartDateTime(Date date) {
		String dateStr = datesdf.format(date);
		dateStr += " 00:00:00";
		Date startDateTime = null;
		try {
			startDateTime = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startDateTime;
	}
	
	public static Date getEndDateTime(Date date) {
		String dateStr = datesdf.format(date);
		dateStr += " 23:59:59";
		Date startDateTime = null;
		try {
			startDateTime = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startDateTime;
	}
	
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.MONTH) + 1;
		return year;
	}
	
	public static void main(String[] args) {
		String dateStr = "1000-01-01 00:00:00";
		Date date = parseFullDate(dateStr);
		System.out.println(date);
	}
}
