package com.craftsman.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.saleoa.model.SalaryConfig;
import com.saleoa.service.ISalaryConfigService;
import com.saleoa.service.ISalaryConfigServiceImpl;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat monthSdf = new SimpleDateFormat("yyyy-MM");
	public static String formatFullDate(Date date) {
		return sdf.format(date);
	}
	
	public static String formatMonthDate(Date date) {
		return monthSdf.format(date);
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
	
	/**
	 * ͨ��ʱ���ѯʱ�������·ݵ�����ʱ��
	 * @param date
	 * @return
	 */
	public static Date getCustomFirstDateOfMonthByDate(Date date) {
		Date firstDate = null;
		firstDate = DateUtil.parseFullDate(DateUtil.getCustomFirstDateStrOfMonthByDate(date));
		return firstDate;
	}
	
	public static String getCustomFirstDateStrOfMonthByDate(Date date) {
		String firstDateStr = "";
		try {
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			String endSplitDateStr = DateUtil.getCustomEndDateStr(year, month);
			Date endSplitDate = DateUtil.parseFullDate(endSplitDateStr);
			if(date.before(endSplitDate) || date.equals(endSplitDate)) {
				firstDateStr = DateUtil.getCustomStartDateStr(year, month);
			} else {
				month ++;
				if(month > 12) {
					month = 1;
					year ++;
				}
				firstDateStr = DateUtil.getCustomStartDateStr(year, month);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return firstDateStr;
	}
	
	public static Date getCustomEndDateOfMonthByDate(Date date) {
		Date splitDate = null;
		try {
			int year = DateUtil.getYear(date);
			int month = DateUtil.getMonth(date);
			String splitDateStr = DateUtil.getCustomEndDateStr(year, month);
			splitDate = DateUtil.parseFullDate(splitDateStr);
			if(date.after(splitDate)) {
				month ++;
				if(month > 12) {
					month = 1;
					year ++;
				}
				splitDateStr = DateUtil.getCustomEndDateStr(year, month);
				splitDate = DateUtil.parseFullDate(splitDateStr);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return splitDate;
	}
	
	public static String getCustomEndDateStrOfMonthByDate(Date date) {
		String monthDate = "";
		monthDate = DateUtil.formatFullDate(DateUtil.getCustomEndDateOfMonthByDate(date));
		return monthDate;
	}
	
	public static String getCustomStartDateStr(int year, int month) throws Exception {
		String startDate = "";
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
		int startDay = salaryConfig.getSalaryStartDay();
		month = month - 1;
		if(month <= 0) {
			month = 12;
			year --;
		}
		startDate = year + "-" + (month > 9 ? month : "0"+month) + "-" + (startDay > 9 ? startDay : "0" + startDay) + " 00:00:00";
		return startDate;
	}
	
	public static String getCustomEndDateStr(int year, int month) throws Exception {
		String startDate = "";
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
		int endDay = salaryConfig.getSalaryEndDay();
		startDate = year + "-" + (month > 9 ? month : "0"+month) + "-" + (endDay > 9 ? endDay : "0" + endDay) + " 23:59:59";
		return startDate;
	}
	
	public static void main(String[] args) {
		String dateStr = "2018-08-25 23:59:59";
		Date date = parseFullDate(dateStr);
		//System.out.println(date);
		
		System.out.println(DateUtil.getCustomFirstDateStrOfMonthByDate(date));
		System.out.println(DateUtil.getCustomEndDateStrOfMonthByDate(date));
	}
}
