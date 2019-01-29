# language-distinguish(语言识别language-detect)
根据输入的语言字符串识别是哪个国家的语言，利用了unicode值，谷歌翻译，百度翻译辅助。

# 项目说明
本项目主要基于shuyo的开源框架(https://github.com/shuyo/language-detection)[https://github.com/shuyo/language-detection]和谷歌翻译的语言检测接口完成。由于网上很多开源框架或软件都只能识别一种语言，，并且不能检测出字符串中某些语言的占比和语言支持种类都比较少，而我近来有个需求需要识别包含不同种类的语言和求出它们之间的占比，因此开发出这个功能jar包。<br/>

特点：<br/>
1. 当前支持同一字符串两种语言的识别，并能求出占比，返回形式：lang1:0.09,lang2:0.91
2. 支持语言种类多，具体支持的语言种类见下表
3. 会自动过滤掉特殊非语言字符，如：邮箱，链接，数字，换行符，多余的空格等
4. 支持自定义语言库

缺点：<br/>
1. 最多只支持两种语言，而且不支持都是字母的多语言识别，例如：法语和英语的混合，这种情况下，会整个字符串进行识别。
2. 如果使用精确识别模式，识别速度上比较慢，如果语言大多是非英语字母，则速度大概是1秒一次。



#  使用说明




# 代码示例

```
import com.kanlon.language.LanguageDistinguish;

/**
 * @author zhangcanlong
 * @description:
 * @since 2019/1/28 11:13
 **/
public class Test {
    public static void main(String[] args) {
        System.out.println(LanguageDistinguish.getLanguageByString("com.cybozu.labs.langdetect.Detector.getProbabilities"));
        System.out.println(LanguageDistinguish.getLanguageByString("尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。"));
    }
}

```