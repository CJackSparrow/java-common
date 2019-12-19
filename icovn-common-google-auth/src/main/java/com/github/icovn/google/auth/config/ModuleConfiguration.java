package com.github.icovn.google.auth.config;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.github.icovn.google.auth.service"
})
@Slf4j
public class ModuleConfiguration {

  @PostConstruct
  public void postConstruct() {
    log.info("init com.github.icovn.google.auth.config");
  }
}
