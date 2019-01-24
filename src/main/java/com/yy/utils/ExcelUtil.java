package com.yy.utils;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.StringUtils;
import com.yy.entity.ExcelTestDataModel;
import com.yy.entity.TranslateType;
import com.yy.entity.UnicodeExcelInfo;
import com.yy.language.LanguageDistinguish;
import com.yy.utils.baidu.TranslateLanguageListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
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
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private ExcelUtil() {
    }

    /**
     * 能识别的语言及其对应的unicode码的excel文件的路径
     **/
    private final static String LANGUAGE_UNICODE_INFO = PropUtil.INSTANCE.getStringByKey("LANGUAGE_UNICODE_INFO");
    /**
     * 存放特殊字符unicode码及其对应语言名称的excel文件地址
     **/
    private final static String SPECIAL_LANGUAGE_UNICODE_INFO = PropUtil.INSTANCE.getStringByKey("SPECIAL_LANGUAGE_UNICODE_INFO");
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
     * 存放特殊字符语言对应的unicode数组(已经有序)和语言名称数组(unicodes=int[],languages=String[]),不能直接调用，要通过下面方法从excel中获取
     **/
    private static Map<String, Object> SPECIAL_LANGUAGE_MAP = null;
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


    public static void main(String[] agrs) {
        writeTestDataToExcel(TxtUtil.getTestJson());
    }

    /**
     * 将测试数据写入到excel中去
     *
     * @param jsonObjects 测试数据的json对象
     **/
    public static void writeTestDataToExcel(List<JSONObject> jsonObjects) {
        if (jsonObjects == null) {
            return;
        }
        OutputStream outputStream = getOutputStream(PropUtil.INSTANCE.getStringByKey("TEST_DATA_EXCEL"));
        ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 0, ExcelTestDataModel.class);
        List<ExcelTestDataModel> list = new ArrayList<>();
        int count = 0;
        for (JSONObject object : jsonObjects) {
            ExcelTestDataModel model = new ExcelTestDataModel();
            model.setNum(++count);
            try {
                model.setId(object.getString("id"));
                String tags = "";
                String description = "";
                String title = "";
                if (!object.isNull("tags")) {
                    tags = StringUtil.removeSpecialChar(object.getString("tags"));
                }
                if (!object.isNull("description")) {
                    description = StringUtil.removeSpecialChar(object.getString("description"));
                }
                if (object.isNull("title")) {
                    title = StringUtil.removeSpecialChar(object.getString("title"));
                }
                model.setDescription(description);
                model.setTags(tags);
                model.setTitle(title);
                String allStr = description + title + tags;
                if (!StringUtils.isEmpty(allStr) && !allStr.matches("\\s+")) {
                    model.setLanguage(LanguageDistinguish.getLanguageByString(description + title + tags));
                }
                list.add(model);
            } catch (JSONException e) {
                logger.error("值获取失败,次数" + (count));
            }
        }
        writer.write(list, sheet1);
        writer.finish();
    }


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
     * @return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    public static Map<String, Object> getLanguageAndUnicodeFromExcel() {
        if (LANGUAGE_MAP != null) {
            return LANGUAGE_MAP;
        }
        LANGUAGE_MAP = new HashMap<>(2);
        //读取excel
        List<UnicodeExcelInfo> excelInfos = getExcelInfo(LANGUAGE_UNICODE_INFO);
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
     * 从excel中得到特殊字符对应的unicode数组和语言名称数组(（只会获取一次，如果已经获取过一次，则不会再获取） 从excel中得到语言对应的unicode数组和语言名称数组(（只会获取一次，如果已经获取过一次，则不会再获取）
     *
     * @return java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>
     **/
    public static Map<String, Object> getSpecialLanguageAndUnicodeFromFromExcel() {
        if (SPECIAL_LANGUAGE_MAP != null) {
            return SPECIAL_LANGUAGE_MAP;
        }
        SPECIAL_LANGUAGE_MAP = new HashMap<>(2);
        //读取excel
        List<UnicodeExcelInfo> excelInfos = getExcelInfo(SPECIAL_LANGUAGE_UNICODE_INFO);
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
        SPECIAL_LANGUAGE_MAP.put(UNICODES_KEY, unicodes);
        SPECIAL_LANGUAGE_MAP.put(LANGUAGES_KEY, languages);
        return SPECIAL_LANGUAGE_MAP;
    }

    /**
     * 读取excel得到excel的unicode和对应语言的
     *
     * @param languageTypeFile 是得到特殊字符的，还是普通字符的
     * @return excel的信息
     */
    private static List<UnicodeExcelInfo> getExcelInfo(String languageTypeFile) {
        InputStream inputStream = null;
        List<UnicodeExcelInfo> list;
        try {
            inputStream = new FileInputStream(languageTypeFile);
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

    /**
     * 根据文件路径得到输入流
     *
     * @param filePath 文件路径
     * @return java.io.InputStream
     **/
    public static InputStream getInputStream(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 根据文件路径得到输出流
     *
     * @param filePath 文件路径
     * @return java.io.OutputStream
     **/
    public static OutputStream getOutputStream(String filePath) {
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }


}
