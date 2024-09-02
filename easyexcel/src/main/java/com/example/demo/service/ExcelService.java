package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dao.ExcelDao;
import com.example.demo.model.Excel;
import com.example.demo.util.EasyExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExcelService {

    private final ExcelDao excelDao;

    public List<Excel> importExcel(MultipartFile file) throws IOException {
        // 读取文件
        List<Excel> datas = EasyExcelUtil.importExcel(file, Excel.class);
        String flag = UUID.randomUUID().toString().replaceAll("-", "");
        for (Excel data : datas) {
            data.setFlag(flag);
            excelDao.insert(data);
        }
        return datas;
    }

    public void exportExcel(String flag, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<Excel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Excel::getFlag, flag);
        List<Excel> data = excelDao.selectList(queryWrapper);

        String fileName = "清单导出_" + System.currentTimeMillis() + ".xlsx";
        String sheetName = "sheet1";
        EasyExcelUtil.exportExcel(response, fileName, sheetName, Excel.class, data);
    }
}
