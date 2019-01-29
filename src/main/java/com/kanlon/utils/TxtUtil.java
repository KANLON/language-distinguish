package com.kanlon.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * txt文件的工具类
 * @author zhangcanlong
 * @since 2019/1/23 10:18
 **/
public class TxtUtil {

    private static Logger logger = LoggerFactory.getLogger(TxtUtil.class);

    /**
     * 爬取的真实测试数据的文件路径
     **/
    private final static String TEST_DATA_FILE = PropUtil.INSTANCE.getStringByKey("TEST_DATA_FILE");


    /**
     * 将txt文件的每行内容转化为json对象
     **/
    @Test
    public static List<JSONObject> getTestJson() {
        //存储文件中每行json对象
        List<JSONArray> list = new ArrayList<>();
        List<JSONObject> objectList = new ArrayList<>();
        //统计解析不了的行数
        int errorParseCount = 0;
        try {
            FileReader file = new FileReader(TEST_DATA_FILE);
            BufferedReader bufferedReader = new BufferedReader(file);
            StringBuffer buffer = new StringBuffer();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str + "\n");
                try {
                    JSONArray array = new JSONArray(str);
                    list.add(array);
                    JSONObject object = new JSONObject(array.get(1).toString());
                    objectList.add(object);
                } catch (JSONException e) {
                    logger.info("解析json对象有问题，第" + (++errorParseCount) + "次");
                }
            }
            for (int i = 0; i < list.size(); i++) {
                logger.info(objectList.get(i).toString());
            }
            logger.info("解析成功：" + list.size() + "行,失败" + errorParseCount + "行");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectList;
    }


}
