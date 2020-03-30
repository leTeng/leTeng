package com.eteng.scaffolding.component.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字符串转换为时间转换器
 * @FileName String2DateConverter
 * @Author eTeng
 * @Date 2019/12/25 14:15
 * @Description
 */
@Component
public class String2DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String dataStr) {
        if (StrUtil.isBlank(dataStr)) {
            return null;
        }
        if (dataStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return makeConverter(dataStr.trim(), "yyyy-MM-dd");
        }
        if (dataStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return makeConverter(dataStr.trim(), "yyyy-MM-dd HH:mm:ss");
        }
        throw new IllegalArgumentException("时间参数转换失败：" + dataStr);
    }

    private Date makeConverter(String dataStr,String format){
        return DateUtil.parse(dataStr,format);
    }
}
