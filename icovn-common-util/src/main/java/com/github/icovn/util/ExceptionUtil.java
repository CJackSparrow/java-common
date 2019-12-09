package com.github.icovn.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

  public static String getFullStackTrace(Exception e) {
    return "(getFullStackTrace)" + getFullStackTraceRaw(e);
  }

  public static String getFullStackTrace(Exception e, Boolean isRaw) {
    if (isRaw) {
      return getFullStackTraceRaw(e);
    }
    return "(getFullStackTrace)" + getFullStackTraceRaw(e).replaceAll("\n", "[NEW_LINE]");
  }

  public static String getFullStackTraceRaw(Exception e) {
    StringWriter sWriter = new StringWriter();
    PrintWriter pWriter = new PrintWriter(sWriter);
    e.printStackTrace(pWriter);
    String stackTrace = sWriter.toString();
    return stackTrace;
  }
}
