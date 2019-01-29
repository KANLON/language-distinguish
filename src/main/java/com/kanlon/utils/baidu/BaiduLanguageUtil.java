package com.kanlon.utils.baidu;

import com.kanlon.utils.StringUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zhangcanlong
 *  调用百度翻译API识别语言类别
 * @since 2019/1/24 15:56
 **/
public class BaiduLanguageUtil {
    private static Logger logger = LoggerFactory.getLogger(BaiduLanguageUtil.class);

    /**
     * 这两个参数需要在百度翻译的调用接口那个页面获取
     **/
    private static String APP_ID = "";
    private static String SECURITY_KEY = "";

    /**
     * 需要设置百度翻译需要的请求的两个参数：http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
     *
     * @param appID 百度接口要求的appID
     * @param securityKey 接口要求的安全密钥
     **/
    public BaiduLanguageUtil(String appID, String securityKey) {
        APP_ID = appID;
        SECURITY_KEY = securityKey;
    }

    /**
     * 根据字符串调用百度翻译功能识别语言类别（每月200万字符免费，超过之后就要收费，无流量限制，识别语言少）
     *
     * @param str 要识别的字符串
     * @return String 返回识别得到的语言的代码，如果识别不了返回null
     **/
    private static String getLanguageFromBaidu(String str) {
        if (StringUtil.isEmptyOrWhiteSpace(APP_ID) || StringUtil.isEmptyOrWhiteSpace(SECURITY_KEY)) {
            throw new RuntimeException("还没设置APP_ID和SECURITY_KEY");
        }
        logger.info("使用了百度翻译");
        String language = null;
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String result = api.getTransResult(str, "auto", "en");
        JSONObject object = new JSONObject(result);
        Map<String, Object> map = object.toMap();
        //获取错误
        final String languageKey = "from";
        final String errorCodeKey = "error_code";
        final String signErrorCode = "54001";
        final String passwordErrorCode = "52003";
        final String tooQuickRequestErrorCode = "54003";
        final String tooMoreRequestErrorCode = "54004";
        if (map.get(languageKey) == null) {
            String errorCode = map.get(errorCodeKey).toString();
            if (signErrorCode.equals(errorCode) || passwordErrorCode.equals(errorCode)) {
                logger.error("签名错误,请检查app-id和密码是否填写正确," + result);
            } else if (tooQuickRequestErrorCode.equals(errorCode)) {
                logger.error("调用频率太高，请降低调用频率," + result);
            } else if (tooMoreRequestErrorCode.equals(errorCode)) {
                logger.error("免费次数已经用完，请充值," + result);
            } else {
                logger.error("其他原因，" + result);
            }
            return language;
        } else {
            String languageCode = map.get(languageKey).toString();
            return languageCode;
        }
    }

}
