package com.example.demo.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.util.ExcelValid;
import lombok.Data;

import java.util.Date;

@Data
@TableName("excel")
public class Excel extends ImportCommon{

    @TableId(type = IdType.ASSIGN_UUID)
    @ExcelIgnore
    private String id;

    @ExcelProperty(value = "测试1")
    @ExcelValid(filedName = "测试1", format = "phone",singleUnique = "测试1",unique = true)
    private String test1;

    @ExcelProperty(value = "测试2")
    @ExcelValid(filedName = "测试2", format = "email",unique = true)
    private String test2;

    @ExcelProperty(value = "测试3")
    @ExcelValid(filedName = "测试3", dateFormat = "yyyy-MM-dd")
    private String test3;

}
