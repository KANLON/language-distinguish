package com.kanlon.entity;

/**
 * 语言识别的模式，是精准还是模糊
 * @author zhangcanlong
 * @since 2019/1/29 10:23
 **/
public enum DetectMode {
    /**
     * 识别精确的模式，速度较慢
     **/
    PRECISION,

    /**
     * 识别不太精准的模式，速度快
     **/
    IMPRECISION;
}
