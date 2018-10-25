package com.craftsman.common.util;

import java.util.Random;

public class SerialNoUtil {
	/**
	 * 生产单号
	 * **/
	public static String getGroupNodeUniqueID() {
		String formatStr = "yyMMdd" + buildRandom(4);
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				formatStr);
		java.util.Date currentTime = new java.util.Date();
		String sid = formatter.format(currentTime);
		return sid;
	}
	
	/**
	 * 生成12位序列号，其中8位随机数
	 * @return
	 */
	public static String getGroupNodeUniqueIDTwelveBit() {
		String formatStr = "yyMMdd" + buildRandom(8);
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				formatStr);
		java.util.Date currentTime = new java.util.Date();
		String sid = formatter.format(currentTime);
		return sid;
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 随即生成固定长度字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 随即生成固定长度数字串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomInteger(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString().toUpperCase();
	}

	public static void main(String [] args) {
		int num = buildRandom(14);
		System.out.println(num);
	}
}
