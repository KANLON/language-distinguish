package com.yy.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author zhangcanlong
 * @description: 操作配置资源的工具类
 * @since 2019/1/19 18:33
 **/
public enum PropUtil {
    INSTANCE;
    private Properties prop = new Properties();

    PropUtil() {
        try {
            prop.load(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("./translation-info.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据配置中的key获取字符串值
     *
     * @param key 要获取的key
     **/
    public String getStringByKey(String key) {
        return prop.getProperty(key);
    }

}
