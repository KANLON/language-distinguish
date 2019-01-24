package com.yy.utils.baidu;

import com.yy.entity.TranslateType;
import com.yy.utils.ExcelUtil;
import com.yy.utils.PropUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author zhangcanlong
 * @description: 调用百度翻译API识别语言类别
 * @since 2019/1/24 15:56
 **/
public class BaiduLanguageTest {
    private static Logger logger = LoggerFactory.getLogger(BaiduLanguageTest.class);

    public static void main(String[] args) {
        //System.out.println(getLanguageFromBaidu("张三三liadfda"));
        System.out.println("unicode值：" + 12388);
        System.out.println("字符值：" + (char) (12388));
    }

    /**
     * 根据字符串调用百度翻译功能识别语言类别（每月200万字符免费，超过之后就要收费，无流量限制，识别语言少）
     *
     * @param str 要识别的字符串
     * @return java.lang.String 返回识别得到的语言，如果识别不了返回null
     **/
    private static String getLanguageFromBaidu(String str) {
        logger.info("使用了百度翻译");
        String language = null;
        TransApi api = new TransApi(PropUtil.INSTANCE.getStringByKey("APP_ID"), PropUtil.INSTANCE.getStringByKey("SECURITY_KEY"));
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
        } else {
            String languageCode = map.get(languageKey).toString();
            language = ExcelUtil.getTranslateLanguageAndCodeFromExcel(TranslateType.BAIDU).get(languageCode);
        }
        return language;
    }

}
