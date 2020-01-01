package com.github.icovn.try_spring_cloud_zoo_keeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LockListener implements DependencyWatcherListener {

  @Override
  public void stateChanged(String dependencyName, DependencyState newState) {
    log.info("()");
  }
}
