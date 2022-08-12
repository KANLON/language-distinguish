package com.kanlon;

import com.kanlon.entity.DetectMode;
import com.kanlon.language.LanguageDistinguish;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 测试语言识别
 *
 * @author zhangcanlong
 * @since 2019/1/28 11:13
 */
@Slf4j
public class LanguageDistinguishTest {

    /**
     * 测试识别
     */
    @Test
    public void testDistinguish() {
        String lang1 = "com.cybozu.labs.langdetect.Detector.getProbabilities";
        String lang2 = "尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。";
        String lang3 = "com.cybozu.labs.langdetect.Detector.getProbabilities尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。";
        String lang4 = "BTS (방탄소년단) 'Save ME' Official MV\n";
        String lang5 = "\"เพลงอื่นๆในอัลบั้ม\n" + "01 Fly : https://www.youtube.com/watch?v=eaIsRux0EUs\n" + "02 Can't (못하겠어) : https://www.youtube.com/watch?v=eduPd3zejwc\n" + "03 See the light (빛이나) : https://www.youtube.com/watch?v=n9O6ZxIiNfo\n" + "04 Fish : https://www.youtube.com/watch?v=q8UdPXzH2qA\n" + "05 Rewind : https://www.youtube.com/watch?v=J9jfoApJous\n" + "06 Beggin on my knees : https://www.youtube.com/watch?v=TOzU28PLjkM\n" + "07 Something good : https://www.youtube.com/watch?v=dtupeRACACc&feature=youtu.be\n" + "\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + "ห้ามรีอัพโหลดและใช้ในเชิงพาณิชย์ [Do not Re-upload & Not for sale]\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + ":: Credit::\n" + "Hangul : music.naver\n" + "Eng trans : KpopViral.com\n" + "Thai lyrics & Trans  : BAEBOOIrene\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + "แปลผิดพลาดประการใดขออภัยด้วยนะคะ\n" + "กดไลค์และคอมเมนต์เป็นกำลังใจให้คนทำซับมากมาย\"\n";
        String lang6 = "Всем доброго дня! Сегодняшнее видео посвящено автомобилям, которые разгоняется до скорости в 300 и больше! Здесь и Porsche GT2, и Lamborghini Urus и Huracan, Audi R8, Mercedes E63 S, а также Bentley Continental GT! Это уже 3 видео по данной теме, так что если вы не видели первые две подборки, то обязательно посмотрите! Приятного просмотра!\n";
        String lang7 = "1--";
        String lang8 = "";
        log.info("识别1的文本为：{},结果为：{}", lang1, LanguageDistinguish.getLanguageByString(lang1, DetectMode.PRECISION));
        log.info("识别2的文本为：{},结果为：{}", lang2, LanguageDistinguish.getLanguageByString(lang2, DetectMode.PRECISION));
        log.info("识别3的文本为：{},结果为：{}", lang3, LanguageDistinguish.getLanguageByString(lang3, DetectMode.PRECISION));
        log.info("识别4的文本为：{},结果为：{}", lang4, LanguageDistinguish.getLanguageByString(lang4, DetectMode.PRECISION));
        log.info("识别5的文本为：{},结果为：{}", lang5, LanguageDistinguish.getLanguageByString(lang5, DetectMode.PRECISION));
        log.info("识别6的文本为：{},结果为：{}", lang6, LanguageDistinguish.getLanguageByString(lang6, DetectMode.PRECISION));
        //如果检测的字符串不含有特征字符，即识别不了，会返回"{}"
        log.info("识别7的文本为：{},结果为：{}", lang7, LanguageDistinguish.getLanguageByString(lang7, DetectMode.PRECISION));
        //如果检测的字符串为空，或空字符串，则会抛出异常，因此需要自己判空
        try {
            log.info("识别8的文本为：{},结果为：{}", lang8, LanguageDistinguish.getLanguageByString(lang8, DetectMode.PRECISION));
        } catch (Exception e) {
            log.error("这里会抛出空报错！", e);
        }
    }
}
