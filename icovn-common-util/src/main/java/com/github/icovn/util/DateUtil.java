package com.github.icovn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtil {

  public static final String DATE_IDENTIFY_FORMAT = "yyyyMMdd";

  public static Date add(Date date, Boolean increase) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    if (increase) {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -1);
    }

    return calendar.getTime();
  }

  public static Date toDate(String date) {
    return toDate(date, DATE_IDENTIFY_FORMAT);
  }

  public static Date toDate(String date, String simpleDateFormat) {
    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
    return toDate(date, format);
  }

  public static Date toDate(String date, SimpleDateFormat simpleDateFormat) {
    try {
      return simpleDateFormat.parse(date);
    } catch (ParseException | NullPointerException ex) {
      log.info("[toDate]PARSE_ERROR|" + date + "|" + simpleDateFormat.toPattern());
      return null;
    }
  }

  public static List<String> toList(Date from, Date until, String simpleDateFormat) {
    log.info(String.format("(toList)from: %s, until: %s", from, until));
    List<String> list = new ArrayList<>();
    Date tmpDate = from;
    while (tmpDate.before(until)) {
      log.info("(toList)endDate: %s", tmpDate);
      list.add(toString(tmpDate, tmpDate, simpleDateFormat));
      tmpDate = add(tmpDate, true);
    }

    return list;
  }

  public static Long toLong(String date, long defaultValue) {
    return toLong(date, DATE_IDENTIFY_FORMAT, defaultValue);
  }

  public static Long toLong(String date, String simpleDateFormat, long defaultValue) {
    Long result = toLong(date, simpleDateFormat);
    if (result == null) {
      return defaultValue;
    } else {
      return result;
    }
  }

  public static Long toLong(String date) {
    return toLong(date, DATE_IDENTIFY_FORMAT);
  }

  public static Long toLong(String date, String simpleDateFormat) {
    Date convertedDate = toDate(date, simpleDateFormat);
    if (convertedDate == null) {
      return null;
    } else {
      return convertedDate.getTime();
    }
  }

  public static String toString(Date date) {
    return toString(date, DATE_IDENTIFY_FORMAT);
  }

  public static String toString(Date date, String simpleDateFormat) {
    SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
    return toString(date, format);
  }

  public static String toString(Date date, SimpleDateFormat simpleDateFormat) {
    if (date == null) {
      return null;
    }

    return simpleDateFormat.format(date);
  }

  public static String toString(Date from, Date until, String simpleDateFormat) {
    // "yyyy-MM-dd"
    return "{'since':'"
        + toString(from, simpleDateFormat)
        + "','until':'"
        + toString(until, simpleDateFormat)
        + "'}";
  }
}
