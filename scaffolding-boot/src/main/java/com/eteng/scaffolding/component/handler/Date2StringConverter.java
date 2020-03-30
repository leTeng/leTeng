package com.eteng.scaffolding.component.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @FileName Date2StringConverter
 * @Author eTeng
 * @Date 2019/12/25 17:40
 * @Description
 */
@Component
public class Date2StringConverter implements Converter<Date,String> {
    @Override
    public String convert(Date date) {
        if(ObjectUtil.isNotNull(date)){
            return DateUtil.format(date, DTFormatConstant.DATE_TIME);
        }
        return null;
    }
}
