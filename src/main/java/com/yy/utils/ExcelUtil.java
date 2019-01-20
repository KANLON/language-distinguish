package com.yy.utils;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.yy.entity.TranslateType;
import com.yy.entity.UnicodeExcelInfo;
import com.yy.utils.baidu.TranslateLanguageListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作excel的工具类，主要是读取各个语言对应的unicode码
 *
 * @author zhangcanlong
 * @since 2019-1-17
 */
public class ExcelUtil {
    private ExcelUtil() {
    }

    /**
     * 能识别的语言及其对应的unicode码的excel文件的路径
     **/
    private final static String LANGUAGE_UNICODE_INFO = PropUtil.INSTANCE.getStringByKey("LANGUAGE_UNICODE_INFO");
    /**
     * 测试的字符的文件路径
     **/
    private final static String TEST_STR_FILE = PropUtil.INSTANCE.getStringByKey("TEST_STR_FILE");

    /**
     * 百度和谷歌翻译所能识别的语言及其对应代码的文件路径
     **/
    private final static String TRANSLATE_LANGUAGE_CODE_FILE = PropUtil.INSTANCE.getStringByKey("TRANSLATE_LANGUAGE_CODE_FILE");

    /**
     * 存放语言对应的unicode数组(已经有序)和语言名称数组(unicodes=int[],languages=String[]),不能直接调用，要通过下面方法从excel中获取
     **/
    private static Map<String, Object> LANGUAGE_MAP = null;
    /**
     * 百度能识别的语言及其对应代码，语言代码=语言名称,不能直接调用，要通过下面方法从excel中获取
     **/
    private static Map<String, String> baiduLanguageAndCode = null;
    /**
     * 谷歌能识别的语言及其对应代码，语言代码=语言名称,不能直接调用，要通过下面方法从excel中获取
     **/
    private static Map<String, String> googleLanguageAndCode = null;

    /**
     * 存放map中的key字符值
     **/
    public final static String UNICODES_KEY = "unicodes";
    /**
     * 存放map中的key字符值
     **/
    public final static String LANGUAGES_KEY = "languages";
    /**
     * 用于获取谷歌或者百度翻译所支持的语言及其对应代码的excel表名
     **/
    public final static String GOOGLE_TRANSLATE = "谷歌翻译";
    public final static String BAIDU_TRANSLATE = "百度翻译";

    /**
     * 从excel中得到测试字符
     *
     * @return 测试字符串的数组
     **/
    public static List<String> getTestStr() {
        InputStream inputStream = null;
        TestStrExcelListener excelListener = new TestStrExcelListener();
        try {
            inputStream = new FileInputStream(TEST_STR_FILE);
            // 解析每行结果在listener中处理
            ExcelReader excelReader = new ExcelReader(inputStream, null,
                    excelListener);
            excelReader.read(new Sheet(1, 1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return excelListener.getList();
    }

    /**
     * 从excel中得到百度和谷歌翻译能识别的语言及其代码
     *
     * @param translateType 输入"百度翻译"则是获取百度翻译的，输入"谷歌翻译"则是获取谷歌翻译的
     * @return 测试字符串的数组
     **/
    public static Map<String, String> getTranslateLanguageAndCodeFromExcel(Enum translateType) {
        if (baiduLanguageAndCode != null && TranslateType.BAIDU == translateType) {
            return baiduLanguageAndCode;
        } else if (googleLanguageAndCode != null && TranslateType.GOOGLE == translateType) {
            return googleLanguageAndCode;
        }
        InputStream inputStream = null;
        TranslateLanguageListener excelListener = new TranslateLanguageListener();
        try {
            int sheetNo = 1;
            if (TranslateType.BAIDU == translateType) {
                sheetNo = 1;
            } else if (TranslateType.GOOGLE == translateType) {
                sheetNo = 2;
            }
            inputStream = new FileInputStream(TRANSLATE_LANGUAGE_CODE_FILE);
            // 解析每行结果在listener中处理
            ExcelReader excelReader = new ExcelReader(inputStream, null,
                    excelListener);
            excelReader.read(new Sheet(sheetNo, 1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (TranslateType.BAIDU == translateType) {
            baiduLanguageAndCode = excelListener.getMap();
            return baiduLanguageAndCode;
        } else if (TranslateType.GOOGLE == translateType) {
            googleLanguageAndCode = excelListener.getMap();
            return googleLanguageAndCode;
        }
        return null;
    }

    /**
     * 从excel中得到语言对应的unicode数组和语言名称数组(（只会获取一次，如果已经获取过一次，则不会再获取） 从excel中得到语言对应的unicode数组和语言名称数组(（只会获取一次，如果已经获取过一次，则不会再获取）
     *
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     **/
    public static Map<String, Object> getLanguageAndUnicodeFromExcel() {
        if (LANGUAGE_MAP != null) {
            return LANGUAGE_MAP;
        }
        LANGUAGE_MAP = new HashMap<>(2);
        //读取excel
        List<UnicodeExcelInfo> excelInfos = getExcelInfo();
        if (excelInfos == null || excelInfos.size() <= 0) {
            throw new RuntimeException("没有获取到语言及其对应的unicode数据");
        }
        int[] unicodes = new int[excelInfos.size() * 2];
        String[] languages = new String[excelInfos.size()];
        for (int i = 0; i < excelInfos.size(); i++) {
            String[] unicodeRange = excelInfos.get(i).unicodeRange.split("-");
            unicodes[2 * i] = Integer.parseInt(unicodeRange[0], 16);
            unicodes[2 * i + 1] = Integer.parseInt(unicodeRange[1], 16);
            languages[i] = excelInfos.get(i).languageChineseName;
        }
        LANGUAGE_MAP.put(UNICODES_KEY, unicodes);
        LANGUAGE_MAP.put(LANGUAGES_KEY, languages);
        return LANGUAGE_MAP;
    }

    /**
     * 读取excel得到excel的unicode和对应语言的
     *
     * @return excel的信息
     */
    private static List<UnicodeExcelInfo> getExcelInfo() {
        InputStream inputStream = null;
        List<UnicodeExcelInfo> list;
        try {
            inputStream = new FileInputStream(LANGUAGE_UNICODE_INFO);
            // 解析每行结果在listener中处理
            ExcelListener excelListener = new ExcelListener();
            ExcelReader excelReader = new ExcelReader(inputStream, null,
                    excelListener);
            excelReader.read(new Sheet(1, 1, UnicodeExcelInfo.class));
            list = excelListener.getDatas();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
