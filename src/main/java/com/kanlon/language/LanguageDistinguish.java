package com.kanlon.language;

import com.kanlon.entity.DetectMode;
import com.kanlon.utils.GoogleTranslateUtil;
import com.kanlon.utils.JsonUtil;
import com.kanlon.utils.ShuyoLangDetectorUtil;
import com.kanlon.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.List;

/**
 * 主要是根据输入的语言识别出是那个国家的语言(假定以utf-8编码方式输入)
 *
 * @author zhangcanlong
 * @since 2019年1月16日
 */
public class LanguageDistinguish {

    private static Logger logger = LoggerFactory.getLogger(LanguageDistinguish.class);
    private static final String ENGLISH_CODE = "en";

    /**
     * 测试
     *
     * @param args 输入参数
     **/
    public static void main(String[] args) {
        List<String> list = JsonUtil.getInstance().getTestStr();
        System.out.println("开始时间：" + new Date());
        for (int i = 0; i < list.size(); i++) {
            logger.info("识别的原始字符串：" + list.get(i % list.size()));
            System.out.println(i + "次，" + getLanguageByString(list.get(i % list.size()), DetectMode.PRECISION));
        }
        System.out.println("结束时间：" + new Date());
        //System.out.println(LanguageDistinguish.getLanguageByString("Luxusné Porsche, Sajfa a jazda života Exkluzívny pohľad na cestu za top nabíjačkou v strednej Európe od ZSE. Na predstavenie sme sa presúvali v expresnom Porsche so Sajfom, MartinomzMartina a Zedňom A.K.A. oranžový Ford. Užite si cestu, naše kecy a malú ukážku Porsche a Audi e-tron. Zedňo:https://www.instagram.com/matozednicek/?hl=enMartinzmartina: https://www.instagram.com/martinzmartina/?hl=skSajfa: https://www.instagram.com/sajfa/?hl=skHudba: Atmospherica - Squiid:https://www.youtube.com/watch?v=33xW2djac3IKokab - Got U (Ready or Not):https://www.youtube.com/watch?v=_whGk6HjUAQFall - Cospe:https://www.youtube.com/watch?v=TBK3JZdpTDsVĎAKA ZA ODBER!#draho #rdmgaraz #vlogTento kanál vznikol na základe toho, že občas navštívim zaujímavé miesta, a som rád, že sa takto s nimi môžem podeliť ďalej. Ak by ste o mne chceli vedieť viac, tak som Drahomír Piok a kanál som založil pre pridávanie VLOGOV. Pracujem ako novinár v Startitupe.Sledovať ma určite môžete aj tu:https://www.instagram.com/draho/https://www.facebook.com/DrahomirPiokHN/ sajfa,porsche,martinzmartina,panamera,etron,audi,918 spyder,jaguar,ipace,bmw,draho,draho vlog,drahomír piok,budča,zse", DetectMode.PRECISION));

    }

    /**
     * 根据字符串得到该字符串的语言代码
     *
     * @param str 要判断的字符串
     * @param mode 判断模式，精确还是模糊
     * @return java.lang.String 字符串的语言代码,如果不能确定，则返回null
     **/
    public static String getLanguageByString(String str, DetectMode mode) {
        if (str == null || str.length() <= 0) {
            throw new IllegalArgumentException("字符串不能为空");
        }
        //去除特殊的字符
        str = StringUtil.removeSpecialChar(str);
        //存放unicode值大于127的字符，即为非字母和数字
        StringBuilder noLetterAndNumBuilder = new StringBuilder();
        //存放unicode值小于等于127的字符，即为字母和数字
        StringBuilder letterAndNumBuilder = new StringBuilder();

        //以空格划分字符，以此归类
        String[] allStrs = str.split("\\s");
        for (int i = 0; i < allStrs.length; i++) {
            //在每个单独字符串中判断，一旦有大于127的，则将其放入对应的区域
            boolean isLetterStr = true;
            for (int j = 0; j < allStrs[i].length(); j++) {
                if (allStrs[i].charAt(j) > 127) {
                    noLetterAndNumBuilder.append(allStrs[i]);
                    noLetterAndNumBuilder.append(" ");
                    isLetterStr = false;
                    break;
                }
            }
            if (isLetterStr) {
                letterAndNumBuilder.append(allStrs[i]);
                letterAndNumBuilder.append(" ");
            }
        }
        logger.info("大于127的字符串：" + noLetterAndNumBuilder.toString());
        logger.info("小于127的字符串：" + letterAndNumBuilder.toString());
        String noLetterAndNumLanguage = getBig127UnicodeLanguage(noLetterAndNumBuilder.toString());

        String letterLanguage = null;
        if (!StringUtil.isEmptyOrWhiteSpace(letterAndNumBuilder.toString())) {
            //判断纯字母的语言，如英语，法语，西班牙语等
            letterLanguage = ShuyoLangDetectorUtil.detect(letterAndNumBuilder.toString());
            //如果是精确的模式，才再次调用谷歌翻译核对
            if (mode.equals(DetectMode.PRECISION) && !ENGLISH_CODE.equals(letterLanguage)) {
                letterLanguage = GoogleTranslateUtil.getLanguageFromGoogle(letterAndNumBuilder.toString());
            }
        }

        //判断是否只有一种语言
        boolean isOnlyOneLanguage = (noLetterAndNumLanguage == null && letterLanguage == null) || (noLetterAndNumLanguage != null && noLetterAndNumLanguage.equals(letterLanguage));
        if (isOnlyOneLanguage) {
            return constructLanguageProportion(noLetterAndNumLanguage, noLetterAndNumBuilder.length(), null, 0);
        }
        return constructLanguageProportion(noLetterAndNumLanguage, noLetterAndNumBuilder.length(), letterLanguage, letterAndNumBuilder.length());
    }


