package com.kanlon.entity;

/**
 * json中对象的名称的枚举类
 *
 * @author zhangcanlong
 * @since 2019-1-28
 **/
public enum JsonKeyType {

    /**
     * 特殊语言的unicode
     **/
    SPECIAL_LANGUAGE_UNICODE("special-language-unicode"),
    /**
     * 能够识别的语言的unicode
     **/
    ABLE_JUDGE_BY_UNICODE("able-judge-by-unicode"),
    /**
     * 百度翻译API支持的语言种类
     **/
    BAIDU_TRANSLATE_TYPE("baidu-translate-type"),
    /**
     * 谷歌翻译支持的语言种类
     **/
    GOOGLE_TRANSLATE_TYPE("google-translate-type"),
    /**
     * shuyo的开源框架支持的语言种类
     **/
    SHUYO_LANGUAGE_DETECT("shuyo-language-detect");


    /**
     * 对象的key值
     **/
    private String key;

    JsonKeyType(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }}
