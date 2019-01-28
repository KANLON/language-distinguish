package com.kanlon.utils;

import com.kanlon.language.LanguageDistinguish;

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
    public static int[] string2UnicodeInts(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        int[] unicodes = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            unicodes[i] = str.charAt(i);
        }
        return unicodes;
    }

    /**
     * 去除字符串中的特殊字符
     *
     * @param str 要去除的字符串
     * @return java.lang.String
     **/
    public static String removeSpecialChar(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        //替换装饰的字符
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            //如果字符是属于UTF-16高半区，utf-8的编码代表是两个字符，则将其及其后面的字符设置为" "
            if (chars[i] >= 0xD800 && chars[i] <= 0xDBFF) {
                chars[i] = ' ';
                chars[++i] = ' ';
            }
            if (LanguageDistinguish.isSpecialLanguage(chars[i])) {
                chars[i] = ' ';
            }
        }
        str = new String(chars);
        //替换字符中所有链接为空
        String reg = "[a-zA-z]+://[^\\s]*";
        str = str.replaceAll(reg, "");
        //替换所有邮箱为空
        String emailReg = "[a-zA-z0-9]+@[^\\s]*";
        str = str.replaceAll(emailReg, "");
        //替换非必要英文标点符号
        String signReg = "[!@#\\$%\\^&\\*\\(\\)_\\+=\\{\\}\\\\\\[\\]\\?\\/\\|#:><;\\-\'\",́`~\\.]";
        str = str.replaceAll(signReg, " ");
        //替换数字，前后有空白字符的则替换
        String numReg = "(([0-9]+[\\s]+)|([\\s]+[0-9]+))";
        str = str.replaceAll(numReg, "");
        //替换所有空白字符为，单一空格字符
        str = str.replaceAll("\\s+", " ");
        return str;
    }


    /**
     * 判断字符串是不是全是空白字符串或者空字符串
     *
     * @param str 要判断的字符串
     * @return boolean true或者false
     **/
    public static boolean isEmptyOrWhiteSpace(String str) {
        return str == null || str.length() <= 0 || str.matches("\\s");
    }
}
