package com.yy.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * unicode和对应语言的编码的excel信息类
 *
 * @author zhangcanlong
 * @date 2019-1-17
 */
@Data
public class UnicodeExcelInfo extends BaseRowModel {
    @ExcelProperty(index = 0, value = "value")
    public int num;

    @ExcelProperty(index = 1, value = "编码范围")
    public String unicodeRange;

    @ExcelProperty(index = 2, value = "中文名称")
    public String languageChineseName;

    @ExcelProperty(index = 3, value = "英文名称")
    public String languageEnglishName;

}
