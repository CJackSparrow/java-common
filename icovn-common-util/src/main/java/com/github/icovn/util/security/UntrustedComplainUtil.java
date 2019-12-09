package com.github.icovn.util.security;

import com.github.icovn.util.ExceptionUtil;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UntrustedComplainUtil {

  public static void disableUntrustedComplain() {
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {
          }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {
          }
        }
    };
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      SSLContext.setDefault(sc);
      log.info("(disableUntrustedComplain)DISABLED");
    } catch (Exception ex) {
      log.error("(disableUntrustedComplain)DISABLED_FAILED: {}", ExceptionUtil.getFullStackTrace(ex));
    }
  }
}
