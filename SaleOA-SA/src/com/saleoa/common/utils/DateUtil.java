package com.saleoa.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String formatFullDate(Date date) {
		return sdf.format(date);
	}
}
