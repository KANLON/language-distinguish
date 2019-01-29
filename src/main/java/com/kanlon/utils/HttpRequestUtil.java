package com.kanlon.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http请求工具类
 *
 * @author zhangcanlong
 * @since 2019-1-18
 */
public class HttpRequestUtil {

    private HttpRequestUtil() {
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是{@code name1=value1&name2=value2}的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws Exception 发送时的异常
     */
    public static String sendGet(String url, String param) throws Exception {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            connection.setRequestProperty("cookie", "CONSENT=YES+UA.zh-CN+; _ga=GA1.3.590596956.1547798349; _gid=GA1.3.338048566.1547798349; SID=-AY6s8EAA4ECY6gnD5FgfymzzmUuRPPSnH0JthZ4o6i-WUUn2IBI7_k1tB-4Nj9P0Ua6oA.; HSID=AeEug-Frif7zZU7C_; SSID=Az44CcdvA1lQXDK6x; APISID=9SAFCmKlNLEQ1ZeN/AUa2igDbEbk4J4y0d; SAPISID=dNyDViOMUWxgl46t/A6eFtpS9r4MCubkh8; 1P_JAR=2019-1-19-4; NID=156=G19wCXlmqdGV_6R4lEMMJeJFn3t1I3s2-oxLAGdY02T4LWbCIRw9qbwOYviOafvbwzFMZAv-8QWxwUf96GwbNugG1exSjSBtUxcBHH5r7snNxYzqCHdDdR0lQWAnx2CaItUv11_bZguU2Hk2ZtHg8PJT-tAOJhLNgBIaMJHels9ApXXzgYpDMLzYioRgh8HcnvWrmMVSIGyNpa5-GQsyoG-6x2rpp7ildO81zAzEzn5aH18; SIDCC=ABtHo-HoR72CadSNhl6QHho8TqYVXd-lthiIrTCAeVPV5e78RvjJvE0zzivp1YoquosLAlZY");
            // 建立实际的连接
            connection.connect();

            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("发送GET请求出现异常！" + e.getMessage());
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

}
