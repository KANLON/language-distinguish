package com.yy.language;

import com.alibaba.excel.util.StringUtils;
import com.yy.utils.ExcelUtil;
import com.yy.utils.HttpRequestUtil;
import com.yy.utils.ShuyoLangDetectorUtil;
import com.yy.utils.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 主要是根据输入的语言识别出是那个国家的语言(假定以utf-8编码方式输入)
 *
 * @author zhangcanlong
 * @date 2019年1月16日
 */
public class LanguageDistinguish {
    /**
     * 存储上一次的请求的时间
     **/
    private static long lastRequestTime = 0;
    private static Logger logger = LoggerFactory.getLogger(LanguageDistinguish.class);

    //测试
    public static void main(String[] args) {
        List<String> list = ExcelUtil.getTestStr();
        System.out.println("开始时间：" + new Date());
        long count = 0;
        for (int i = 0; i < 1000_000; i++) {
            System.out.println(i + "次，" + getLanguageByString(list.get(i % list.size())));
        }
        System.out.println("结束时间：" + new Date() + "\n次数：" + count);

    }
    /**
     * 根据字符串得到该字符串的语言名称(中文)
     *
     * @param str 要判断的字符串
     * @return java.lang.String 字符串的语言名称(中文),如果不能确定，则返回null
     **/
    public static String getLanguageByString(String str) {
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException("字符串不能为空");
        }
        //去除特殊的字符
        str = StringUtil.removeSpecialChar(str);
        char[] unicodes = str.toCharArray();
        boolean isEnglish = true;
        String languageStr = null;
        //存放unnicode值大于127的字符，即为非字母和数字
        StringBuilder noLetterAndNumBuilder = new StringBuilder();
        //存放unnicode值小于等于127的字符，即为字母和数字
        StringBuilder letterAndNumBuilder = new StringBuilder();
        for (int i = 0; i < unicodes.length; i++) {
            //英文判断，如果有不少于127的，则不是英文。如果已经判断不是英文了，直接跳过。如果是空格，则上一个字符是什么则添加到哪里
            if (unicodes[i] > 127 || (unicodes[i] == 32 && i >= 1 && unicodes[i - 1] > 127)) {
                isEnglish = false;
                noLetterAndNumBuilder.append(unicodes[i]);
            } else {
                letterAndNumBuilder.append(unicodes[i]);
            }
        }

