package com.example.demo.util;

import com.alibaba.excel.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelImportValid<T> {

    private Set<String> uniqueSet = new HashSet<>();

    private Map<String, String> uniqueMap = new HashMap<>();
    private Map<String, Set<String>> uniqueMaps = new HashMap<>();

    private String singleValue;

    private final String regexPhone = "^(?:(?:\\+|00)86)?1\\d{10}$";

    private final String regexEmail = "^([A-Za-z0-9_\\-\\.\\u4e00-\\u9fa5])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,8})$";

    /**
     * Excel导入字段校验
     *
     * @param data 校验的JavaBean 其属性须有自定义注解
     */
    public String valid(T data) {
        // 获取所有字段
        Field[] fields = FieldUtils.getAllFields(data.getClass());
        StringBuilder keyBuilder = new StringBuilder();

        String message = null;

        for (Field field : fields) {
            // 设置可访问
            field.setAccessible(true);
            // 属性的值
            Object fieldValue = null;
            try {
                fieldValue = field.get(data);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("导入参数检查失败");
            }
            // 是否包含必填校验注解
            // boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
            ExcelValid excelValid = field.getAnnotation(ExcelValid.class);

            if (excelValid != null) {
                // 校验不能为空
                if (Objects.isNull(fieldValue)) {
                    if (excelValid.notEmpty()) {
                        message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】不能为空；";
                    }
                } else {
                    // 校验重复数据
                    if (excelValid.unique()) {
                        String fieldName = excelValid.filedName();
                        keyBuilder.append(fieldName).append(fieldValue);
                    }

                    // 校验单个重复数据
                    if (excelValid.singleUnique().equals(excelValid.filedName())) {
                        String value = Optional.ofNullable(uniqueMap.get(excelValid.filedName())).orElse("") + fieldValue + ",";
                        uniqueMap.put(excelValid.singleUnique(), value);
                    }

                    // 校验日期格式
                    if (excelValid.dateFormat().length() > 0){
                        boolean isMatch = isDate(fieldValue.toString(), excelValid.filedName());
                        if (!isMatch) {
                            message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】日期格式不正确；";
                        }
                    }

                    // 校验格式
                    String formatName = excelValid.format();
                    if (StringUtils.isNotBlank(formatName)){
                        if (formatName.equals("phone")){
                            // 校验手机格式
                            if (fieldValue.toString().length() != 11) {
                                message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】手机号应该为11位数；";
                            } else {
                                boolean isMatch = isValid(fieldValue.toString(), regexPhone);
                                if (!isMatch) {
                                    message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】手机号格式不正确；";
                                }
                            }
                        }else if (formatName.equals("email")){
                            // 校验邮箱格式
                            boolean isMatch = isValid(fieldValue.toString(), regexEmail);
                            if (!isMatch) {
                                message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】邮箱格式不正确；";
                            }
                        }else if (formatName.equals("number")){
                            // 校验数值格式
                            boolean isMatch = NumberUtils.isCreatable(fieldValue.toString());
                            if (!isMatch) {
                                message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】数值格式不正确；";
                            }
                        }else {
                            // 校验正则格式
                            boolean isMatch = isValid(fieldValue.toString(), formatName);
                            if (!isMatch) {
                                message = Optional.ofNullable(message).orElse("") + "【" + excelValid.filedName() + "】格式不正确；";
                            }
                        }
                    }

                }

            }

        }

        // 校验重复数据 检查元素是否已经存在于uniqueSet中，如果不存在添加元素，存在记录错误
        if (StringUtils.isNotBlank(keyBuilder.toString()) && !uniqueSet.add(keyBuilder.toString())) {
            message = Optional.ofNullable(message).orElse("") +"【"+ keyBuilder +"】重复行；";
        }

        // 校验单个数据是否重复
        Set<String> map = uniqueMap.keySet();
        for (Iterator iter = map.iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            String value = uniqueMap.get(key).toString();

            List<String> split = Arrays.asList(value.split(","));
            String endSplit = split.get(split.size() - 1);
            for(int i = 0 ; i < split.size()-1 ; i++){
                if(split.get(i).equals(endSplit)){
                    message = Optional.ofNullable(message).orElse("") + "【"+ key +"】"+ endSplit +"不能重复；";
                    break;
                }
            }
        }

        return message;
    }

    // 校验 格式是否正确
    public static boolean isValid(String value, String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(value);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    /**
     * 校验字符串是不是日期格式
     * @param date
     * @return
     */
    public static Boolean isDate(String date,String format){
        try {
            DateUtils.parseDate(date,format);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
