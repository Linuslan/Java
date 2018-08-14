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
 * ʱ�����ڸ�ʽת����

 * 
 * @author Administrator
 * 
 */
public class DateStrFormat {

	/**
	 * ��ȡ��ǰʱ�� yyyy-MM-dd
	 * 
	 * @return ���ص�ǰʱ�� yyyy-MM-dd
	 */
	public static String getCurrentDate() {
		String str = "";
		Date date = new Date();
		str = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		return str;
	}

	/**
	 * ��ȡ��ǰʱ�� yyyy-MM-dd HH:mm:ss
	 * 
	 * @return ���ص�ǰʱ�� yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		String str = "";
		Date date = new Date();
		str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
				.toString();
		return str;
	}

	/**
	 * �ѵ�ǰʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����GMT+8:00����ʽ����ʱ��

	 * 
	 * @return ��ʽ�����ʱ��

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
	 * �Ѹ�ʽΪ��yyyy-MM-dd HH:mm:ss�����ַ���ת��Ϊ��Ӧ��ʱ������

	 * 
	 * @param strDate
	 *            ��ʽΪ��yyyy-MM-dd HH:mm:ss�����ַ���

	 * @return ת����Ķ�Ӧʱ������
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
	 *            ��ʽΪ��yyyy-MM-dd HH:mm:ss�����ַ���

	 * @return ת����Ķ�Ӧʱ������
	 * @throws ParseException 
	 */
	public static Date getNewDate(String strDate,String sFmt) throws ParseException {
          SimpleDateFormat sdf = new SimpleDateFormat ( sFmt ) ; 		
		return sdf.parse(strDate) ; 
	}		
	/**
	 * @param strDate
	 *            ��ʽΪ��yyyy-MM-dd HH:mm:ss�����ַ���

	 * @return ת����Ķ�Ӧʱ������
	 * @throws ParseException 
	 */
	public static String getNewDate(Date strDate,String sFmt) throws ParseException {
          SimpleDateFormat sdf = new SimpleDateFormat ( sFmt ) ; 		
          return strDate == null ? "": sdf.format(strDate); 	
	}	
	/**
	 * �Ѹ�ʽΪʱ�����͵��ַ���ת��Ϊ��Ӧ��ʱ������
	 * 
	 * @param strDate
	 *            ��ʽΪ��yyyy-MM-dd HH:mm:ss�����ַ���

	 * @return ת����Ķ�Ӧʱ�������ַ���

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
	 * �ѵ�ǰʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����ʽ���ַ���

	 * 
	 * @return ת����yyyy-MM-dd HH:mm:ss����ʽ���ַ���

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
	 * ��ʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����ʽ���ַ���

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
	 * ��ʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����ʽ���ַ���

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
	 * ��ʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����ʽ���ַ���

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
	 * ��ʱ��ת��Ϊ��yyyy-MM-dd HH:mm:ss����ʽ���ַ���

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
	 * ��ȡ�ܵĵڼ���
	 * @param day
	 * @return ���ڼ�
	 */
	public static String getStrDay(int day){
		String str="";
		if(day==1){
			str="����һ";
		}
		else if(day==2){
			str="���ڶ�";
		}
		else if(day==3){
			str="������";
		}
		else if(day==4){
			str="������";
		}
		else if(day==5){
			str="������";
		}
		else if(day==6){
			str="������";
		}
		else{
			str="������";
		}
		return str;
	}
}
