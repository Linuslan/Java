package com.craftsman.common.util;

import java.text.DecimalFormat;

public class NumberFormatUtils {
    public static String format(double num, String format) {
        DecimalFormat df = new DecimalFormat(format);
        String numStr = df.format(num);
        return numStr;
    }
}
