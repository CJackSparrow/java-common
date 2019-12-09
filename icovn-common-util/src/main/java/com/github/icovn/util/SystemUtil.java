package com.github.icovn.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemUtil {

  private static String OS = System.getProperty("os.name").toLowerCase();

  public static void main(String[] args) {
    log.info(OS);

    if (isWindows()) {
      log.info("This is Windows");
    } else if (isMac()) {
      log.info("This is Mac");
    } else if (isUnix()) {
      log.info("This is Unix or Linux");
    } else if (isSolaris()) {
      log.info("This is Solaris");
    } else {
      log.info("Your OS is not support!!");
    }
  }

  public static boolean isWindows() {
    return (OS.indexOf("win") >= 0);
  }

  public static boolean isMac() {
    return (OS.indexOf("mac") >= 0);
  }

  public static boolean isUnix() {
    return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
  }

  public static boolean isSolaris() {
    return (OS.indexOf("sunos") >= 0);
  }

  public static String getOS() {
    if (isWindows()) {
      return "win";
    } else if (isMac()) {
      return "osx";
    } else if (isUnix()) {
      return "uni";
    } else if (isSolaris()) {
      return "sol";
    } else {
      return "err";
    }
  }
}