        //存在其他形式语言
        if (noLetterAndNumBuilder.length() > 0) {
            languageStr = getOneLanguageByUnicode(noLetterAndNumBuilder.charAt(0), ExcelUtil.getLanguageAndUnicodeFromExcel());
            //如果根据unicode不能直接判断语言的，继续执行开源框架和谷歌翻译进行判断
            if (StringUtils.isEmpty(languageStr)) {
                languageStr = ShuyoLangDetectorUtil.detect(noLetterAndNumBuilder.toString());
                if (StringUtils.isEmpty(languageStr)) {
                    try {
                        long currentTime = System.nanoTime();
                        long betweenTime = (currentTime - lastRequestTime) / 1000_000;
                        Thread.sleep(betweenTime > 1000 ? 1000 - betweenTime : 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    languageStr = getLanguageFromGoogle(noLetterAndNumBuilder.toString());
                    lastRequestTime = System.nanoTime();
                }
                if (!StringUtils.isEmpty(languageStr)) {
                    return constructLanguageProportion(languageStr, noLetterAndNumBuilder.length(), "en", str.length() - noLetterAndNumBuilder.length());
                }
            }
            //如果unicode值符合，直接返回
            return constructLanguageProportion(languageStr, noLetterAndNumBuilder.length(), "en", str.length() - noLetterAndNumBuilder.length());
        } else {
            //判断，英语，法语，西班牙语
            return ShuyoLangDetectorUtil.detect(str) + ":1.00";
        }
    }

    /**
     * 根据字符串调用谷歌翻译功能识别语言类别（有流量限制，需要限制每秒一次，识别语言种类多，大多数都能识别）
     *
     * @param str 要识别的字符串
     * @return java.lang.String 返回识别得到的语言，如果识别不了返回null
     **/
    private static String getLanguageFromGoogle(String str) {
        logger.info("使用了谷歌翻译");
        String responseStr = null;
        String url = "https://translate.google.com/translate_a/single";
        StringBuilder param = new StringBuilder();
        param.append("client=webapp&sl=auto&tl=zh-CN&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=1&ssel=3&tsel=0&kc=1");
        param.append("&tk=");
        param.append(token(str));
        param.append("&q=");
        try {
            param.append(URLEncoder.encode(str, "UTF-8"));
            long startTime = System.currentTimeMillis();
            long startNanoTime = System.nanoTime();
            responseStr = HttpRequestUtil.sendGet(url, param.toString());
            logger.info("所用纳秒时间：" + (System.nanoTime() - startNanoTime) + "，共：" + (System.currentTimeMillis() - startTime) + "毫秒");
        } catch (Exception e) {
            logger.error("调用谷歌翻译功能识别语言类别出现错误", e);
        }
        JSONObject jsonObject = new JSONObject("{\"result\":" + responseStr + "}");
        JSONArray array = jsonObject.getJSONArray("result");
        responseStr = array.get(2) == null ? null : array.get(2).toString();
        return responseStr;
    }


    /**
     * 根据字符判断是不是特殊字符
     *
     * @param c 要判断的字符
     * @return java.lang.Boolean
     **/
    public static Boolean isSpecialLanguage(Character c) {
        return getOneLanguageByUnicode(c, ExcelUtil.getSpecialLanguageAndUnicodeFromFromExcel()) != null;
    }

    /**
     * 根据某个unicode值判断属于哪个特定的语言，如果符合多个语言，则返回null
     *
     * @param unicode 要判断的unicode值
     * @param languageTypeMap 语言类别的map，是识别特定语言还是特殊语言
     * @return java.lang.String 不能判断，则返回null
     **/
    private static String getOneLanguageByUnicode(int unicode, Map<String, Object> languageTypeMap) {
        final int maxUnicode = 0xFFFF;
        if (unicode < 0 || unicode > maxUnicode) {
            throw new IllegalArgumentException("非正常识别的unicode整数，当前只能识别第一平面的");
        }
        int[] unicodes = (int[]) languageTypeMap.get(ExcelUtil.UNICODES_KEY);
        String[] language = (String[]) languageTypeMap.get(ExcelUtil.LANGUAGES_KEY);
        //从区间数组中找到不大于且最接近该unicode数的下标
        int index = findIndexFromUnicodes(unicodes, unicode);
        //如果等于-1，则表示当前还不确定是那种语言
        if (index == -1) {
            return null;
        }
        return language[index / 2];
    }

    /**
     * 从区间数组（有序）中找到不大于且最接近该unicode数的下标
     *
     * @param unicode 要查找的unicode值
     * @return int 返回下标值,找不到则返回-1
     **/
    private static int findIndexFromUnicodes(int[] unicodesRange, int unicode) {
        if (unicodesRange == null || unicodesRange.length <= 1) {
            return -1;
        }
        //类二分查找
        int start = 0;
        int end = unicodesRange.length - 1;
        int midIndex;
        while (start <= end) {
            if (unicode < unicodesRange[start] || unicode > unicodesRange[end]) {
                return -1;
            }
            midIndex = start + (end - start) / 2;
            if (unicode == unicodesRange[midIndex]) {
                return midIndex;
            }
            if (unicode < unicodesRange[midIndex]) {
                if (midIndex % 2 == 1) {
                    if (unicode >= unicodesRange[midIndex - 1]) {
                        return midIndex - 1;
                    } else {
                        end = midIndex - 2;
                    }
                } else {
                    end = midIndex - 1;
                }
            } else {
                if (midIndex % 2 == 0) {
                    if (unicode <= unicodesRange[midIndex + 1]) {
                        return midIndex + 1;
                    } else {
                        start = midIndex + 2;
                    }
                } else {
                    start = midIndex + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 根据语言的名称及它们在字符中所占的字符长度，构造它们之间
     * @param language1 语言1
     * @param length1   语言1所占长度
     * @param language2 语言2
     * @param length2   语言2所占长度
     * @return java.lang.String 返回  zh:0.91,ko:0.12 之类的形式，如果其中要给语言的占比小于0.01，则返回一种语言 zh:1
     *  的占比
     **/
    private static String constructLanguageProportion(String language1, int length1, String language2, int length2) {
        if (length1 + length2 <= 0) {
            throw new IllegalArgumentException("总长度不能小于等于0");
        }
        StringBuilder returnBuilder = new StringBuilder();
        double d1 = length1;
        double d2 = length2;
        double sumLength = length1 + length2;
        BigDecimal proportion1 = new BigDecimal(length1 / sumLength, MathContext.DECIMAL32);
        BigDecimal proportion2 = new BigDecimal(length2 / sumLength, MathContext.DECIMAL32);
        BigDecimal minPrecision = new BigDecimal(0.01, MathContext.DECIMAL32);
        if (proportion1.compareTo(minPrecision) <= 0 || proportion2.compareTo(minPrecision) <= 0) {
            if (proportion1.compareTo(minPrecision) <= 0) {
                returnBuilder.append(language2);
            } else {
                returnBuilder.append(language1);
            }
            returnBuilder.append(":1.00");
            return returnBuilder.toString();
        }
        returnBuilder.append(language1);
        returnBuilder.append(":");
        returnBuilder.append(proportion1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        returnBuilder.append(",");
        returnBuilder.append(language2);
        returnBuilder.append(":");
        returnBuilder.append(proportion2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        return returnBuilder.toString();
    }

    /**
     * 获取tk值，tk值仔细上网查过之后，每次翻译的文字不同，参数中的tk值就会不同，ticket这种策略就是google用来防爬虫的。tk和文字以及TKK有关，TKK也是实时变化的，
     *
     * @param text 要识别的字符串
     * @return java.lang.String 返回tk值
     **/
    private static String token(String text) {
        String tk = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            FileReader reader = new FileReader(LanguageDistinguish.class.getClassLoader().getResource("./Google.js").getFile());
            engine.eval(reader);
            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable) engine;
                tk = String.valueOf(invoke.invokeFunction("token", text));
            }
        } catch (Exception e) {
            logger.error("未知获取谷歌翻译的tk值时的错误：", e);
        }
        return tk;
    }

}
