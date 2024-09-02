package com.example.demo.util;

import com.alibaba.excel.EasyExcel;
import com.example.demo.listener.ExcelListener;
import com.example.demo.model.ImportCommon;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class EasyExcelUtil<T> {

    // 导出excel
    public static <T> void exportExcel(HttpServletResponse response, String fileName, String sheetName, Class clazz, List<T> data) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel");
        // 导出excel
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(data);
    }

    // 导入excel
    public static <T extends ImportCommon> List<T> importExcel(MultipartFile file, Class clazz) throws IOException {
        ExcelImportValid<T> callback = new ExcelImportValid<>();
        ExcelListener<T> excelListener = new ExcelListener<>(callback);
        EasyExcel.read(file.getInputStream(), clazz, excelListener).sheet().doRead();
        List<T> datas = excelListener.getDatas();
        return datas;
    }
}
