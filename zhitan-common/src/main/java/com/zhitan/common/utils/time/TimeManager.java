package com.zhitan.common.utils.time;

import com.zhitan.common.enums.TimeType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class TimeManager {

  public static final TimeType[] typeArray = {TimeType.LIVE, TimeType.DAY, TimeType.HOUR,
      TimeType.MONTH, TimeType.SCHEDULING, TimeType.YEAR};
  private static final DateTimeFormatter pastDataFormat = DateTimeFormat.forPattern
      ("yyyyMMddHHmmss");
  private static final DateTimeFormatter hourFormat = DateTimeFormat.forPattern("yyyyMMddHH");
  private static final DateTimeFormatter dayFormat = DateTimeFormat.forPattern("yyyyMMdd");
  private static final DateTimeFormatter monthFormat = DateTimeFormat.forPattern("yyyyMM");
  private static final DateTimeFormatter yearFormat = DateTimeFormat.forPattern("yyyy");

  private static final String livePrefix = "L";
  private static final String hourPrefix = "H";
  private static final String dayPrefix = "D";
  private static final String monthPrefix = "M";
  private static final String yearPrefix = "Y";

  public static String getExecuteTimeCode(Date time, TimeType type, int delayTime) {
    DateTime dateTime = new DateTime(time).plusSeconds(-delayTime);
    return getTimeCode(dateTime.toDate(), type);
  }

  public static String getTimeCode(Date time, TimeType type) {
    switch (type) {
      case LIVE:
        return livePrefix;
      case DAY:
        return dayPrefix + dayFormat.print(new DateTime(time));
      case HOUR:
        return hourPrefix + hourFormat.print(new DateTime(time));
      case MONTH:
        return monthPrefix + monthFormat.print(new DateTime(time));
      case YEAR:
        return yearPrefix + yearFormat.print(new DateTime(time));
      default:
        return null;
    }
  }

  public static Date getBeginTime(String timeCode) {
    Date beginTime = getTime(timeCode, 0);
    if (getTimeType(timeCode) == TimeType.DAY) {
      beginTime = new DateTime(beginTime).plusHours(9).toDate();
    }

    return beginTime;
  }

  public static Date getEndTime(String timeCode) {
    Date endTime = getTime(timeCode, 1);
    if (getTimeType(timeCode) == TimeType.DAY) {
      endTime = new DateTime(endTime).plusHours(9).toDate();
    }

    return endTime;
  }

  public static Date getTime(String timeCode) {
    return getTime(timeCode, 0);
  }

  public static Date getTime(String timeCode, int offset) {
    TimeType type = getTimeType(timeCode);
    String timeStr = timeCode.substring(1);
    Date result = null;
    DateTime dateTime;

    try {
      if (type == null) {
        result = pastDataFormat.parseDateTime(timeStr).toDate();
        if (offset != 0) {
          dateTime = new DateTime(result);
          dateTime = dateTime.plusSeconds(offset);
          result = dateTime.toDate();
        }
      } else {
        switch (type) {
          case LIVE:
            result = new Date();
            break;
          case DAY:
            result = dayFormat.parseDateTime(timeStr).toDate();
            if (offset != 0) {
              dateTime = new DateTime(result);
              dateTime = dateTime.plusDays(offset);
              result = dateTime.toDate();
            }
            break;
          case HOUR:
            result = hourFormat.parseDateTime(timeStr).toDate();
            if (offset != 0) {
              dateTime = new DateTime(result);
              dateTime = dateTime.plusHours(offset);
              result = dateTime.toDate();
            }
            break;
          case MONTH:
            result = monthFormat.parseDateTime(timeStr).toDate();
            if (offset != 0) {
              dateTime = new DateTime(result);
              dateTime = dateTime.plusMonths(offset);
              result = dateTime.toDate();
            }
            break;
          case YEAR:
            result = yearFormat.parseDateTime(timeStr).toDate();
            if (offset != 0) {
              dateTime = new DateTime(result);
              dateTime = dateTime.plusYears(offset);
              result = dateTime.toDate();
            }
            break;
          default:
            break;
        }
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    return result;
  }

  public static TimeType getTimeType(String timeCode) {
    switch (timeCode.substring(0, 1).toUpperCase()) {
      case livePrefix:
        return TimeType.LIVE;
      case hourPrefix:
        return TimeType.HOUR;
      case dayPrefix:
        return TimeType.DAY;
      case monthPrefix:
        return TimeType.MONTH;
      case yearPrefix:
        return TimeType.YEAR;
      default:
        return null;
    }
  }
}
