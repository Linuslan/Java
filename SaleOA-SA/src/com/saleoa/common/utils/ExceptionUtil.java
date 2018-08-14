package com.saleoa.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public static void throwExcep(String msg) throws Exception {
		throw new Exception(msg);
	}
	
	public static void throwRuntimeExcep(String msg) throws RuntimeException {
		throw new RuntimeException(msg);
	}
	
	public static void throwRuntimeExcep(Exception ex) throws RuntimeException {
		throw new RuntimeException(ExceptionUtil.getStackTrace(ex));
	}
	
	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
}
