package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;

import com.example.demo.model.ImportCommon;
import com.example.demo.util.ExcelImportValid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class ExcelListener<T extends ImportCommon> extends AnalysisEventListener<T> {

    /**
     * 自定义用于暂时存储data
     * 可以通过实例获取该值
     */
    private List<T> datas = new ArrayList<>();

    private ExcelImportValid<T> callback;

    public ExcelListener(ExcelImportValid<T> callback) {
        this.callback = callback;
    }

    /**
     * 每解析一行都会回调invoke()方法
     * @param data  读取后的数据对象
     * @param context 内容
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        // 校验
        String valid = callback.valid(data);
        // String single = callback.single(data);
        data.setRowIndex(context.getCurrentRowNum());
        data.setErr(valid);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("解析到一条数据:{}", objectMapper.writer().writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        datas.add(data);
    }

    /**
     * 读取完后操作
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据读取完成");
    }

    /**
     * 异常方法 (类型转换异常也会执行此方法)  （读取一行抛出异常也会执行此方法)
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.info("有异常");
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
            throw new RuntimeException("第"+excelDataConvertException.getRowIndex()+"行" +
                    "，第" + (excelDataConvertException.getColumnIndex() + 1) + "列读取错误");
        }
    }

    /**
     * 返回数据
     * @return 返回读取的数据集合
     **/
    public List<T> getDatas() {
        return datas;
    }
}