package com.github.icovn.google.drive.service;

import com.github.icovn.google.auth.service.GoogleAuthenticationService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleDriveService {

  @Value("${google.credential.application-name}")
  private String applicationName;

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final Set<String> SCOPES = DriveScopes.all();

  private final GoogleAuthenticationService authService;

  public GoogleDriveService(
      GoogleAuthenticationService authService) {
    this.authService = authService;
  }

  private Drive getDrive() throws IOException, GeneralSecurityException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new Drive.Builder(httpTransport, JSON_FACTORY,
        authService.getCredentials(httpTransport, SCOPES))
        .setApplicationName(applicationName)
        .build();
  }

  public String createFolder(String name) throws Exception {
    return createFolder(name, null);
  }

  public String createFolder(String name, String parentId) throws Exception {
    File fileMetadata = new File();
    fileMetadata.setName(name);
    fileMetadata.setMimeType("application/vnd.google-apps.folder");

    String fields = "id";
    if (parentId != null) {
      fields = "id, parents";
      fileMetadata.setParents(Collections.singletonList(parentId));
    }

    File file = getDrive().files().create(fileMetadata)
        .setFields(fields)
        .setSupportsTeamDrives(true)
        .execute();
    return file.getId();
  }

  public String createFolderIfNotExist(String name, String parentId) throws Exception {
    log.info("(createFolderIfNotExist)name: {}, parentId: {}", name, parentId);
    List<File> folders = searchFolderWithParentId(name, parentId);

    log.info("(createFolderIfNotExist)size: {}, isEmpty: {}", folders.size(), folders.isEmpty());
    if (folders.isEmpty()) {
      return createFolder(name, parentId);
    } else {
      return folders.get(0).getId();
    }
  }

  public String createFolderTree(String relativePath, String gdfsRootId) throws Exception {
    log.info("(createFolderTree)relativePath: {}, separator: {}", relativePath,
        java.io.File.separator);
    String formattedPath = relativePath.replace(java.io.File.separator, "|");
    String[] parentFolders = formattedPath.split("\\|");

    log.info("(createFolderTree)parentFolders: {}, length: {}", parentFolders,
        parentFolders.length);
    String parentId = gdfsRootId;
    if (parentFolders.length <= 1) {
      return parentId;
    }
    for (int i = 0; i < parentFolders.length - 1; i++) {
      String folderName = parentFolders[i];
      log.info("(createFolderTree)folderName: {}, parentId: {}, i: {}", folderName, parentId, i);
      if (!folderName.equals("")) {
        parentId = createFolderIfNotExist(folderName, parentId);
      }
    }

    return parentId;
  }

  public String createParentFolder(String relativePath, String gdfsRootId) throws Exception {
    log.info("(createParentFolder)relativePath: {}", relativePath);
    String formattedPath = relativePath.replace(java.io.File.separator, "|");
    String parentFolder = formattedPath.substring(0, formattedPath.lastIndexOf("|"));

    log.info("(createParentFolder)parentFolder: {}", parentFolder);
    return createFolderTree(parentFolder, gdfsRootId);
  }

  public List<File> searchByName(String name) throws Exception {
    return search("name = '" + name + "'");
  }

  public List<File> searchByNameAndMimeType(String name, String mimeType) throws Exception {
    return search("mimeType='" + mimeType + "' and name = '" + name + "'");
  }

  public List<File> searchFolderWithParentId(String name, String parentId) throws Exception {
    return search(
        "mimeType='application/vnd.google-apps.folder' and name = '" + name + "' and '" + parentId
            + "' in parents");
  }

  public List<File> search(String query, Boolean... searchTrashed) throws Exception {
    log.info("(search)query: {}", query);
    List<File> files = new ArrayList<>();

    if (searchTrashed.length == 0 || (searchTrashed.length > 0 && searchTrashed[0])) {
      //format query
      query += " and trashed = false";
      log.info("(search)formatted query: {}", query);
    }

    String pageToken = null;
    do {
      FileList result = getDrive().files().list()
          .setQ(query)
          .setSpaces("drive")
          .setFields("nextPageToken, files(id, name, parents)")
          .setPageToken(pageToken)
          .setIncludeTeamDriveItems(true)
          .setSupportsTeamDrives(true)
          .execute();
      files.addAll(result.getFiles());
      pageToken = result.getNextPageToken();
    } while (pageToken != null);

    log.info("(search)files.size(): {}", files.size());
    return files;
  }

  public String uploadFile(String filepath, String contentType) throws Exception {
    return uploadFile(filepath, contentType, null);
  }

  public String uploadFile(String filepath, String contentType, String parentId) throws Exception {
    java.io.File localFile = new java.io.File(filepath);
    File fileMetadata = new File();
    fileMetadata.setName(localFile.getName());

    String fields = "id";
    if (parentId != null) {
      fields = "id, parents";
      fileMetadata.setParents(Collections.singletonList(parentId));
    }

    FileContent mediaContent = new FileContent(contentType, localFile);
    File file = getDrive().files().create(fileMetadata, mediaContent)
        .setFields(fields)
        .setSupportsTeamDrives(true)
        .execute();
    return file.getId();
  }

  public String uploadFolder(String folderPath) throws Exception {
    return uploadFolder(folderPath, null);
  }

  public String uploadFolder(String folderPath, String parentId) throws Exception {
    java.io.File localFolder = new java.io.File(folderPath);
    if (localFolder.isFile()) {
      return null;
    }

    //create Google Drive folder
    String currentFolderId = createFolder(localFolder.getName(), parentId);

    //create children folders & files
    for (java.io.File childFile : localFolder.listFiles()) {
      if (childFile.isDirectory() && !isSymlink(childFile)) {
        log.info("Folder\t" + childFile.getName());

        uploadFolder(childFile.getAbsolutePath(), currentFolderId);
      } else {
        log.info("File\t\t" + childFile.getName());
        uploadFile(childFile.getAbsolutePath(), null, currentFolderId);
      }
    }

    return currentFolderId;
  }

  public boolean isSymlink(java.io.File file) throws IOException {
    return !file.getAbsolutePath().equals(file.getCanonicalPath());
  }
}
