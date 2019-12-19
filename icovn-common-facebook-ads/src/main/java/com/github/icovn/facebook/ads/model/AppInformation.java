package com.github.icovn.facebook.ads.model;

import com.facebook.ads.sdk.APIContext;
import lombok.Data;

@Data
public class AppInformation {

  private String app;

  private String appSecret;

  private String accessToken;

  public APIContext getContext(boolean enableDebug) {
    if (app.contains("TET")) {
      return new APIContext(accessToken).enableDebug(enableDebug);
    }

    return new APIContext(accessToken, appSecret).enableDebug(enableDebug);
  }
}
