package com.github.icovn.google.ads.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.icovn.google.ads.service")
@Slf4j
public class ModuleConfiguration {

  @PostConstruct
  public void init() {
    log.info("init com.github.icovn.google.ads.config success");
  }
}
