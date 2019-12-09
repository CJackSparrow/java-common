package com.github.icovn.try_custom_repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TryCustomRepositoryApplication {

  public static void main(String[] args) {
    SpringApplication.run(TryCustomRepositoryApplication.class, args);
  }
}
