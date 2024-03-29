# language-distinguish(语言识别language-detect)
根据输入的语言字符串识别是哪个国家的语言，利用了unicode值，谷歌翻译，百度翻译辅助（重复造轮子系列）。

# 项目说明
本项目主要基于shuyo的开源框架 https://github.com/shuyo/language-detection 和谷歌翻译的语言检测接口完成。由于网上很多开源框架或软件都只能识别一种语言，，并且不能检测出字符串中某些语言的占比和语言支持种类都比较少，而我近来有个需求需要识别包含不同种类的语言和求出它们之间的占比，因此开发出这个功能jar包。<br/>

### 特点：<br/>
1. 当前支持同一字符串两种语言的识别，并能求出占比，返回形式：`{"lang1":0.09,"lang2":0.91}`,如果检测不了，则返回`{}`

2. 支持语言种类多，具体支持的语言种类见下表

3. 会自动过滤掉特殊非语言字符，如：邮箱，链接，数字，换行符，多余的空格等

4. 支持自定义语言库

### 缺点：<br/>
1. 最多只支持两种语言，而且不支持都是字母的多语言识别，例如：法语和英语的混合，这种情况下，会整个字符串进行识别。

2. 如果使用精确识别模式，识别速度上比较慢，如果语言大多是非英语字母，则速度大概是1秒一次。



#  使用说明
建议使用maven来构建项目<br/>

## 安装依赖或添加jar包

### 方式一（推荐）

直接使用以下maven依赖<br/>

```

<dependency>
    <groupId>com.github.kanlon</groupId>
    <artifactId>language-distinguish</artifactId>
    <version>1.1.1</version>
</dependency>

```

###  方式二

下载项目目录下的lib包中的所有jar包，将它们加到需要用到的项目中的jar包中。然后就可以创建以下测试类进行测试了。<br/>


## 自定义配置与使用

1. 如果需要自己构造语言库，需要在项目根目录下建一个lang文件夹，并将语言库放到该目录下，否则会默认使用自带的lang语言库。语言库制作参考： https://www.jianshu.com/p/9611a048f970 中的”如何生成Language Profile“ <br/>

2. 然后就可以调用`LanguageDistinguish`类中的静态方法`getLanguageByString(String str,DetectMode mode)`来使用了，会返回语言名称的简写代码，str表示要检测的语言，DetectMode表示识别模式`DetectMode.PRECISION`表示精确的，不过速度慢，`DetectMode.IMPRECISION`表示不太精确的，速度快。


# 代码示例

```java
/**
 * 测试语言识别
 *
 * @author zhangcanlong
 * @since 2019/1/28 11:13
 */
@Slf4j
public class LanguageDistinguishTest {

    /**
     * 测试识别
     */
    @Test
    public void testDistinguish() {
        String lang1 = "com.cybozu.labs.langdetect.Detector.getProbabilities";
        String lang2 = "尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。";
        String lang3 = "com.cybozu.labs.langdetect.Detector.getProbabilities尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。";
        String lang4 = "BTS (방탄소년단) 'Save ME' Official MV\n";
        String lang5 = "\"เพลงอื่นๆในอัลบั้ม\n" + "01 Fly : https://www.youtube.com/watch?v=eaIsRux0EUs\n" + "02 Can't (못하겠어) : https://www.youtube.com/watch?v=eduPd3zejwc\n" + "03 See the light (빛이나) : https://www.youtube.com/watch?v=n9O6ZxIiNfo\n" + "04 Fish : https://www.youtube.com/watch?v=q8UdPXzH2qA\n" + "05 Rewind : https://www.youtube.com/watch?v=J9jfoApJous\n" + "06 Beggin on my knees : https://www.youtube.com/watch?v=TOzU28PLjkM\n" + "07 Something good : https://www.youtube.com/watch?v=dtupeRACACc&feature=youtu.be\n" + "\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + "ห้ามรีอัพโหลดและใช้ในเชิงพาณิชย์ [Do not Re-upload & Not for sale]\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + ":: Credit::\n" + "Hangul : music.naver\n" + "Eng trans : KpopViral.com\n" + "Thai lyrics & Trans  : BAEBOOIrene\n" + "- - - - - - - - - - - - - - - - - - - - -\n" + "แปลผิดพลาดประการใดขออภัยด้วยนะคะ\n" + "กดไลค์และคอมเมนต์เป็นกำลังใจให้คนทำซับมากมาย\"\n";
        String lang6 = "Всем доброго дня! Сегодняшнее видео посвящено автомобилям, которые разгоняется до скорости в 300 и больше! Здесь и Porsche GT2, и Lamborghini Urus и Huracan, Audi R8, Mercedes E63 S, а также Bentley Continental GT! Это уже 3 видео по данной теме, так что если вы не видели первые две подборки, то обязательно посмотрите! Приятного просмотра!\n";
        String lang7 = "1--";
        String lang8 = "";
        log.info("识别1的文本为：{},结果为：{}", lang1, LanguageDistinguish.getLanguageByString(lang1, DetectMode.PRECISION));
        log.info("识别2的文本为：{},结果为：{}", lang2, LanguageDistinguish.getLanguageByString(lang2, DetectMode.PRECISION));
        log.info("识别3的文本为：{},结果为：{}", lang3, LanguageDistinguish.getLanguageByString(lang3, DetectMode.PRECISION));
        log.info("识别4的文本为：{},结果为：{}", lang4, LanguageDistinguish.getLanguageByString(lang4, DetectMode.PRECISION));
        log.info("识别5的文本为：{},结果为：{}", lang5, LanguageDistinguish.getLanguageByString(lang5, DetectMode.PRECISION));
        log.info("识别6的文本为：{},结果为：{}", lang6, LanguageDistinguish.getLanguageByString(lang6, DetectMode.PRECISION));
        //如果检测的字符串不含有特征字符，即识别不了，会返回"{}"
        log.info("识别7的文本为：{},结果为：{}", lang7, LanguageDistinguish.getLanguageByString(lang7, DetectMode.PRECISION));
        //如果检测的字符串为空，或空字符串，则会抛出异常，因此需要自己判空
        try {
            log.info("识别8的文本为：{},结果为：{}", lang8, LanguageDistinguish.getLanguageByString(lang8, DetectMode.PRECISION));
        } catch (Exception e) {
            log.error("这里会抛出空报错！", e);
        }
    }
}
```

