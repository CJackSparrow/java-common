package com.github.icovn.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertUtils {

  public static long getLong(String input, Long defaultValue) {
    try {
      log.debug("getLong|" + input);
      return Long.parseLong(input);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static Integer getInteger(String input, Integer defaultValue) {
    try {
      log.debug("getInteger|" + input);
      return Integer.parseInt(input);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  public static Boolean getBoolean(String input, Boolean defaultValue) {
    try {
      log.debug("getInteger|" + input);
      return Boolean.parseBoolean(input);
    } catch (Exception ex) {
      return defaultValue;
    }
  }

  // old function
  public static boolean isValid(String input) {
    if (input != null) {
      if (input.trim().equals("")) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public static int toInt(boolean input){
    return (input) ? 1 : 0;
  }
}
