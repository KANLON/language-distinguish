package com.kanlon.utils;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * shuyo语言识别开源框架使用工具类
 * @author zhangcanlong
 * @since 2019-1-25
 **/
public class ShuyoLangDetectorUtil {

    private static Logger logger = LoggerFactory.getLogger(ShuyoLangDetectorUtil.class);

    private final static String LANG_PATH = "lang";

    static {
        try {
            //加载语言库
            String path = ShuyoLangDetectorUtil.class.getClassLoader().getResource(LANG_PATH).getPath();
            File langFile = new File(path);
            if (langFile.exists()) {
                DetectorFactory.loadProfile(langFile);
            } else {
                //如果不存在，加载默认jar内原件包的语言包
                logger.info("加载默认的语言库");
                DetectorFactory.loadProfile(getProfileStr());
            }
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
        if (StringUtil.isEmptyOrWhiteSpace(originalStr)) {
            return null;
        }
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

    /**
     * 加载解析jar包，加载默认的语言库
     *
     * @return java.util.List<java.lang.String>
     **/
    private static List<String> getProfileStr() {
        List<String> list = new ArrayList<>();
        String path = ShuyoLangDetectorUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        JarFile localJarFile = null;
        try {
            localJarFile = new JarFile(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> files = localJarFile.entries();
        while (files.hasMoreElements()) {
            String innerFile = files.nextElement().getName();
            if (innerFile.startsWith(LANG_PATH + "/")) {
                logger.info("加载了：" + innerFile);
                InputStream inputStream = ShuyoLangDetectorUtil.class.getClassLoader().getResourceAsStream(innerFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                try {
                    while (((line = br.readLine()) != null)) {
                        builder.append(line);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(builder.toString());
            }
        }
        return list;
    }

}