运行结果

```
21:30:15,282  INFO LanguageDistinguishTest:30 - 识别1的文本为：com.cybozu.labs.langdetect.Detector.getProbabilities,结果为：{"en":1.00}
21:30:15,287  INFO LanguageDistinguishTest:31 - 识别2的文本为：尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。,结果为：{"zh-cn":1.00}
21:30:15,291  INFO LanguageDistinguishTest:32 - 识别3的文本为：com.cybozu.labs.langdetect.Detector.getProbabilities尽管每种应用都会有所不同，但是本质上都是相似的，需要比较单独个体的相似性。,结果为：{"zh-cn":0.60,"en":0.40}
21:30:15,294  INFO LanguageDistinguishTest:33 - 识别4的文本为：BTS (방탄소년단) 'Save ME' Official MV
,结果为：{"ko":0.20,"en":0.80}
21:30:15,298  INFO LanguageDistinguishTest:34 - 识别5的文本为："เพลงอื่นๆในอัลบั้ม
01 Fly : https://www.youtube.com/watch?v=eaIsRux0EUs
02 Can't (못하겠어) : https://www.youtube.com/watch?v=eduPd3zejwc
03 See the light (빛이나) : https://www.youtube.com/watch?v=n9O6ZxIiNfo
04 Fish : https://www.youtube.com/watch?v=q8UdPXzH2qA
05 Rewind : https://www.youtube.com/watch?v=J9jfoApJous
06 Beggin on my knees : https://www.youtube.com/watch?v=TOzU28PLjkM
07 Something good : https://www.youtube.com/watch?v=dtupeRACACc&feature=youtu.be

- - - - - - - - - - - - - - - - - - - - -
ห้ามรีอัพโหลดและใช้ในเชิงพาณิชย์ [Do not Re-upload & Not for sale]
- - - - - - - - - - - - - - - - - - - - -
:: Credit::
Hangul : music.naver
Eng trans : KpopViral.com
Thai lyrics & Trans  : BAEBOOIrene
- - - - - - - - - - - - - - - - - - - - -
แปลผิดพลาดประการใดขออภัยด้วยนะคะ
กดไลค์และคอมเมนต์เป็นกำลังใจให้คนทำซับมากมาย"
,结果为：{"th":0.43,"en":0.57}
21:30:15,301  INFO LanguageDistinguishTest:35 - 识别6的文本为：Всем доброго дня! Сегодняшнее видео посвящено автомобилям, которые разгоняется до скорости в 300 и больше! Здесь и Porsche GT2, и Lamborghini Urus и Huracan, Audi R8, Mercedes E63 S, а также Bentley Continental GT! Это уже 3 видео по данной теме, так что если вы не видели первые две подборки, то обязательно посмотрите! Приятного просмотра!
,结果为：{"ru":0.77,"en":0.23}
21:30:15,302  INFO LanguageDistinguishTest:37 - 识别7的文本为：1--,结果为：{}
21:30:15,302 ERROR LanguageDistinguishTest:42 - 这里会抛出空报错！
java.lang.IllegalArgumentException: 字符串不能为空
	at com.kanlon.language.LanguageDistinguish.getLanguageByString(LanguageDistinguish.java:35)
```


