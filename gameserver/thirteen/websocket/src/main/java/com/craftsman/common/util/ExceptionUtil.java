package com.craftsman.common.util;

import com.alibaba.fastjson.JSONObject;
import com.craftsman.common.constant.enums.ErrorCode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class ExceptionUtil {
	public static void throwException(String msg) throws Exception {
		throw new Exception(msg);
	}
	
	public static void throwRuntimeException(String msg) throws RuntimeException {
		throw new RuntimeException(msg);
	}
	
	public static void throwRuntimeException(Exception ex) throws RuntimeException {
		throw new RuntimeException(ExceptionUtil.getStackTrace(ex));
	}

	public static void throwExceptionWithJson(String code, String msg) throws Exception {
		ExceptionUtil.throwException(ExceptionUtil.getJsonMsg(code, msg));
	}

	public static void throwExceptionWithJsonByCode(int errorCode) throws Exception {
		ErrorCode code = ErrorCode.getByIndex(errorCode);
		String index = String.valueOf(code.getIndex());
		String msg = code.getName();
		throwExceptionWithJson(index, msg);
	}

	public static void throwExceptionJsonByError(ErrorCode error) throws Exception {
		String index = String.valueOf(error.getIndex());
		String msg = error.getName();
		throwExceptionWithJson(index, msg);
	}
	
	public static String getStackTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public static String getJsonMsg(String code, String msg) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("msg", msg);
		return JSONObject.toJSONString(map);
	}

	public static String getJSONByError(ErrorCode error) {
		String index = String.valueOf(error.getIndex());
		String msg = error.getName();
		return ExceptionUtil.getJsonMsg(index, msg);
	}
}
