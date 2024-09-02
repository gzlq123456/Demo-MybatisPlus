package com.example.demo.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class ImportCommon {

    @ExcelIgnore
    protected String flag;

    @TableField(select = false)
    @ExcelIgnore
    protected Integer rowIndex;

    @ExcelProperty("错误信息")
    protected String err;
}
