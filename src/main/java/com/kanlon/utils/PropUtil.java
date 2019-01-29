package com.kanlon.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 操作配置资源的工具类
 * @author zhangcanlong
 * @since 2019/1/19 18:33
 **/
public enum PropUtil {
    /**
     * 实例化
     **/
    INSTANCE;
    private Properties prop = new Properties();
    private Logger logger = LoggerFactory.getLogger(PropUtil.class);

    PropUtil() {
        try {
            logger.info("配置文件路径：" + PropUtil.class.getClassLoader().getResource("translation-info.properties").getPath());
            prop.load(new InputStreamReader(PropUtil.class.getClassLoader().getResourceAsStream("translation-info.properties")));
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
