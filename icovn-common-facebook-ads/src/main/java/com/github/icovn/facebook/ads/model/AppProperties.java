package com.github.icovn.facebook.ads.model;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.facebookAds")
@Data
public class AppProperties {

  private List<AppInformation> apps;
}
