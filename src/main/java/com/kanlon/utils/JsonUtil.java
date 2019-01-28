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
    }

    /**
     * @param keyType json中对应的对象
     * @return java.util.HashMap<java.lang.String       ,       java.lang.String>
     * @description 根据json中对象的名称返回其内容的hashmap
     **/
    public Map<String, Object> getLanguageInfoByKey(JsonKeyType keyType) {
        return jsonObject.getJSONObject(keyType.toString()).toMap();
    }

    /**
     * @return List<String>
     * @description 根据json中对象的名称返回其内容的list
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

    public static JsonUtil getInstance() {
        return INSTANCE;
    }
}
