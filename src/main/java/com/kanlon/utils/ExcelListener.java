package com.kanlon.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.kanlon.entity.UnicodeExcelInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * excel读取处理逻辑
 *
 * @author zhangcanlong
 * @Date 20190118
 */
@Getter
@Setter
public class ExcelListener extends AnalysisEventListener<UnicodeExcelInfo> {

    // 自定义用于暂时存储data。
    private List<UnicodeExcelInfo> datas = new ArrayList<>();

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //datas.clear();//解析结束销毁不用的资源
    }

    @Override
    public void invoke(UnicodeExcelInfo excelInfo, AnalysisContext context) {
//		System.out.println("当前行：" + context.getCurrentRowNum());
//		System.out.println(excelInfo);
        datas.add(excelInfo);// 数据存储到list，供批量处理，或后续自己业务逻辑处理。
        //doSomething(object);// 根据自己业务做处理

    }
}