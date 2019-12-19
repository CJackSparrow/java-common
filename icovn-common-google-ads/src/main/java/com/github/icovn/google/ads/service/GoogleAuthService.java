package com.github.icovn.google.ads.service;

import static com.google.api.ads.common.lib.utils.Builder.DEFAULT_CONFIGURATION_FILENAME;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GoogleAuthService {

  @Value("${api.adwords.refreshToken}")
  private String refreshToken;

  @Value("${api.adwords.clientId}")
  private String clientId;

  @Value("${api.adwords.clientSecret}")
  private String clientSecret;

  @Value("${api.adwords.clientCustomerId}")
  private String clientCustomerId;

  @Value("${api.adwords.developerToken}")
  private String developerToken;

  @Value("${api.adwords.isPartialFailure}")
  private Boolean isPartialFailure;

  public AdWordsSession getAdWordsSession() {
    return getAdWordsSession(clientCustomerId);
  }

  public AdWordsSession getAdWordsSession(String accountId) {
    AdWordsSession session;
    try {
      // Generate a refreshable OAuth2 credential.
      Credential oAuth2Credential =
          new OfflineCredentials.Builder()
              .forApi(OfflineCredentials.Api.ADWORDS)
              .from(initOauthConfiguration())
              .build()
              .generateCredential();

      // Construct an AdWordsSession.
      session =
          new AdWordsSession.Builder()
              .from(initAdWordsConfiguration(accountId))
              .withOAuth2Credential(oAuth2Credential)
              .build();
    } catch (ValidationException ve) {
      log.error(
          "Invalid configuration in the {} file. Exception: {}",
          DEFAULT_CONFIGURATION_FILENAME,
          ve);
      return null;
    } catch (OAuthException oe) {
      log.error(
          "Failed to create OAuth credentials. Check OAuth settings in the {} file. "
              + "Exception: {}",
          DEFAULT_CONFIGURATION_FILENAME,
          oe);
      return null;
    }

    return session;
  }

  public AdWordsServicesInterface getAdWordsService() {
    AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();
    return adWordsServices;
  }

  private Configuration initAdWordsConfiguration(String accountId) {
    Configuration config = new BaseConfiguration();
    config.addProperty("api.adwords.clientCustomerId", accountId);
    config.addProperty("api.adwords.developerToken", developerToken);
    config.addProperty("api.adwords.isPartialFailure", isPartialFailure);

    return config;
  }

  private Configuration initOauthConfiguration() {
    Configuration config = new BaseConfiguration();
    config.addProperty("api.adwords.refreshToken", refreshToken);
    config.setProperty("api.adwords.clientId", clientId);
    config.addProperty("api.adwords.clientSecret", clientSecret);
    return config;
  }
}
