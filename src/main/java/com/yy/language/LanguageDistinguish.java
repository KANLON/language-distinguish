package com.yy.language;

import com.yy.entity.TranslateType;
import com.yy.utils.ExcelUtil;
import com.yy.utils.HttpRequestUtil;
import com.yy.utils.PropUtil;
import com.yy.utils.StringUtil;
import com.yy.utils.baidu.TransApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 主要是根据输入的语言识别出是那个国家的语言(假定以utf-8编码方式输入)，其中谷歌翻译那里还没测试每月超过200万字符请求是否可以。
 *
 * @author zhangcanlong
 * @date 2019年1月16日
 */
public class LanguageDistinguish {
    /**
     * 存储上一次的请求的时间
     **/
    private static long lastRequestTime = 0;
    private Logger logger = LoggerFactory.getLogger(LanguageDistinguish.class);

    /**
     * 根据字符串得到该字符串的语言名称(中文)
     *
     * @param str 要判断的字符串
     * @return java.lang.String 字符串的语言名称(中文),如果不能确定，则返回null
     **/
    public String getLanguageByString(String str) {
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException("字符串不能为空");
        }
        Integer[] unicodes = StringUtil.string2UnicodeInts(str);
        boolean isCertain = false;
        boolean isEnglish = true;
        String languageStr = null;
        for (Integer unicode : unicodes) {
            languageStr = getOneLanguageByUnicode(unicode);
            //英文判断，如果有不少于127的，则不是英文
            if (unicode > 127) {
                isEnglish = false;
            }
            if (languageStr != null) {
                isCertain = true;
                break;
            }
        }
        //如果不能判断，继续其他判断
        if (isCertain) {
            return languageStr;
        }
        //英文判断(这个是大概率有可能是)
        if (isEnglish) {
            return "英语";
        }
        //存储当前的时间
        long currentTime = System.nanoTime();
        //一秒的纳秒值
        int secondNanoValue = 1000_000_000;
        //如果距离上一次请求大于等于1秒时间，则使用谷歌翻译，否则使用百度翻译
        System.out.println("所用时间为：" + (currentTime - lastRequestTime));
        if ((currentTime - lastRequestTime) >= secondNanoValue) {
            languageStr = getLanguageFromGoogle(str);
        } else {
            languageStr = getLanguageFromBaidu(str);
        }
        lastRequestTime = System.nanoTime();
        return languageStr;
    }

    /**
     * 根据字符串调用谷歌翻译功能识别语言类别（有流量限制，需要限制每秒一次，识别语言种类多，大多数都能识别）
     *
     * @param str 要识别的字符串
     * @return java.lang.String 返回识别得到的语言，如果识别不了返回null
     **/
    private String getLanguageFromGoogle(String str) {
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
        String languageCode = array.get(2) == null ? null : array.get(2).toString();
        responseStr = ExcelUtil.getTranslateLanguageAndCodeFromExcel(TranslateType.GOOGLE).get(languageCode);
        return responseStr;
    }

    /**
     * 根据字符串调用百度翻译功能识别语言类别（每月200万字符免费，超过之后就要收费，无流量限制，识别语言少）
     *
     * @param str 要识别的字符串
     * @return java.lang.String 返回识别得到的语言，如果识别不了返回null
     **/
    private String getLanguageFromBaidu(String str) {
        logger.info("使用了百度翻译");
        String language = null;
        TransApi api = new TransApi(PropUtil.INSTANCE.getStringByKey("APP_ID"), PropUtil.INSTANCE.getStringByKey("SECURITY_KEY"));
        String result = api.getTransResult(str, "auto", "en");
        JSONObject object = new JSONObject(result);
        Map<String, Object> map = object.toMap();
        //获取错误
        final String languageKey = "from";
        final String errorCodeKey = "error_code";
        final String signErrorCode = "54001";
        final String passwordErrorCode = "52003";
        final String tooQuickRequestErrorCode = "54003";
        final String tooMoreRequestErrorCode = "54004";
        if (map.get(languageKey) == null) {
            String errorCode = map.get(errorCodeKey).toString();
            if (signErrorCode.equals(errorCode) || passwordErrorCode.equals(errorCode)) {
                logger.error("签名错误,请检查app-id和密码是否填写正确," + result);
            } else if (tooQuickRequestErrorCode.equals(errorCode)) {
                logger.error("调用频率太高，请降低调用频率," + result);
            } else if (tooMoreRequestErrorCode.equals(errorCode)) {
                logger.error("免费次数已经用完，请充值," + result);
            } else {
                logger.error("其他原因，" + result);
            }
        } else {
            String languageCode = map.get(languageKey).toString();
            language = ExcelUtil.getTranslateLanguageAndCodeFromExcel(TranslateType.BAIDU).get(languageCode);
        }
        return language;
    }

    /**
     * 根据某个unicode值判断属于哪个特定的语言，如果符合多个语言，则返回null
     *
     * @param unicode 要判断的unicode值
     * @return java.lang.String 不能判断，则返回null
     **/
    private String getOneLanguageByUnicode(int unicode) {
        final int maxUnicode = 0xFFFF;
        if (unicode < 0 || unicode > maxUnicode) {
            throw new IllegalArgumentException("非正常识别的unicode整数，当前只能识别第一平面的");
        }
        Map<String, Object> map = ExcelUtil.getLanguageAndUnicodeFromExcel();
        int[] unicodes = (int[]) map.get(ExcelUtil.UNICODES_KEY);
        String[] language = (String[]) map.get(ExcelUtil.LANGUAGES_KEY);
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
    private int findIndexFromUnicodes(int[] unicodesRange, int unicode) {
        if (unicodesRange == null || unicodesRange.length <= 1) {
            return -1;
        }
        //二分查找
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
     * 获取tk值，tk值仔细上网查过之后，每次翻译的文字不同，参数中的tk值就会不同，ticket这种策略就是google用来防爬虫的。tk和文字以及TKK有关，TKK也是实时变化的，
     *
     * @param text 要识别的字符串
     * @return java.lang.String 返回tk值
     **/
    private String token(String text) {
        String tk = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            FileReader reader = new FileReader(this.getClass().getClassLoader().getResource("./Google.js").getFile());
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
