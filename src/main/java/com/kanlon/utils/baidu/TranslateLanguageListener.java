package com.kanlon.utils.baidu;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangcanlong
 * @description: 得到百度或谷歌翻译所能识别的语言及其代码
 * @since 2019/1/20 14:27
 **/
public class TranslateLanguageListener extends AnalysisEventListener<List<String>> {
    private Map<String, String> map = new HashMap<>();

    @Override
    public void invoke(List<String> list, AnalysisContext context) {
        map.put(list.get(0), list.get(1));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    public Map<String, String> getMap() {
        return map;
    }
}
