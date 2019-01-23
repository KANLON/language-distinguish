package com.yy.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @author zhangcanlong
 * @description 写入测试数据的和excel表的映射类
 * @since 2019-1-23
 **/
@Data
public class ExcelTestDataModel extends BaseRowModel {

    @ExcelProperty(value = "序号", index = 0)
    private Integer num;

    @ExcelProperty(value = "iD", index = 1)
    private String id;

    @ExcelProperty(value = "标题", index = 2)
    private String title;

    @ExcelProperty(value = "描述", index = 3)
    private String description;

    @ExcelProperty(value = "标签", index = 4)
    private String tag;

}