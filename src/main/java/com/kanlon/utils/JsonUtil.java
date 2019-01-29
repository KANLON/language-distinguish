package com.kanlon.utils;

import com.kanlon.entity.JsonKeyType;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * json文件及格式操作工具类
 *
 * @author zhangcanlong
 * @since 2019/1/28 16:43
 **/
public class JsonUtil {

    /**
     * 饿汉式单例
     **/
    private static JsonUtil INSTANCE = new JsonUtil();

    /**
     * 整个json数据的对象
     **/
    private JSONObject jsonObject;

    /**
     * 能够通过unicode判断的语言的unicode集合
     **/
    private List<Integer> ableJudgeUnicodes = new ArrayList<>();
    /**
     * 能够通过unicode判断语言的语言代码集合
     **/
    private List<String> ableJudgeLanguages = new ArrayList<>();

    /**
     * 特殊unicode的字符集，应该除去的字符符号
     **/
    private List<Integer> specialLanguageUnicode = new ArrayList<>();

    private JsonUtil() {
        InputStream inputStream = JsonUtil.class.getClassLoader().getResourceAsStream("language-unicode-info.json");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer jsonStr = new StringBuffer();
        String line = null;
        while (true) {
            try {
                if (((line = in.readLine()) == null)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonStr.append(line);
        }
        jsonObject = new JSONObject(jsonStr.toString());
        //设置能够识别的语言
        JSONObject ableJudgeJson = jsonObject.getJSONObject(JsonKeyType.ABLE_JUDGE_BY_UNICODE.toString());
        for (String key : ableJudgeJson.keySet()) {
            String[] unicodeRange = key.split("-");
            ableJudgeUnicodes.add(Integer.parseInt(unicodeRange[0], 16));
            ableJudgeUnicodes.add(Integer.parseInt(unicodeRange[1], 16));
            ableJudgeLanguages.add(ableJudgeJson.getString(key));
        }
        //排序unicodes，因为查找的时候使用的是类二分法
        Collections.sort(ableJudgeUnicodes);

        //设置特殊unicode的字符，即标点符号，图案之类的
        JSONObject specialJson = jsonObject.getJSONObject(JsonKeyType.SPECIAL_LANGUAGE_UNICODE.toString());
        for (String key : specialJson.keySet()) {
            String[] unicodeRange = key.split("-");
            specialLanguageUnicode.add(Integer.parseInt(unicodeRange[0], 16));
            specialLanguageUnicode.add(Integer.parseInt(unicodeRange[1], 16));
        }
        //排序unicodes，因为查找的时候使用的是类二分法
        Collections.sort(specialLanguageUnicode);
    }

    /**
     * 根据json中对象的名称返回其内容的hashmap
     * @param keyType json中对应的对象
     * @return 对象
     **/
    public Map<String, Object> getLanguageInfoByKey(JsonKeyType keyType) {
        return jsonObject.getJSONObject(keyType.toString()).toMap();
    }

    /**
     * 根据json中对象的名称返回其内容的list
     * @return 测试的字符串集合
     **/
    public List<String> getTestStr() {
        List<String> list = new ArrayList<>();
        List<Object> objects = jsonObject.getJSONArray("test-str").toList();
        for (Object obj : objects) {
            list.add(obj.toString());
        }
        return list;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }


    public Integer[] getAbleJudgeUnicodes() {
        return ableJudgeUnicodes.toArray(new Integer[ableJudgeUnicodes.size()]);
    }

    public String[] getAbleJudgeLanguages() {
        return ableJudgeLanguages.toArray(new String[ableJudgeLanguages.size()]);
    }

    public Integer[] getSpecialLanguageUnicode() {
        return specialLanguageUnicode.toArray(new Integer[specialLanguageUnicode.size()]);
    }

    public static JsonUtil getInstance() {
        return INSTANCE;
    }
}
