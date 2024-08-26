package com.dingzhuo.energy.project.common;

import cn.hutool.core.date.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Description: 时间工具类
 * @author: yxw
 * @date: 2022年02月02日 12:23
 */
public class DateTimeUtil {
    /**
     * 日期常用格式
     */
    public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 日期常用格式 - 年份
     */
    public static final String COMMON_PATTERN_YEAR = "yyyy";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_MONTH = "yyyyMM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH = "yyyy-MM";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_DAY = "yyyyMMdd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY = "yyyy-MM-dd";
    /**
     * 日期常用格式 - 天某一天,
     */
    public static final String COMMON_PATTERN_DAY_OF_MONTH = "dd";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_HOUR = "yyyyMMddHH";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR = "yyyy-MM-dd HH";

    /**
     * 获取当前时间,时间格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowDateTime() {
        return getNowDateTime(COMMON_PATTERN);
    }

    /**
     * 获取当前时间
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getNowDateTime(String pattern) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        String dateTime = df.format(new Date());
        return dateTime;
    }

    /**
     * 获取今年的年份值
     *
     * @return
     */
    public static String getNowYear() {
        return getNowDateTime(COMMON_PATTERN_YEAR);
    }

    /**
     * 获取今年的月份值
     *
     * @return
     */
    public static String getNowMonth() {
        return getNowDateTime(COMMON_PATTERN_MONTH);
    }

    /**
     * 字符串转成时间类型,默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTimeStr
     * @return
     */
    public static Date toDateTime(String dateTimeStr) {
        DateTime dt = null;
        try {
            dt = DateTime.of(dateTimeStr, COMMON_PATTERN);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 字符串转成时间类型
     *
     * @param dateTimeStr
     * @return
     */
    public static Date toDateTime(String dateTimeStr, String pattern) {
        DateTime dt = null;
        try {
            dt = DateTime.of(dateTimeStr, pattern);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 字符串转成特定格式的时间字符串类型
     *
     * @param dateTimeStr   时间字符串
     * @param sourcePattern 字符串时间格式
     * @param toPattern     要转成什么格式的时间
     * @return
     */
    public static String toDateTimeStr(String dateTimeStr, String sourcePattern, String toPattern) {
        String str = CommonConst.EMPTY;
        try {
            DateTime dt = DateTime.of(dateTimeStr, sourcePattern);
            str = getDateTime(dt, toPattern);
        } catch (Exception e) {

        }
        return str;
    }

    /**
     * 时间转成指定格式的字符串
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getDateTime(Date dt, String pattern) {
        if (dt == null) {
            return CommonConst.EMPTY;
        }
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(dt);
    }

    /**
     * 根据字符串以及时间类型获取时间值
     *
     * @param dateTime
     * @param timeType
     * @return
     */
    public static String getDateTimeStrWithTimeType(Date dateTime, String timeType) {
        String str = CommonConst.EMPTY;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                str = getDateTime(dateTime, COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                str = getDateTime(dateTime, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                str = getDateTime(dateTime, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                str = getDateTime(dateTime, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 根据字符串以及时间格式获取时间值
     *
     * @param dateTimeStr
     * @param timeType
     * @return
     */
    public static Date getDateTimeWithTimeType(String dateTimeStr, String timeType) {
        Date dt = null;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = toDateTime(dateTimeStr, COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = toDateTime(dateTimeStr, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = toDateTime(dateTimeStr, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = toDateTime(dateTimeStr, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 根据字符串以及时间格类型获取时间值
     *
     * @param dateTime
     * @param timeType
     * @return
     */
    public static Date getEndTimeWithTimeType(Date dateTime, String timeType) {
        Date dt = null;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = addSeconds(addHours(dateTime, CommonConst.DIGIT_1), CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = addSeconds(addDays(dateTime, CommonConst.DIGIT_1), CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = addSeconds(addMonths(dateTime, CommonConst.DIGIT_1), CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = addSeconds(addYears(dateTime, CommonConst.DIGIT_1), CommonConst.DIGIT_MINUS_1);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 获取当前时间所属月份的最后一天
     *
     * @return
     */
    public static int getDateTimeLastDay(Date dt) {
        String month = getMonth(dt);
        String firstDate = month + "01";
        Date nextMonthFirstDate = addMonths(toDateTime(firstDate, COMMON_PATTERN_DAY), CommonConst.DIGIT_1);
        Date currentMonthLastDate = addDays(nextMonthFirstDate, CommonConst.DIGIT_MINUS_1);
        int day = IntegerUtil.toInt(getDateTime(currentMonthLastDate, COMMON_PATTERN_DAY_OF_MONTH));
        return day;
    }

    /**
     * 获取年份值
     *
     * @return
     */
    public static String getYear(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_YEAR);
    }

    /**
     * 获取月份值 202202
     *
     * @return
     */
    public static String getMonth(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_MONTH);
    }

    /**
     * 获取天
     *
     * @return
     */
    public static String getDay(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_DAY);
    }

    /**
     * 获取小时
     *
     * @return
     */
    public static String getHour(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_HOUR);
    }

    /**
     * 转成字符串类型值
     *
     * @return
     */
    public static String toString(Date dt) {
        if (dt == null) {
            return CommonConst.EMPTY;
        }
        return getDateTime(dt, COMMON_PATTERN);
    }

    /**
     * 时间增加对应的年数
     *
     * @param dateTime
     * @param years
     * @return
     */
    public static Date addYears(Date dateTime, int years) {
        return calcDate(dateTime, years, Calendar.YEAR);
    }

    /**
     * 时间增加对应的月数
     *
     * @param dateTime
     * @param months
     * @return
     */
    public static Date addMonths(Date dateTime, int months) {
        return calcDate(dateTime, months, Calendar.MONTH);
    }

    /**
     * 时间增加对应的天数
     *
     * @param dateTime
     * @param days
     * @return
     */
    public static Date addDays(Date dateTime, int days) {
        return calcDate(dateTime, days, Calendar.DATE);
    }

    /**
     * 时间增加对应的小时数
     *
     * @param dateTime
     * @param hours
     * @return
     */
    public static Date addHours(Date dateTime, int hours) {
        return calcDate(dateTime, hours, Calendar.HOUR);
    }

    /**
     * 时间增加对应的分钟数
     *
     * @param dateTime
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date dateTime, int minutes) {
        return calcDate(dateTime, minutes, Calendar.MINUTE);
    }

    /**
     * 时间增加对应的秒数
     *
     * @param dateTime
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date dateTime, int seconds) {
        return calcDate(dateTime, seconds, Calendar.SECOND);
    }

    /**
     * 计算日期通用方法
     *
     * @param dateTime
     * @param value
     * @param calendarType 计算类型：Calendar.YEAR，Calendar.MONTH,Calendar.DAY
     * @return
     */
    public static Date calcDate(Date dateTime, int value, int calendarType) {
        Date dt = null;
        try {
            if (dateTime == null) {
                return dt;
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);
            //把日期往后增加一年，整数往后推，负数往前移
            calendar.add(calendarType, value);
            // 获取相加或者相减之后的时间值
            Date tempDt = calendar.getTime();
            // 把时间转成所需要的格式
            String temp = getDateTime(tempDt, COMMON_PATTERN);
            dt = toDateTime(temp);
        } catch (Exception e) {

        }
        return dt;
    }
}
