package com.github.icovn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

  public static String removeAccent(String s) {
    StringBuilder sb = new StringBuilder(s);
    for (int i = 0; i < sb.length(); i++) {
      sb.setCharAt(i, removeAccent(sb.charAt(i)));
    }
    return sb.toString();
  }
  private static char removeAccent(char ch) {
    String special = "àÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬđĐèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆìÌỉỈĩĨíÍịỊòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰýÝỹỸỳỲỷỶỵỴ :+\\<>\"*,!?%$=@#~[]`|^'.;＆&";
    String replace = "aAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAdDeEeEeEeEeEeEeEeEeEeEeEiIiIiIiIiIoOoOoOoOoOoOoOoOoOoOoOoOoOoOoOoOoOuUuUuUuUuUuUuUuUuUuUuUyYyYyYyYyY----\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0----";
    int index = special.indexOf(ch);
    if (index >= 0) {
      ch = replace.charAt(index);
    }
    return ch;
  }

  public static String toNative(String input) {
    return "\"" + input + "\"";
  }

  public static String toRaw(List<String> ids) {
    if (ids == null || ids.isEmpty()) {
      return "";
    }

    String output = "";
    for (String id : ids) {
      output += "'" + id + "',";
    }

    return output.substring(0, output.length() - 1);
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

  public static String toUrlFriendly(String s) {
    int maxLength = Math.min(s.length(), 236);
    char[] buffer = new char[maxLength];
    int n = 0;
    for (int i = 0; i < maxLength; i++) {
      char ch = s.charAt(i);
      buffer[n] = removeAccent(ch);
      // skip not printable characters
      if (buffer[n] > 31) {
        n++;
      }
    }
    // skip trailing slashes
    while (n > 0 && buffer[n - 1] == '/') {
      n--;
    }

    String result = String.valueOf(buffer, 0, n);
    result = result.replaceAll("\\/", "");
    return result;
  }
}
