package com.github.icovn.util.command;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnixCommandUtil {

  /** Create .sh sendToConvertFlow script */
  public static CommandResult createScript(String scriptPath, String content) {
    Writer output = null;
    try {
      output = new BufferedWriter(new FileWriter(scriptPath));
      output.write(content);

      CommandResult commandResult = ShellCommandUtil.runCommand("chmod u+x " + scriptPath);
      return commandResult;
    } catch (IOException ex) {
      log.error(getFullStackTrace(ex));
      return null;
    } finally {
      try {
        output.close();
      } catch (IOException ex) {
        log.error(getFullStackTrace(ex));
        return null;
      }
    }
  }

  /** Run .sh sendToConvertFlow script */
  public static CommandResult runScript(String scriptPath) {
    CommandResult commandResult = ShellCommandUtil.runCommand(scriptPath);
    return commandResult;
  }

  public static Boolean changeMode(String folderPath) {
    String setModCommand = "chmod -R 775 " + folderPath;
    CommandResult setModResult = ShellCommandUtil.runCommand(setModCommand);
    log.info(setModCommand + "|" + setModResult);
    if (setModResult.getExitStatus() == 0) {
      return true;
    } else {
      return false;
    }
  }

  public static Boolean deleteFolder(String folderPath) {
    String commandTemplate = "rm -rf %FOLDER_PATH%";
    String command = commandTemplate.replace("%FOLDER_PATH%", folderPath);
    CommandResult deleteResult = ShellCommandUtil.runCommand(command);
    log.info(command + "|" + deleteResult);
    if (deleteResult.getExitStatus() == 0) {
      return true;
    } else {
      return false;
    }
  }

  public static Boolean copyFileToRemoteServer(
      String sourcePath, String destinationPath, String server, String username) {
    String commandTemplate = "scp -p %SOURCE_PATH% vt_admin@%SERVER%:%DESTINATION_PATH%";
    String command =
        commandTemplate
            .replace("%SERVER%", server)
            .replace("%SOURCE_PATH%", sourcePath)
            .replace("%DESTINATION_PATH%", destinationPath);
    CommandResult copyResult = ShellCommandUtil.runCommand(command);
    log.info(command + "|" + copyResult);
    if (copyResult.getExitStatus() == 0) {
      return true;
    } else {
      return false;
    }
  }

  public static Boolean copyFolderToRemoteServer(
      String sourcePath, String destinationPath, String server) {
    String commandTemplate = "scp -r -p %SOURCE_PATH% vt_admin@%SERVER%:%DESTINATION_PATH%";
    String command =
        commandTemplate
            .replace("%SERVER%", server)
            .replace("%SOURCE_PATH%", sourcePath)
            .replace("%DESTINATION_PATH%", destinationPath);
    CommandResult result = ShellCommandUtil.runCommand(command);
    log.info(command + "|" + result);
    if (result.getExitStatus() == 0) {
      return true;
    } else {
      return false;
    }
  }
}
