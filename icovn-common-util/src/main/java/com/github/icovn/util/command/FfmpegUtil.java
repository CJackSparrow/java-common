package com.github.icovn.util.command;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;
import static com.github.icovn.util.StringUtil.getLong;

import com.github.icovn.util.MapperUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FfmpegUtil {

  /**
   * Get bit rate of file
   *
   * @param path
   * @return
   */
  public static boolean isH264File(String path) {
    return isH264(getInformation(path));
  }

  /**
   * Get duration of file in second
   *
   * @param path
   * @return
   */
  public static long getFileDuration(String path) {
    return getDuration(getInformation(path));
  }

  /**
   * Get bit rate of file
   *
   * @param path
   * @return
   */
  public static long getFileBitRate(String path) {
    return getBitRate(getInformation(path));
  }

  /**
   * Get quality of file
   *
   * @param path
   * @return
   */
  public static boolean getFileQuality(String path) {
    String commandTemplate = "ffprobe %PATH%";
    String command = commandTemplate.replace("%PATH%", path);
    log.info(command);
    return getQuality(ShellCommandUtil.executeCommand(command));
  }

  public static String getInformation(String path) {
    String commandTemplate = "ffprobe -v quiet -print_format json -show_format -show_streams %PATH%";
    String command = commandTemplate.replace("%PATH%", path);
    log.info(command);
    return ShellCommandUtil.executeCommand(command);
  }

  public static MediaInformation getMediaInformation(String path) throws Exception{
    return MapperUtil.getMapper().readValue(getInformation(path), MediaInformation.class);
  }

  /**
   * Check if duration match or not
   *
   * @param input Format origin_path,convert_path
   * @return
   */
  public static boolean isSameDuration(String input) {
    boolean result = false;
    try {
      log.debug(input);
      String path = input.split(",")[0];
      String wapPath = input.split(",")[1];

      long originDuration = getFileDuration(path);
      long convertDuration = getFileDuration(wapPath);
      if (convertDuration >= originDuration && convertDuration > 0) {
        result = true;
      }
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    } finally {
      log.info("CHECK_DURATION|" + input + "|" + result);
      return result;
    }
  }

  /**
   * Get duration from ffprobe json input
   *
   * @param input
   * @return
   */
  public static long getDuration(String input) {
    int startIndex = input.indexOf("duration");
    log.debug("startIndex: {}", startIndex);
    if (startIndex > 0) {
      int endIndex = input.indexOf('.', startIndex);
      log.debug(input.substring(startIndex, input.length()));
      log.debug("endIndex: {}", endIndex);
      if (endIndex > 0) {
        return getLong(input.substring(startIndex + 12, endIndex), (long) -1);
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  /**
   * Get bit_rate from ffprobe json input
   *
   * @param input
   * @return
   */
  public static long getBitRate(String input) {
    int startIndex = input.indexOf("bit_rate");
    log.debug("startIndex|" + startIndex);
    if (startIndex > 0) {
      int endIndex = input.indexOf(',', startIndex);
      log.debug("endIndex|" + endIndex);
      if (endIndex > 0) {
        return getLong(input.substring(startIndex + 12, endIndex - 1), (long) -1);
      } else {
        endIndex = input.lastIndexOf('"');
        log.debug("endIndex2|" + endIndex);
        if (endIndex > 0) {
          return getLong(input.substring(startIndex + 12, endIndex), (long) -1);
        } else {
          return -2;
        }
      }
    } else {
      return -1;
    }
  }

  /**
   * Get info from ffprobe json input
   *
   * @param input FFprobe json input
   * @param key
   * @return
   */
  private static String getFfprobeInfo(String input, String key) {
    int startIndex = input.indexOf(key);
    log.debug("getFfprobeInfo|startIndex|" + startIndex);
    if (startIndex > 0) {
      int endIndex = input.indexOf('.', startIndex);
      log.debug(input.substring(startIndex));
      log.debug("getFfprobeInfo|endIndex|" + endIndex);
      if (endIndex > 0) {
        return input.substring(startIndex, endIndex);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Get quality (good or not) from ffprobe json input
   *
   * @param input
   * @return
   */
  public static boolean getQuality(String input) {
    int errorIndex = input.indexOf("error");
    log.debug("errorIndex|" + errorIndex);
    if (errorIndex > 0) {
      return false;
    } else {
      int noframeIndex = input.indexOf("no frame");
      log.debug("noframeIndex|" + noframeIndex);
      if (noframeIndex > 0) {
        return false;
      } else {
        return true;
      }
    }
  }

  public static boolean isH264(String input) {
    return input.indexOf("h264") > 0;
  }

  public static String getCodec(String input) {
    String codec;
    int startIndex = input.indexOf("codec_name");
    if (startIndex > 0) {
      if (input.length() > startIndex + 14) {
        codec = input.substring(startIndex, startIndex + 14);
      } else {
        codec = input.substring(startIndex, input.length());
      }
      int lastIndex = input.lastIndexOf("codec_name");
      if (lastIndex != startIndex) {
        if (input.length() > lastIndex + 14) {
          codec += "&" + input.substring(lastIndex, lastIndex + 14);
        } else {
          codec += "&" + input.substring(lastIndex, input.length());
        }
      }
    } else {
      codec = input;
    }
    return codec;
  }
}
