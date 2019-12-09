package com.github.icovn.util.http;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/** Created by ico on 3/14/17. */
public class HttpClient {

  private static HttpClient ourInstance = new HttpClient();
  private final OkHttpClient client;

  private HttpClient() {
    client =
        new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(900, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .followRedirects(true)
            .build();
  }

  public static HttpClient getInstance() {
    return ourInstance;
  }

  public OkHttpClient getHttpClient() {
    return this.client;
  }
}
