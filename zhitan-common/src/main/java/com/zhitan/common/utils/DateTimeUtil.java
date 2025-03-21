package com.zhitan.common.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 时间工具类
 * @author: yxw
 * @date: 2022年02月02日 12:23
 */
@Slf4j
public class DateTimeUtil {
    /**
     * 日期常用格式
     */
    public static final String COMMON_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化日期到分钟
     */
    public static final String COMMON_PATTERN_END_WITH_MINUTE = "yyyy-MM-dd HH:mm";
    /**
     * 日期格式 - 小时:分钟
     */
    public static final String COMMON_PATTERN_HOUR_MINUTE = "HH:mm";
    /**
     * 日期常用格式 - 年份
     */
    public static final String COMMON_PATTERN_YEAR = "yyyy";
    /**
     * 日期常用格式 - 某一年,
     */
    public static final String COMMON_PATTERN_CERTAIN_YEAR = "yy";

    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_MONTH = "yyyyMM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH = "yyyy-MM";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH_WORD = "yyyy-MM月";
    /**
     * 日期常用格式 - 月份
     */
    public static final String COMMON_PATTERN_TO_MONTH_ZH = "yyyy年MM月";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_DAY = "yyyyMMdd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY = "yyyy-MM-dd";
    /**
     * 日期常用格式 - 天
     */
    public static final String COMMON_PATTERN_TO_DAY_WORD = "yyyy-MM-dd日";
    /**
     * 日期常用格式 - 月日
     */
    public static final String COMMON_PATTERN_MONTH_DAY = "MM-dd";
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
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR_WORD = "yyyy-MM-dd HH时";
    /**
     * 日期常用格式 - 小时
     */
    public static final String COMMON_PATTERN_TO_HOUR_TEXT = "yyyy年MM月dd日 HH时";

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
     * 时间转成指定的格式
     *
     * @param pattern 时间格式
     * @return
     */
    public static String getDateTime(Date dt, String pattern) {
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(dt);
    }

    /**
     * 时间转成yyyy-MM-dd HH:mm:ss格式
     *
     * @return
     */
    public static String getDateTime(Date dt) {
        if (ObjectUtil.isEmpty(dt)) {
            return CommonConst.EMPTY;
        }
        return getDateTime(dt, COMMON_PATTERN);
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
     * 获取天,格式：yyyyMMdd
     *
     * @return
     */
    public static String toDay(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_DAY);
    }

    /**
     * 获取小时:yyyyMMddHH
     *
     * @return
     */
    public static String toHour(Date dt) {
        return getDateTime(dt, COMMON_PATTERN_HOUR);
    }

