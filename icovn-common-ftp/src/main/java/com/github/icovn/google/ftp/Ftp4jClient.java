package com.github.icovn.google.ftp;

import static com.github.icovn.util.ExceptionUtil.getFullStackTrace;
import static com.github.icovn.util.FileUtil.getParent;

import lombok.extern.slf4j.Slf4j;
import it.sauronsoftware.ftp4j.FTPClient;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Slf4j
public class Ftp4jClient {

  private String ip;
  private int port = 21;
  private String username;
  private String password;
  private boolean passiveMode = true;

  private static final Logger logger = LoggerFactory.getLogger(Ftp4jClient.class);

  public boolean downloadFile(String remotePath, String localPath){
    try{
      FTPClient client = new FTPClient();
      client.connect(this.ip, this.port);
      client.login(this.username, this.password);
      client.setPassive(passiveMode);

      File directory = new File(getParent(localPath));
      if(!directory.exists()){
        directory.mkdirs();
      }
      File file = new File(remotePath);
      client.changeDirectory(getParent(remotePath));
      client.download(FilenameUtils.getName(remotePath), file);

      return true;
    }catch (Exception ex){
      logger.error(getFullStackTrace(ex));
      return false;
    }
  }

  public boolean uploadFile(String localPath){
    try{
      FTPClient client = new FTPClient();
      client.connect(this.ip, this.port);
      client.login(this.username, this.password);

      File file = new File(localPath);
      client.upload(file);
      return true;
    }catch (Exception ex){
      logger.error(getFullStackTrace(ex));
      return false;
    }
  }

  public boolean uploadFileToFolder(String remotePath, String localPath){
    try{
      FTPClient client = new FTPClient();
      client.connect(this.ip, this.port);
      client.login(this.username, this.password);

      File file = new File(localPath);
      client.changeDirectory(getParent(remotePath));
      client.upload(file);
      return true;
    }catch (Exception ex){
      logger.error(getFullStackTrace(ex));
      return false;
    }
  }
}
