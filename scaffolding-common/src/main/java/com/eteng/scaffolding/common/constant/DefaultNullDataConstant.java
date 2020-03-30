package com.eteng.scaffolding.common.constant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 空值默认值常亮
 * @FileName DefaultNullDataConstant
 * @Author eTeng
 * @Date 2020/3/24 16:08
 * @Description
 */
public class DefaultNullDataConstant {

    public final static String DEFALUT_STRING = "NA";
    public final static String DEFALUT_TEXT = "NA";

    public final static Integer DEFALUT_INTEGER = -1;
    public final static Short DEFALUT_SHORT = -1;
    public final static Long DEFALUT_LONG = -1L;
    public final static Float DEFALUT_FLOAT = -1F;
    public final static Double DEFALUT_DOUBLE = -1D;

    public final static Date DEFALUT_CURRENT_DATE = new Date();
    public final static Date DEFALUT_DATE;

    static {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime of = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        ZonedDateTime zonedDateTime = of.atZone(zoneId);
        DEFALUT_DATE = Date.from(zonedDateTime.toInstant());
    }
}
