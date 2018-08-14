package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/*******************************************************************************
 * 时间日期格式转换类

 * 
 * @author Administrator
 * 
 */
public class DateStrFormat {

	/**
	 * 获取当前时间 yyyy-MM-dd
	 * 
	 * @return 返回当前时间 yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		String str = "";
		Date date = new Date();
		str = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		return str;
	}

	/**
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 返回当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		String str = "";
		Date date = new Date();
		str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
				.toString();
		return str;
	}

	/**
	 * 把当前时间转换为（yyyy-MM-dd HH:mm:ss）（GMT+8:00）格式的新时间

	 * 
	 * @return 格式后的新时间

	 */
	public static Date getNewDate() {
		Date newDate = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String newDateStr = format.format(newDate);
		Timestamp timeStamp = Timestamp.valueOf(newDateStr);
		return (java.util.Date) timeStamp;
	}

	/**
	 * 把格式为（yyyy-MM-dd HH:mm:ss）的字符串转换为对应的时间类型

	 * 
	 * @param strDate
	 *            格式为（yyyy-MM-dd HH:mm:ss）的字符串

	 * @return 转换后的对应时间类型
	 */
	public static Date getNewDate(String strDate) {
		if (strDate == null) {
			return null;
		}
		Timestamp timeStamp = Timestamp.valueOf(strDate);
		return (java.util.Date) timeStamp;
	}
	/**
	 * @param strDate
	 *            格式为（yyyy-MM-dd HH:mm:ss）的字符串

	 * @return 转换后的对应时间类型
	 * @throws ParseException 
	 */
	public static Date getNewDate(String strDate,String sFmt) throws ParseException {
          SimpleDateFormat sdf = new SimpleDateFormat ( sFmt ) ; 		
		return sdf.parse(strDate) ; 
	}		
	/**
	 * @param strDate
	 *            格式为（yyyy-MM-dd HH:mm:ss）的字符串

	 * @return 转换后的对应时间类型
	 * @throws ParseException 
	 */
	public static String getNewDate(Date strDate,String sFmt) throws ParseException {
          SimpleDateFormat sdf = new SimpleDateFormat ( sFmt ) ; 		
          return strDate == null ? "": sdf.format(strDate); 	
	}	
	/**
	 * 把格式为时间类型的字符串转换为对应的时间类型
	 * 
	 * @param strDate
	 *            格式为（yyyy-MM-dd HH:mm:ss）的字符串

	 * @return 转换后的对应时间类型字符串

	 */
	public static String getNewDateStr(String strDate) {
		if (strDate == null) {
			return "";
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		Timestamp timeStamp = Timestamp.valueOf(strDate);
		String newDateStr = format.format(timeStamp);
		return newDateStr;
	}

	/**
	 * 把当前时间转换为（yyyy-MM-dd HH:mm:ss）格式的字符串

	 * 
	 * @return 转换后（yyyy-MM-dd HH:mm:ss）格式的字符串

	 */
	public static String getNewDateString() {
		Date newDate = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String newDateStr = format.format(newDate);
		return newDateStr;
	}

	public static String getCutDateString(String dateStr) {
		String str = "";
		str = dateStr.substring(0, 16) + ":00";
		return str;
	}

	@SuppressWarnings("unchecked")
	public static List getSplitPhone(String phoneStr, String splitStr) {
		List list = null;
		String[] phones = phoneStr.split(splitStr);
		list = new ArrayList();
		for (int i = 0; i < phones.length; i++) {
			list.add(phones[i]);
		}
		return list;
	}

	/**
	 * 把时间转换为（yyyy-MM-dd HH:mm:ss）格式的字符串

	 * 
	 * @param date
	 * @return Oct 26, 2010 4:31:54 PM author:laijianbin
	 */
	public static String getDateByMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, month);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				calendar.getTime()).toString();
	}

	/**
	 * 把时间转换为（yyyy-MM-dd HH:mm:ss）格式的字符串

	 * 
	 * @param date
	 * @return Oct 26, 2010 4:31:54 PM author:laijianbin
	 */
	public static String getDateByDay(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, month);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				calendar.getTime()).toString();
	}

	/**
	 * 把时间转换为（yyyy-MM-dd HH:mm:ss）格式的字符串

	 * 
	 * @param date
	 * @return Oct 26, 2010 4:31:54 PM author:laijianbin
	 */
	public static String getEDateByDay(String baft, int day) {
		Calendar calendar = Calendar.getInstance();
		if (baft != null && baft.trim() != "") {
			if (baft.equals("-")) {
				calendar.add(Calendar.DATE, -day);
			} else if (baft.equals("+")) {
				calendar.add(Calendar.DATE, +day);
			} else {
				calendar.add(Calendar.DATE, day);
			}
		}

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				calendar.getTime()).toString();
	}

	/**
	 * 把时间转换为（yyyy-MM-dd HH:mm:ss）格式的字符串

	 * 
	 * @param date
	 * @return Oct 26, 2010 4:31:54 PM author:laijianbin
	 */
	public static Date getDate(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}
	/**
	 * 获取周的第几天
	 * @param day
	 * @return 星期几
	 */
	public static String getStrDay(int day){
		String str="";
		if(day==1){
			str="星期一";
		}
		else if(day==2){
			str="星期二";
		}
		else if(day==3){
			str="星期三";
		}
		else if(day==4){
			str="星期四";
		}
		else if(day==5){
			str="星期五";
		}
		else if(day==6){
			str="星期六";
		}
		else{
			str="星期日";
		}
		return str;
	}
}