    /**
     * 根据字符串调用开源框架和谷歌翻译判定是那种语言，该字符串的unicode含有大于127的字符
     *
     * @param noLetterAndNumStr 要判断的字符
     * @return java.lang.String 返回判断后的单一语言，这里不支持多语言判断，如果判断不了，返回null
     **/
    private static String getBig127UnicodeLanguage(String noLetterAndNumStr) {
        if (StringUtil.isEmptyOrWhiteSpace(noLetterAndNumStr)) {
            return null;
        }
        String languageStr;
        //继续执行开源框架,谷歌翻译进行判断
        languageStr = ShuyoLangDetectorUtil.detect(noLetterAndNumStr);
        if (StringUtil.isEmptyOrWhiteSpace(languageStr)) {
            languageStr = GoogleTranslateUtil.getLanguageFromGoogle(noLetterAndNumStr);
        }
        if (!StringUtil.isEmptyOrWhiteSpace(languageStr)) {
            return languageStr;
        }
        //如果unicode值符合，直接返回
        return languageStr;
    }

    /**
     * 根据字符判断是不是特殊字符
     *
     * @param c 要判断的字符
     * @return java.lang.Boolean
     **/
    public static Boolean isSpecialLanguage(char c) {
        //从区间数组中找到不大于且最接近该unicode数的下标
        int index = findIndexFromUnicodes(JsonUtil.getInstance().getSpecialLanguageUnicode(), c);
        return index > -1;
    }

    /**
     * 根据某个unicode值判断属于哪个特定的语言，(这个方法暂时没有用了，泰文有两种语言，会导致识别不准，以后都使用开源框架识别)
     *
     * @param unicode 要判断的unicode值
     * @return java.lang.String 不能判断，则返回null
     **/
    @Deprecated
    private static String getOneLanguageByUnicode(int unicode) {
        final int maxUnicode = 0xFFFF;
        if (unicode < 0 || unicode > maxUnicode) {
            throw new IllegalArgumentException("非正常识别的unicode整数，当前只能识别第一平面的");
        }
        Integer[] unicodes = JsonUtil.getInstance().getAbleJudgeUnicodes();
        String[] language = JsonUtil.getInstance().getAbleJudgeLanguages();
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
     * @param unicodesRange 要查找的unicode范围
     * @param unicode 要查找的unicode值
     * @return int 返回下标值,找不到则返回-1
     **/
    private static int findIndexFromUnicodes(Integer[] unicodesRange, int unicode) {
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
     *
     * @param language1 语言1
     * @param length1   语言1所占长度
     * @param language2 语言2
     * @param length2   语言2所占长度
     * @return java.lang.String 返回  zh:0.91,ko:0.12 之类的形式，如果其中要给语言的占比小于0.01，则返回一种语言 zh:1
     * 的占比
     **/
    private static String constructLanguageProportion(String language1, int length1, String language2, int length2) {
        if (length1 + length2 <= 0) {
            throw new IllegalArgumentException("总长度不能小于等于0");
        }
        StringBuilder returnBuilder = new StringBuilder();
        double sumLength = length1 + length2;
        BigDecimal proportion1 = new BigDecimal(length1 / sumLength, MathContext.DECIMAL32);
        BigDecimal proportion2 = new BigDecimal(length2 / sumLength, MathContext.DECIMAL32);
        BigDecimal minPrecision = new BigDecimal(0.01, MathContext.DECIMAL32);
        returnBuilder.append("{\"");
        if (proportion1.compareTo(minPrecision) <= 0 || proportion2.compareTo(minPrecision) <= 0) {
            if (proportion1.compareTo(minPrecision) <= 0) {
                returnBuilder.append(language2);
            } else {
                returnBuilder.append(language1);
            }

            returnBuilder.append("\":1.00}");
            return returnBuilder.toString();
        }
        returnBuilder.append(language1);
        returnBuilder.append("\":");
        returnBuilder.append(proportion1.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        returnBuilder.append(",\"");
        returnBuilder.append(language2);
        returnBuilder.append("\":");
        returnBuilder.append(proportion2.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        returnBuilder.append("}");
        return returnBuilder.toString();
    }


}
