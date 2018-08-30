package com.saleoa.common.utils;

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
	 * 通过时间查询时间所在月份的首日时间
	 * @param date
	 * @return
	 */
	public static Date getCustomFirstDateOfMonthByDate(Date date) {
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		Date firstDate = null;
		try {
			SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
			int startDay = salaryConfig.getSalaryStartDay();
			String monthDate = monthSdf.format(date);
			String firstDateStr = monthDate+"-"+(startDay > 9 ? startDay : "0"+startDay);
			try {
				firstDate = datesdf.parse(firstDateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return firstDate;
	}
	
	public static String getCustomFirstDateStrOfMonthByDate(Date date) {
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		String firstDateStr = "";
		try {
			SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
			int startDay = salaryConfig.getSalaryStartDay();
			String monthDate = monthSdf.format(date);
			firstDateStr = monthDate+"-"+(startDay > 9 ? startDay : "0"+startDay)+" 00:00:00";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return firstDateStr;
	}
	
	public static Date getCustomEndDateOfMonthByDate(Date date) {
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		String monthDate = monthSdf.format(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Date endDate = null;
		try {
			SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
			//int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int endDay = salaryConfig.getSalaryEndDay();
			monthDate += "-"+(endDay > 9 ? endDay : "0"+endDay);
			endDate = sdf.parse(monthDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null != endDate) {
			endDate = DateUtil.getEndDateTime(endDate);
		}
		return endDate;
	}
	
	public static String getCustomEndDateStrOfMonthByDate(Date date) {
		ISalaryConfigService salaryConfigService = new ISalaryConfigServiceImpl();
		String monthDate = monthSdf.format(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//monthDate += "-"+endDay +" 23:59:59";
		try {
			SalaryConfig salaryConfig = salaryConfigService.selectById(1L);
			//int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int endDay = salaryConfig.getSalaryEndDay();
			monthDate += "-"+(endDay > 9 ? endDay : "0"+endDay);
			monthDate += "-"+endDay +" 23:59:59";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return monthDate;
	}
	
	public static void main(String[] args) {
		/*String dateStr = "1000-01-01 00:00:00";
		Date date = parseFullDate(dateStr);
		System.out.println(date);*/
		
		System.out.println(DateUtil.getCustomEndDateOfMonthByDate(new Date()));
	}
}