    /**
     * 转成字符串类型值
     *
     * @return
     */
    public static String toString(Date dt) {
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
     * 时间增加对应的小时数
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
     * @param addValue
     * @param calendarType 计算类型：Calendar.YEAR，Calendar.MONTH,Calendar.DAY
     * @return
     */
    private static Date calcDate(Date dateTime, int addValue, int calendarType) {
        Date dt = null;
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);
            //把日期往后增加一年，整数往后推，负数往前移
            calendar.add(calendarType, addValue);
            // 获取相加或者相减之后的时间值
            Date tempDt = calendar.getTime();
            // 把时间转成所需要的格式
            String temp = getDateTime(tempDt, COMMON_PATTERN);
            dt = toDateTime(temp);
        } catch (Exception e) {

        }
        return dt;
    }

    /**
     * 获取该时间属于当天的第几个小时
     *
     * @param dateTime
     * @return
     */
    public static int getHourOfDay(Date dateTime) {
        return getDateValue(dateTime, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取该时间属于当月的第几天
     *
     * @param dateTime
     * @return
     */
    public static int getDayOfMonth(Date dateTime) {
        return getDateValue(dateTime, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取该时间属于当周的第几天
     * 一周的第一天是周日
     *
     * @param dateTime
     * @return
     */
    public static int getDayOfWeek(Date dateTime) {
        return getDateValue(dateTime, Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取该时间属于年的第几个月
     * 月份值+1是真实的当前月
     *
     * @param dateTime
     * @return 已经在系统中获取值的基础上加1了，现在是真实的当前月份值
     */
    public static int getMonthOfYear(Date dateTime) {
        return getDateValue(dateTime, Calendar.MONTH) + 1;
    }

    /**
     * 获取当天的第几个小时/当月的第几天/当年的第几个月
     *
     * @param dateTime     如果时间值为空，默认当前时间
     * @param calendarType
     * @return
     */
    private static int getDateValue(Date dateTime, int calendarType) {
        int value = 0;
        try {
            if (ObjectUtil.isEmpty(dateTime)) {
                dateTime = new Date();
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dateTime);
            value = calendar.get(calendarType);
        } catch (Exception e) {

        }
        return value;
    }

    /**
     * 对比time1 和 time2 的大小
     *
     * @param time1
     * @param time2
     * @return -1:time1小于time2;0:time1等于time2;1:time1大于time2;
     */
    public static int compareDateDiff(Date time1, Date time2) {
        long diff = time1.getTime() - time2.getTime();
        int res = 0;
        if (diff > 0) {
            res = 1;
        } else if (diff < 0) {
            res = -1;
        }
        return res;
    }

    /**
     * 获取查询data_item所需要的timecode值
     *
     * @param timeType 日期类型
     * @param date     时间
     * @return
     */
    public static String getTimeCode(String timeType, Date date) {
        String timeCode = CommonConst.EMPTY;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                timeCode = CommonConst.WORD_H + getDateTime(date, COMMON_PATTERN_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                timeCode = CommonConst.WORD_D + getDateTime(date, COMMON_PATTERN_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                timeCode = CommonConst.WORD_M + getDateTime(date, COMMON_PATTERN_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                timeCode = CommonConst.WORD_Y + getDateTime(date, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return timeCode;
    }

    /**
     * 获取查询日月年报表所需要的timecode值
     *
     * @param timeType 日期类型
     * @param date     时间
     * @return
     */
    public static String getReportTimeCode(String timeType, Date date) {
        String timeCode = CommonConst.EMPTY;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                timeCode = getDateTime(date, COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                timeCode = getDateTime(date, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                timeCode = getDateTime(date, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                timeCode = getDateTime(date, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return timeCode;
    }

    /**
     * 获取时间对应的环比时间
     *
     * @param timeType HOUR/DAY/MONTH/YEAR
     * @param date     时间值
     * @return
     */
    public static Date getLoopTime(String timeType, Date date) {
        Date dt = null;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = addHours(date, CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = addDays(date, CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = addMonths(date, CommonConst.DIGIT_MINUS_1);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = addYears(date, CommonConst.DIGIT_MINUS_1);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 获取整点时间
     *
     * @param timeType HOUR/DAY/MONTH/YEAR
     * @param date     时间值
     * @return
     */
    public static Date getHourTime(String timeType, Date date) {
        Date dt = null;
        if (ObjectUtil.isEmpty(date)) {
            date = new Date();
        }
        String tempStr = null;
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_HOUR);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_DAY);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                tempStr = getDateTime(date, COMMON_PATTERN_TO_MONTH);
                dt = toDateTime(tempStr, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                tempStr = getDateTime(date, COMMON_PATTERN_YEAR);
                dt = toDateTime(tempStr, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 计算两个时间间隔天数（日期格式比较）
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static int daysBetween(Date beginTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        long beginStamp = calendar.getTimeInMillis();
        calendar.setTime(endTime);
        long endStamp = calendar.getTimeInMillis();
        long betweenDays = (endStamp - beginStamp) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 计算两个时间间隔天数（字符串格式比较）
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static int daysBetween(String beginTime, String endTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date begin = format.parse(beginTime);
            Date end = format.parse(endTime);
            return daysBetween(begin, end);
        } catch (ParseException exception) {
            log.error("计算两个时间间隔天数" + exception.getMessage());
            return 0;
        }
    }

    /**
     * 根据时间类型把字符串转成对应的时间
     *
     * @param timeType 时间类型
     * @param time     时间字符串
     * @return
     */
    public static Date getTime(String timeType, String time) {
        Date dt = null;
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = toDateTime(time, COMMON_PATTERN_TO_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = toDateTime(time, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = toDateTime(time, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = toDateTime(time, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 根据时间类型把字符串转成对应的时间
     * timeType="HOUR",time="2025-02-01"，返回"2025-02-01 01:00:00"
     * timeType="DAY",time="2025-02"，返回"2025-02-01 01:00:00"
     * timeType="MONTH",time="2025"，返回"2025-02-01 01:00:00"
     *
     * @param timeType 时间类型
     * @param time     时间字符串
     * @return
     */
    public static Date getTypeTime(String timeType, String time) {
        Date dt = null;
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = toDateTime(time, COMMON_PATTERN_TO_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = toDateTime(time, COMMON_PATTERN_TO_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = toDateTime(time, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 根据时间类型把连续的日期字符串转成对应的时间 （202303、20230303、2023030301、202303030101）
     *
     * @param timeType 时间类型
     * @param time     时间字符串
     * @return
     */
    public static Date getTimeByContinuousTimeCode(String timeType, String time) {
        Date dt = null;
        timeType = StringUtil.ifEmptyOrNullReturnValue(timeType).toUpperCase();
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                dt = toDateTime(time, COMMON_PATTERN_HOUR);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = toDateTime(time, COMMON_PATTERN_DAY);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = toDateTime(time, COMMON_PATTERN_MONTH);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = toDateTime(time, COMMON_PATTERN_YEAR);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 根据时间类型返回天、月、年最后的时间
     *
     * @param timeType 时间类型
     * @param time     时间
     * @return
     */
    public static Date getEndTimeByType(String timeType, Date time) {
        Date dt = null;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                dt = DateUtil.endOfDay(time);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                dt = DateUtil.endOfMonth(time);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                dt = DateUtil.endOfYear(time);
                break;
            default:
                break;
        }
        return dt;
    }

    /**
     * 根据周期类型对生产周期进行加减计算
     * 填报周期类型（HOUR小时、DAY天、MONTH月、YEAR年）
     *
     * @param date      生产周期
     * @param cycleType 生产周期类型
     * @param cycleType 生产周期类型
     * @param val 计算值
     * @return
     */
    public static Date productionCycleCal(Date date, String cycleType, int val) {
        Date momDate = date;
        switch (cycleType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                momDate = DateUtils.addHours(date, val);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                momDate = DateUtils.addDays(date, val);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                momDate = DateUtils.addMonths(date, val);
                break;
        }
        return momDate;
    }

    /**
     * 根据周期类型对生产周期进行加减计算 并且进位  例如： HOUR +1进位就是 加一天
     * 填报周期类型（HOUR小时、DAY天、MONTH月、YEAR年）
     *
     * @param date      生产周期
     * @param cycleType 生产周期类型
     * @param val 计算值
     * @return
     */
    public static Date productionCycleCalCarry(Date date, String cycleType,int val) {
        Date momDate = date;
        switch (cycleType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                momDate = DateUtils.addDays(date, val);
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                momDate = DateUtils.addMonths(date, val);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                momDate = DateUtils.addYears(date, val);
                break;
        }
        return momDate;
    }

    /**
     * 通过时间类型返回对应的时间list
     * <p>
     * 参数：
     * timeType="YEAR",dataTime="2025-01-01 00:00:00"
     * timeType="MONTH",dataTime="2025-01-01 00:00:00"
     * timeType="DAY",dataTime="2025-01-01 00:00:00"
     * 返回格式：
     * [
     * {"datatime":"2025-01-01 00:00:00","value":"value1"},
     * {"datatime":"2025-02-01 00:00:00","value":"value2"},
     * {"datatime":"2025-03-01 00:00:00","value":"value3"},
     * {"datatime":"2025-04-01 00:00:00","value":"value4"},
     * {"datatime":"2025-05-01 00:00:00","value":"value5"},
     * {"datatime":"2025-06-01 00:00:00","value":"value6"},
     * {"datatime":"2025-07-01 00:00:00","value":"value7"},
     * {"datatime":"2025-08-01 00:00:00","value":"value8"},
     * {"datatime":"2025-09-01 00:00:00","value":"value9"},
     * {"datatime":"2025-10-01 00:00:00","value":"value10"},
     * {"datatime":"2025-11-01 00:00:00","value":"value11"},
     * {"datatime":"2025-12-01 00:00:00","value":"value12"}
     * ]
     *
     * @param timeType
     * @param dataTime
     * @return
     */
    public static List<TypeTime> getDateTimeList(String timeType, Date dataTime) {

        List<TypeTime> resultList = new ArrayList<>();
        Date beginTime;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_HOUR:
                beginTime = DateUtil.beginOfDay(dataTime);
                for (int i = 1; i <= 24; i++) {
                    TypeTime typeTime = new TypeTime();
                    typeTime.setDataTime(DateUtil.format(beginTime, COMMON_PATTERN));
                    typeTime.setTimeCode(CommonConst.WORD_H + DateUtil.format(beginTime, COMMON_PATTERN_HOUR));
                    typeTime.setDateTime(beginTime);
                    typeTime.setValue("value" + i);
                    resultList.add(typeTime);
                    beginTime = addHours(beginTime, 1);
                }
                break;
            case TimeTypeConst.TIME_TYPE_DAY:
                beginTime = DateUtil.beginOfMonth(dataTime);
                for (int i = 1; i <= 31; i++) {
                    TypeTime typeTime = new TypeTime();
                    typeTime.setDataTime(DateUtil.format(beginTime, COMMON_PATTERN));
                    typeTime.setTimeCode(CommonConst.WORD_D + DateUtil.format(beginTime, COMMON_PATTERN_DAY));
                    typeTime.setDateTime(beginTime);
                    typeTime.setValue("value" + i);
                    resultList.add(typeTime);
                    beginTime = addDays(beginTime, 1);
                }
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                beginTime = DateUtil.beginOfYear(dataTime);
                for (int i = 1; i <= 12; i++) {
                    TypeTime typeTime = new TypeTime();
                    typeTime.setDataTime(DateUtil.format(beginTime, COMMON_PATTERN));
                    typeTime.setTimeCode(CommonConst.WORD_M + DateUtil.format(beginTime, COMMON_PATTERN_MONTH));
                    typeTime.setDateTime(beginTime);
                    typeTime.setValue("value" + i);
                    resultList.add(typeTime);
                    beginTime = addMonths(beginTime, 1);
                }
                break;
        }
        return resultList;
    }
}
