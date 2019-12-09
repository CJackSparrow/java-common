package com.github.icovn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

  public static String toNative(String input) {
    return "\"" + input + "\"";
  }

  public static String toString(InputStream inputStream) throws IOException {
    StringBuilder textBuilder = new StringBuilder();
    try (Reader reader =
        new BufferedReader(
            new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
      int c = 0;
      while ((c = reader.read()) != -1) {
        textBuilder.append((char) c);
      }
    }
    return textBuilder.toString();
  }

  // region CONVERT FROM STRING
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
  // endregion

  // region FILE & FOLDER PATH
  /**
   * Get parent folder
   *
   * @param folderPath /u03/hls/1/DKQ1ZXHH
   * @return /u03/hls/1/
   */
  public static String getParentFolder(String folderPath) {
    int lastIndex = folderPath.lastIndexOf('/');
    return folderPath.substring(0, lastIndex + 1);
  }
  // endregion

  // region PROCESS STRING (FFPROBE INPUT)

  // endregion
}