# 项目功能搭建思路
1. 先用正则除去特殊字符，多空白字符替换为一个空白字符.<br/>

2. 然后以空格划分字符串，将含有非字母的字符串和只含有字母的字符串分别归为一类，然后调用shuyo的开源框架(尝试过其他方式,发现该框架识别率最高)分别识别两类字符串，如果识别不了,再调用谷歌翻译识别,再统计占比，然后返回<br/>

3. 后期还会优化,识别法语,英语,德语,目前准对所给测试字符串的特点,基本能精确识别.<br/>


# 支持的语言

<table border=1px>
    <thead>
        <tr>
            <th>序号</th>
            <th>语言简写</th>
            <th>语言名称</th>
        </tr>
    </thead>
	<tbody>
<tr><td>1</td><td>af</td><td>布尔语(南非荷兰语)</td></tr>
<tr><td>2</td><td>am</td><td>阿姆哈拉语</td></tr>
<tr><td>3</td><td>an</td><td>阿拉贡语</td></tr>
<tr><td>4</td><td>ar</td><td>阿拉伯语</td></tr>
<tr><td>5</td><td>ast</td><td>阿斯图里亚斯语</td></tr>
<tr><td>6</td><td>az</td><td>阿塞拜疆语</td></tr>
<tr><td>7</td><td>be</td><td>白俄罗斯语</td></tr>
<tr><td>8</td><td>bg</td><td>保加利亚语</td></tr>
<tr><td>9</td><td>bn</td><td>孟加拉语</td></tr>
<tr><td>10</td><td>br</td><td>布兰顿语</td></tr>
<tr><td>11</td><td>bs</td><td>波斯尼亚语</td></tr>
<tr><td>12</td><td>ca</td><td>加泰罗尼亚语</td></tr>
<tr><td>13</td><td>ceb</td><td>宿务语</td></tr>
<tr><td>14</td><td>co</td><td>科西嘉语</td></tr>
<tr><td>15</td><td>cs</td><td>捷克语</td></tr>
<tr><td>16</td><td>cy</td><td>威尔士语</td></tr>
<tr><td>17</td><td>da</td><td>丹麦语</td></tr>
<tr><td>18</td><td>de</td><td>德语</td></tr>
<tr><td>19</td><td>el</td><td>希腊语</td></tr>
<tr><td>20</td><td>en</td><td>英语</td></tr>
<tr><td>21</td><td>eo</td><td>世界语</td></tr>
<tr><td>22</td><td>es</td><td>西班牙语</td></tr>
<tr><td>23</td><td>et</td><td>爱沙尼亚语</td></tr>
<tr><td>24</td><td>eu</td><td>巴斯克语</td></tr>
<tr><td>25</td><td>fa</td><td>波斯语</td></tr>
<tr><td>26</td><td>fi</td><td>芬兰语</td></tr>
<tr><td>27</td><td>fr</td><td>法语</td></tr>
<tr><td>28</td><td>fy</td><td>弗里西语</td></tr>
<tr><td>29</td><td>ga</td><td>爱尔兰语</td></tr>
<tr><td>30</td><td>gd</td><td>苏格兰盖尔语</td></tr>
<tr><td>31</td><td>gl</td><td>加利西亚语</td></tr>
<tr><td>32</td><td>gu</td><td>古吉拉特语</td></tr>
<tr><td>33</td><td>ha</td><td>豪萨语</td></tr>
<tr><td>34</td><td>haw</td><td>夏威夷语</td></tr>
<tr><td>35</td><td>he</td><td>希伯来语</td></tr>
<tr><td>36</td><td>hi</td><td>印地语</td></tr>
<tr><td>37</td><td>hmn</td><td>苗语</td></tr>
<tr><td>38</td><td>hr</td><td>克罗地亚语</td></tr>
<tr><td>39</td><td>ht</td><td>海地克里奥尔语</td></tr>
<tr><td>40</td><td>hu</td><td>匈牙利语</td></tr>
<tr><td>41</td><td>hy</td><td>亚美尼亚语</td></tr>
<tr><td>42</td><td>id</td><td>印尼语</td></tr>
<tr><td>43</td><td>ig</td><td>伊博语</td></tr>
<tr><td>44</td><td>is</td><td>冰岛语</td></tr>
<tr><td>45</td><td>it</td><td>意大利语</td></tr>
<tr><td>46</td><td>iw</td><td>希伯来语</td></tr>
<tr><td>47</td><td>ja</td><td>日语</td></tr>
<tr><td>48</td><td>jw</td><td>印尼爪哇语</td></tr>
<tr><td>49</td><td>ka</td><td>格鲁吉亚语</td></tr>
<tr><td>50</td><td>kk</td><td>哈萨克语</td></tr>
<tr><td>51</td><td>km</td><td>高棉语</td></tr>
<tr><td>52</td><td>kn</td><td>卡纳达语</td></tr>
<tr><td>53</td><td>ko</td><td>韩语</td></tr>
<tr><td>54</td><td>ku</td><td>库尔德语</td></tr>
<tr><td>55</td><td>ky</td><td>吉尔吉斯语</td></tr>
<tr><td>56</td><td>la</td><td>拉丁语</td></tr>
<tr><td>57</td><td>lb</td><td>卢森堡语</td></tr>
<tr><td>58</td><td>lo</td><td>老挝语</td></tr>
<tr><td>59</td><td>lt</td><td>立陶宛语</td></tr>
<tr><td>60</td><td>lv</td><td>拉脱维亚语</td></tr>
<tr><td>61</td><td>mg</td><td>马尔加什语</td></tr>
<tr><td>62</td><td>mi</td><td>毛利语</td></tr>
<tr><td>63</td><td>mk</td><td>马其顿语</td></tr>
<tr><td>64</td><td>ml</td><td>马拉雅拉姆语</td></tr>
<tr><td>65</td><td>mn</td><td>蒙古语</td></tr>
<tr><td>66</td><td>mr</td><td>马拉地语</td></tr>
<tr><td>67</td><td>ms</td><td>马来语</td></tr>
<tr><td>68</td><td>mt</td><td>马耳他语</td></tr>
<tr><td>69</td><td>my</td><td>缅甸语</td></tr>
<tr><td>70</td><td>ne</td><td>尼泊尔语</td></tr>
<tr><td>71</td><td>nl</td><td>荷兰语</td></tr>
<tr><td>72</td><td>no</td><td>挪威语</td></tr>
<tr><td>73</td><td>ny</td><td>齐切瓦语</td></tr>
<tr><td>74</td><td>oc</td><td>奥克语</td></tr>
<tr><td>75</td><td>pa</td><td>旁遮普语</td></tr>
<tr><td>76</td><td>pl</td><td>波兰语</td></tr>
<tr><td>77</td><td>ps</td><td>普什图语</td></tr>
<tr><td>78</td><td>pt</td><td>葡萄牙语</td></tr>
<tr><td>79</td><td>ro</td><td>罗马尼亚语</td></tr>
<tr><td>80</td><td>ru</td><td>俄语</td></tr>
<tr><td>81</td><td>sd</td><td>信德语</td></tr>
<tr><td>82</td><td>si</td><td>僧伽罗语</td></tr>
<tr><td>83</td><td>sk</td><td>斯洛伐克语</td></tr>
<tr><td>84</td><td>sl</td><td>斯洛文尼亚语</td></tr>
<tr><td>85</td><td>sm</td><td>萨摩亚语</td></tr>
<tr><td>86</td><td>sn</td><td>修纳语</td></tr>
<tr><td>87</td><td>so</td><td>索马里语</td></tr>
<tr><td>88</td><td>sq</td><td>阿尔巴尼亚语</td></tr>
<tr><td>89</td><td>sr</td><td>塞尔维亚语</td></tr>
<tr><td>90</td><td>st</td><td>塞索托语</td></tr>
<tr><td>91</td><td>su</td><td>印尼巽他语</td></tr>
<tr><td>92</td><td>sv</td><td>瑞典语</td></tr>
<tr><td>93</td><td>sw</td><td>斯瓦希里语</td></tr>
<tr><td>94</td><td>ta</td><td>泰米尔语</td></tr>
<tr><td>95</td><td>te</td><td>泰卢固语</td></tr>
<tr><td>96</td><td>tg</td><td>塔吉克语</td></tr>
<tr><td>97</td><td>th</td><td>泰语</td></tr>
<tr><td>98</td><td>tl</td><td>菲律宾语</td></tr>
<tr><td>99</td><td>tr</td><td>土耳其语</td></tr>
<tr><td>100</td><td>uk</td><td>乌克兰语</td></tr>
<tr><td>101</td><td>ur</td><td>乌尔都语</td></tr>
<tr><td>102</td><td>uz</td><td>乌兹别克语</td></tr>
<tr><td>103</td><td>vi</td><td>越南语</td></tr>
<tr><td>104</td><td>wa</td><td>瓦隆语</td></tr>
<tr><td>105</td><td>xh</td><td>南非科萨语</td></tr>
<tr><td>106</td><td>yi</td><td>意第绪语</td></tr>
<tr><td>107</td><td>yo</td><td>约鲁巴语</td></tr>
<tr><td>108</td><td>zh-cn</td><td>简体中文</td></tr>
<tr><td>109</td><td>zh-tw</td><td>繁体中文</td></tr>
<tr><td>110</td><td>zu</td><td>南非祖鲁语</td></tr>
	</tbody>
</table>


