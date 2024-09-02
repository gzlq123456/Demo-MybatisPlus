package com.example.demo.controller;

import com.example.demo.model.Excel;
import com.example.demo.model.ImportCommon;
import com.example.demo.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * excel导入导出样例
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExcelController {

    private final ExcelService excelService;

    /**
     * 导入excel
     *
     * @param file
     * @return
     */
    @PostMapping(value = "importExcel", headers = "content-type=multipart/form-data")
    public ResponseEntity importExcel(@RequestPart("file") MultipartFile file) throws IOException {
        ResponseEntity<List<Excel>> ok = ResponseEntity.ok(excelService.importExcel(file));
        return ok;
    }

    /**
     * 导出excel
     * @param importCommon
     */
    @PostMapping("exportExcel")
    public void exportExcel(HttpServletResponse response, @RequestBody ImportCommon importCommon) throws IOException {
        excelService.exportExcel(importCommon.getFlag(),response);
    }
}
