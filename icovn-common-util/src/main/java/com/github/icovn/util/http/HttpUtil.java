package com.github.icovn.util.http;

import com.github.icovn.util.ExceptionUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/** Created by ico on 3/6/17. */
@Slf4j
public class HttpUtil {

  private static final int DOWNLOAD_CHUNK_SIZE = 2048; // Same as Okio Segment.SIZE

  public static boolean downloadUsingOkHttp(String fileUrl, String storagePath) {
    try {
      Response response = call(fileUrl);
      if (!response.isSuccessful()) {
        log.error("(downloadUsingOkHttp)download error: {}", response.code());
        response.close();
        return false;
      }

      FileOutputStream fos = new FileOutputStream(storagePath);
      fos.write(response.body().bytes());
      fos.close();
      response.close();
    } catch (Exception ex) {
      log.error("(downloadUsingOkHttp)" + ex.getMessage());
      return false;
    }
    return true;
  }

  private static Response call(String url) throws IOException {
    OkHttpClient client = HttpClient.getInstance().getHttpClient();

    Request request =
        new Request.Builder()
            .url(url)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
            .build();
    return client.newCall(request).execute();
  }

  public static boolean downloadUsingOkHttpWithTrunk(String fileUrl, String storagePath) {
    try {
      Response response = call(fileUrl);
      if (!response.isSuccessful()) {
        log.error("(downloadUsingOkHttpWithTrunk)download error: {}", response.code());
        response.close();
        return false;
      }

      ResponseBody body = response.body();
      long contentLength = body.contentLength();
      BufferedSource source = body.source();

      File file = new File(storagePath);
      BufferedSink sink = Okio.buffer(Okio.sink(file));

      long totalRead = 0;
      long read = 0;
      while ((read = source.read(sink.buffer(), DOWNLOAD_CHUNK_SIZE)) != -1) {
        totalRead += read;
      }
      sink.writeAll(source);
      sink.flush();
      sink.close();
    } catch (Exception ex) {
      log.error("(downloadUsingOkHttp)" + ex.getMessage());
      return false;
    }
    return true;
  }

  public static String query(String url) {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url(url).build();

    try {
      log.info("[query]" + url);
      Response response = client.newCall(request).execute();
      String body = response.body().string();
      response.close();
      log.info("[query]" + body);
      return body;
    } catch (Exception ex) {
      log.error(ExceptionUtil.getFullStackTrace(ex));
      return null;
    }
  }

  public static String getHostFromUrl(String input) {
    try {
      URL url = new URL(input);
      String domain = url.getHost();
      if (domain.startsWith("wwww.")) {
        return domain.substring(4);
      }

      return domain;
    } catch (MalformedURLException ex) {
      return null;
    }
  }
}
