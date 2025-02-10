package com.zhitan.common.utils;


import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;

import java.util.Date;
import java.util.List;

/**
 * @Description: 统计图相关数据工具类
 * @author: yxw
 * @date: 2022年04月28日 15:29
 */
public class ChartUtils {

    /**
     * 构造日期列表
     *
     * @param timeType
     * @param timeCode
     * @param dateList
     */
    public static void generateDateList(String timeType, String timeCode, List<Date> dateList) {
        Date now = new Date();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                if (!timeCode.contains(CommonConst.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_DAY, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                }
                int hour = 23;
                for (int i =  CommonConst.DIGIT_0; i <= hour; i++) {
                    String tempCode = timeCode + " 0" + i;
                    if (i > 9) {
                        tempCode = timeCode + " " + i;
                    }
                    Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_HOUR);
                    dateList.add(tempD);
                }
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                if (!timeCode.contains(CommonConst.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_MONTH, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                }
                int max = DateTimeUtil.getDateTimeLastDay(DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH));

                for (int i =  CommonConst.DIGIT_1; i <= max; i++) {
                    String tempCode = timeCode + "-0" + i;
                    if (i > 9) {
                        tempCode = timeCode + "-" + i;
                    }
                    Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                    dateList.add(tempD);
                }
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                int monthMax = 12;
                for (int i = CommonConst.DIGIT_1; i <= monthMax; i++) {
                    String tempCode = timeCode + "-0" + i;
                    if (i > 9) {
                        tempCode = timeCode + "-" + i;
                    }
                    Date tempD = DateTimeUtil.toDateTime(tempCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                    dateList.add(tempD);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取对应的结束时间
     *
     * @param timeType
     * @param date
     * @return
     */
    public static Date getEndTime(String timeType, Date date) {
        Date d1 = new Date();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                d1 = DateTimeUtil.addDays(date, CommonConst.DIGIT_1);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                d1 = DateTimeUtil.addMonths(date, CommonConst.DIGIT_1);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                d1 = DateTimeUtil.addYears(date, CommonConst.DIGIT_1);
                break;
            default:
                break;
        }
        return d1;
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param date
     * @return
     */
    public static String getTimeCode(String timeType, Date date) {
        String str = CommonConst.EMPTY;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param date
     * @return
     */
    public static String getTimeCodeChart(String timeType, Date date) {
        String str = CommonConst.EMPTY;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_HOUR_MINUTE);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_MONTH_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                str = DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 获取日期显示字符
     *
     * @param timeType
     * @param timeCode
     * @return
     */
    public static Date getDateTime(String timeType, String timeCode) {
        Date d1 = new Date();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                if (!timeCode.contains(CommonConst.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_DAY, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                }
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                if (!timeCode.contains(CommonConst.SINGLE_MINUS_SIGN)) {
                    timeCode = DateTimeUtil.toDateTimeStr(timeCode, DateTimeUtil.COMMON_PATTERN_MONTH, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                }
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                d1 = DateTimeUtil.toDateTime(timeCode, DateTimeUtil.COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return d1;
    }
}
