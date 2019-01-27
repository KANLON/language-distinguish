package com.yy.utils;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhangcanlong
 * @description shuyo语言识别开源框架使用工具类
 * @since 2019-1-25
 **/
public class ShuyoLangDetectorUtil {
    private static Logger logger = LoggerFactory.getLogger(ShuyoLangDetectorUtil.class);

    public static void main(String[] args) {
        System.out.println(ShuyoLangDetectorUtil.detect("uzalo october\n"));
    }

    /**
     * 加载语言库
     */
    static {
        try {
            DetectorFactory.loadProfile(Thread.currentThread().getContextClassLoader().getResource("lang").getPath());
        } catch (LangDetectException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据字符串识别字符串的语言
     *
     * @param originalStr 要识别的字符串
     * @return 返回识别到的字符串的语言代码
     */
    public static String detect(String originalStr) {
        String langUsed = "";
        Detector detector;
        try {
            detector = DetectorFactory.create();
            detector.append(originalStr);
            langUsed = detector.detect();
        } catch (LangDetectException e) {
            logger.error("识别：“" + originalStr + "”失败。", e);
            for (int i = 0; i < originalStr.length(); i++) {
                System.out.print(Integer.toHexString(originalStr.charAt(i)) + " ");
            }
        }
        return langUsed;
    }

}
