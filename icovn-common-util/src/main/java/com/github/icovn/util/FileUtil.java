package com.github.icovn.util;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

@Slf4j
public class FileUtil {

  public static void changeModInLinux(String path) {
    log.info("(changeMod)path: {}", path);
    changeModInLinux(path, "777");
  }

  public static void changeModInLinux(String path, String mode) {
    log.info("(changeMod)path: {}, mode: {}", path, mode);
    try {
      String command = "chmod -R " + mode + " " + path;
      log.info("(changeMod)command: {}", command);
      if (SystemUtil.isUnix()) {
        Runtime.getRuntime().exec(command);
      } else {
        log.info("(changeMod)NOT_SUPPORT");
      }
    } catch (Exception ex) {
      log.error("(changeMod)ex: {}", ex.getMessage());
    }
  }

  public static String createDateDirectory(String rootDirectory) {
    log.info("(createDirectory)rootDirectory: {}", rootDirectory);
    Calendar calendar = Calendar.getInstance();

    String yearPath = rootDirectory + "/" + calendar.get(Calendar.YEAR);
    createDirectory(yearPath);

    String monthPath = yearPath + "/" + getMonthInHumanView(calendar.get(Calendar.MONTH));
    createDirectory(monthPath);

    String datePath = monthPath + "/" + calendar.get(Calendar.DAY_OF_MONTH);
    createDirectory(datePath);
    return datePath;
  }

  private static int getMonthInHumanView(int machineMonth) {
    return machineMonth + 1;
  }

  public static void createDirectory(String directoryPath, FileAttribute<?>... attrs) {
    log.info("(createDirectory)directoryPath: {}", directoryPath);
    try {
      if (!(new File(directoryPath)).exists()) {
        Path path = Paths.get(directoryPath);
        if (SystemUtil.isUnix()) {
          Files.createDirectories(path, attrs);
        } else {
          Files.createDirectories(path);
        }
      } else {
        log.info("(createDirectory)directoryPath: {} EXIST", directoryPath);
      }
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static void createFile(byte[] bytes, String filepath, FileAttribute<?>... attrs) {
    log.info("(createFile)filepath: {}", filepath);
    try {
      Path path = Paths.get(filepath);
      Files.createFile(path, attrs);

      Files.write(path, bytes);
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static String getExtension(String uri) {
    return uri.substring(uri.lastIndexOf("."));
  }

  public static String getExtensionWithoutDot(String uri) {
    return uri.substring(uri.lastIndexOf(".")).replace(".", "");
  }

  public static String getFilename(String filepath) {
    Path path = Paths.get(filepath);
    return path.getFileName().toString();
  }

  public static String getFilenameWithoutExtension(String filepath) {
    String filename = getFilename(filepath);
    if (filename == null) {
      return null;
    }

    int dotIndex = filename.lastIndexOf(".");
    if (dotIndex > 0) {
      return filename.substring(0, dotIndex);
    } else {
      return filename;
    }
  }

  /**
   * Get size of file in MB
   *
   * @param path File path
   * @return
   */
  public static double getFileSize(String path) {
    File file = new File(path);
    if (file.exists()) {
      double bytes = file.length();
      double kilobytes = (bytes / 1024);
      double megabytes = (kilobytes / 1024);

      return megabytes;
    } else {
      return 0;
    }
  }

    public static String getParent(String filepath) {
    log.info("(getParent)filepath: {}", filepath);
    Path file = Paths.get(filepath);
    Path parent = file.getParent();
    if (parent == null) {
      return null;
    }

    return parent.toString();
  }

  /**
   * Use small RAM
   */
  public static String searchStringInFile(String filePath, String keyword) {
    String result = "";
    try (Stream<String> lines = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        if (line.contains(keyword)) {
          return line;
        }
      }
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    }
    return result;
  }

  public static List<String> toList(String filePath) {
    List<String> result = new ArrayList<>();
    try (Stream<String> lines = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
      for (String line : (Iterable<String>) lines::iterator) {
        result.add(line);
      }
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    }
    return result;
  }

  public static void unzipFile(String zipFilepath, FileAttribute<?>... attrs) throws IOException {
    log.info("(unzipFile)zipFilename: {}", zipFilepath);
    String parent = getParent(zipFilepath);
    String zipFolder = parent + "/" + getFilenameWithoutExtension(zipFilepath) + "/";

    FileUtils.deleteDirectory(new File(zipFolder));
    Files.createDirectories(Paths.get(zipFolder), attrs);

    ZipUtil.unpack(new File(zipFilepath), new File(zipFolder));
  }

  public static FileAttribute<Set<PosixFilePermission>> getFullPermissions() {
    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
    return PosixFilePermissions.asFileAttribute(permissions);
  }
}
