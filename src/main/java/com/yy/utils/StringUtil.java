package com.yy.utils;

/**
 * 字符串的工具类
 *
 * @Author zhangcanlong
 * @Date 2019/1/18 10:44
 **/
public class StringUtil {
    private StringUtil() {
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            // 取出每一个字符
            char c = str.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * 根据字符串得到字符的unicode的整数数组
     *
     * @param str
     * @return java.lang.Integer[]
     **/
    public static Integer[] string2UnicodeInts(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        Integer[] unicodes = new Integer[str.length()];
        for (int i = 0; i < str.length(); i++) {
            unicodes[i] = (int) str.charAt(i);
        }
        return unicodes;
    }
}
