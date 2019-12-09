package com.github.icovn.util.command;

import static com.github.icovn.util.FileUtil.getExtension;
import static com.github.icovn.util.FileUtil.getParent;
import static com.github.icovn.util.command.FfmpegUtil.getFileDuration;
import static com.github.icovn.util.command.ShellCommandUtil.runCommand;
import static com.github.icovn.util.command.UnixCommandUtil.createScript;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnixConvertMediaUtil {

  /** Make folder to store script, converted media */
  private CommandResult makeFolder(String folderPath) {
    log.debug("(makeFolder)folderPath: {}", folderPath);

    String commandTemplate = "mkdir -m 775 -p %FOLDER_PATH%";
    String command = commandTemplate.replace("%FOLDER_PATH%", folderPath);
    log.debug("(makeFolder)command: {}", command);

    return runCommand(command);
  }

  /**
   * /tmp/video/uuid.mp4 --> /tmp/video/uuid_low.mp4
   *
   * @param inputFilePath
   * @param convertCommand
   * @return
   */
  public boolean convertToLowFormat(String inputFilePath, String convertCommand) {
    String convertFolderPath = getParent(inputFilePath);

    String logFilePath = convertFolderPath + "/low.log";
    String convertedFilePath = getLowFormatFilePath(inputFilePath);
    String scriptContent =
        convertCommand
            .replace("%INPUT%", inputFilePath)
            .replace("%OUTPUT%", convertedFilePath)
            .replace("%LOG%", logFilePath);

    String scriptFilePath = convertFolderPath + "/low.sh";
    return runScript(scriptFilePath, scriptContent);
  }

  public boolean validateLowFormat(String inputFilePath) {
    return validateConvertedMedia(inputFilePath, getLowFormatFilePath(inputFilePath));
  }

  public boolean convertToHlsFormat(String inputFilePath, String convertCommand) {
    String convertFolderPath = getParent(inputFilePath);

    String convertedFilePath = getHlsFilePath(inputFilePath);
    String logFilePath = convertFolderPath + "/hls.log";
    String scriptContent =
        convertCommand
            .replace("%INPUT%", inputFilePath)
            .replace("%OUTPUT%", convertedFilePath)
            .replace("%LOG%", logFilePath);

    String scriptFilePath = convertFolderPath + "/hls.sh";
    return runScript(scriptFilePath, scriptContent);
  }

  public boolean validateHlsFormat(String inputFilePath) {
    return validateConvertedMedia(inputFilePath, getHlsFilePath(inputFilePath));
  }

  private String getLowFormatFilePath(String inputFilePath) {
    String extension = getExtension(inputFilePath);
    return inputFilePath.replace(extension, "_low" + extension);
  }

  private String getHlsFilePath(String inputFilePath) {
    return getParent(inputFilePath) + "/playlist.m3u8";
  }

  /** Validate base on duration of converted media */
  private Boolean validateConvertedMedia(String inputFilePath, String convertedFilePath) {
    log.debug(
        "(validateConvertedMedia)inputFilePath: {}, convertedFilePath: {}",
        inputFilePath,
        convertedFilePath);
    long originDuration = getFileDuration(inputFilePath);
    long convertDuration = getFileDuration(convertedFilePath);
    if ((convertDuration >= originDuration || convertDuration + 1 == originDuration)
        && convertDuration > 0) {
      return true;
    } else {
      log.debug(
          "(validateConvertedMedia)DIFFERENT_DURATION, inputFilePath: {}, convertedFilePath: {}, originDuration: {}, convertDuration: {}",
          inputFilePath,
          convertedFilePath,
          originDuration,
          convertDuration);
      return false;
    }
  }

  public static boolean runScript(String scriptFilePath, String scriptContent) {
    CommandResult createScriptResult = createScript(scriptFilePath, scriptContent);
    if (createScriptResult.getExitStatus() == 0) {
      CommandResult convertLowResult = UnixCommandUtil.runScript(scriptFilePath);
      if (convertLowResult.getExitStatus() == 0) {
        return true;
      }
    }
    return false;
  }
}
