package com.github.icovn.try_spring_cloud_zoo_keeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TrySpringCloudZooKeeperApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(TrySpringCloudZooKeeperApplication.class, args);
  }

  @Autowired
  private ZooKeeperDataService zooKeeperDataService;

  @Override
  public void run(String... args) throws Exception {
    zooKeeperDataService.set("room", "abc999");
    String room = (String)zooKeeperDataService.get("room");
    String namespace = zooKeeperDataService.getNamespace();
    log.info("namespace: {}, room: {}", namespace, room);
  }
}
