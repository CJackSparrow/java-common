package com.github.icovn.util.command;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/** Created by icovn on 6/23/2016. */
@Slf4j
public class ShellCommandUtil {

  public static CommandResult runCommand(String command) {
    CommandResult commandResult = new CommandResult();
    String stdOut = "";
    String stdErr = "";
    Process runJob = null;
    try {
      runJob = Runtime.getRuntime().exec(command);

      InputStream cmdStdErr = null;
      InputStream cmdStdOut = null;

      cmdStdErr = runJob.getErrorStream();
      cmdStdOut = runJob.getInputStream();

      String line;
      BufferedReader bufferStdOut = new BufferedReader(new InputStreamReader(cmdStdOut));
      while ((line = bufferStdOut.readLine()) != null) {
        stdOut += line;
      }
      cmdStdOut.close();

      BufferedReader bufferStdErr = new BufferedReader(new InputStreamReader(cmdStdErr));
      while ((line = bufferStdErr.readLine()) != null) {
        stdErr += line;
      }
      cmdStdErr.close();

      commandResult.setExitStatus(runJob.waitFor());
      commandResult.setOutput(stdOut + "|" + stdErr);
    } catch (IOException ex) {
      log.error(getFullStackTrace(ex));
    } catch (InterruptedException ex) {
      log.error(getFullStackTrace(ex));
    }
    return commandResult;
  }

  /**
   * TODO: This function doesn't work
   * http://docs.oracle.com/javase/7/docs/api/java/lang/Process.html Because some native platforms
   * only provide limited buffer size for standard input and output streams, failure to promptly
   * write the input stream or read the output stream of the subprocess may cause the subprocess to
   * block, or even deadlock.
   */
  public static void executeLongCommand(String command) {
    Process p;
    try {
      // start execution
      p = Runtime.getRuntime().exec(command);

      // exhaust input stream
      BufferedInputStream in = new BufferedInputStream(p.getInputStream());
      byte[] bytes = new byte[4096];
      while (in.read(bytes) != -1) {}

      // wait for completion
      p.waitFor();
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    }
  }

  /**
   * @param command
   * @param timeout
   * @param timeUnit
   * @return
   */
  public static String executeTimeoutCommand(String command, long timeout, TimeUnit timeUnit) {
    StringBuffer output = new StringBuffer();

    Process p;
    try {
      System.out.println(
          "execute command with timeout|" + command + "|" + timeout + "|" + timeUnit);
      p = Runtime.getRuntime().exec(command);
      if (!p.waitFor(timeout, timeUnit)) {
        System.out.println("timeout happened|" + command + "|" + timeout + "|" + timeUnit);
        // timeout - kill the process.
        p.destroyForcibly(); // consider using destroyForcibly instead
        return Integer.toString(p.exitValue());
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception ex) {
      log.error("error|" + timeout);
      log.error(getFullStackTrace(ex));
    }

    return output.toString();
  }

  public static String executeCommand(String... command) {
    StringBuffer output = new StringBuffer();
    try {
      ProcessBuilder ps = new ProcessBuilder(command);
      ps.redirectErrorStream(true);

      Process pr = ps.start();

      BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        log.debug(line);
        output.append(line + "\n");
      }
      pr.waitFor();

      in.close();
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    }
    return output.toString();
  }

  public static String executeCommand(String command) {
    StringBuffer output = new StringBuffer();

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception ex) {
      log.error(getFullStackTrace(ex));
    }

    return output.toString();
  }
}
