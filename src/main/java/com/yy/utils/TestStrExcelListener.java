package com.yy.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangcanlong
 * @description: 获取excel中测试字符串处理类
 * @date 2019/1/18 15:08
 **/
@Getter
@Setter
public class TestStrExcelListener extends AnalysisEventListener<ArrayList<String>> {
    private List<String> list = new ArrayList<>();

    @Override
    public void invoke(ArrayList<String> strs, AnalysisContext context) {
        //System.out.println("当前行："+context.getCurrentRowNum());
        // System.out.println(strs.get(1));
        list.add(strs.get(1));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
