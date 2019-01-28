package com.kanlon.utils;

import com.kanlon.language.LanguageDistinguish;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * 谷歌翻译的工具类
 *
 * @author zhangcanlong
 * @since 2019/1/28 16:02
 **/
public class GoogleTranslateUtil {
    private static Logger logger = LoggerFactory.getLogger(GoogleTranslateUtil.class);

    /**
     * 根据字符串调用谷歌翻译功能识别语言类别（有流量限制，需要限制每秒一次，识别语言种类多，大多数都能识别）
     *
     * @param str 要识别的字符串
     * @return java.lang.String 返回识别得到的语言，如果识别不了返回null
     **/
    public static String getLanguageFromGoogle(String str) {
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
     * 获取tk值，tk值仔细上网查过之后，每次翻译的文字不同，参数中的tk值就会不同，ticket这种策略就是google用来防爬虫的。tk和文字以及TKK有关，TKK也是实时变化的，
     *
     * @param text 要识别的字符串
     * @return java.lang.String 返回tk值
     **/
    private static String token(String text) {
        String tk = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            engine.eval(new InputStreamReader(LanguageDistinguish.class.getClassLoader().getResourceAsStream("Google.js")));
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
