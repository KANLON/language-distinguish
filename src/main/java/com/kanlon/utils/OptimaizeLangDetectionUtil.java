package com.kanlon.utils;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangcanlong
 * @description Optimaize的开源库识别语言的工具类（字符串太短的时候，识别率不了）
 * @since 2019-1-24
 **/
public class OptimaizeLangDetectionUtil {

    //加载所有内置语种
    private static List<LanguageProfile> languageProfiles = null;

    static {
        try {
            languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据字符串识别语言
     *
     * @param languageStr 需要识别的字符串
     * @return java.lang.String 识别不了返回null
     **/
    public static String detect(String languageStr) {
        // 创建识别器
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard()).withProfiles(languageProfiles).build();
        ;
        // 创建文本对象工厂
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        // 识别
        TextObject textObject = textObjectFactory.forText(languageStr);
        Optional<LdLocale> lang = languageDetector.detect(textObject);
        if (!lang.isPresent()) {
            System.out.println("语种识别失败，可能文本太短或混合了多国语言");
            return null;
        }
        String languageCode = lang.get().getLanguage();
        //如果是简体中文或繁体中文都返回zh（谷歌翻译的语言代码只有中文）
        if ("zh-cn".equals(languageCode) || "zh-tw".equals(languageCode)) {
            return "zh-CN";
        }
        return lang.get().getLanguage();
    }

}