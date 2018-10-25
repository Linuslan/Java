package com.craftsman.common.util;

public class StringUtil {
	public static boolean isEmpty(String str) {
		if(null == str || str.length() <= 0) {
			return true;
		} else {
			return false;
		}
	}
}
