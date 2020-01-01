package com.github.icovn.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@Slf4j
public class SchedulingMultipleThreadConfig implements SchedulingConfigurer {

  @Value("${application.jog.scheduling.pool-size:100}")
  private int poolSize;

  @Override
  public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
    log.info("(configureTasks)set pool size for scheduling");
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(poolSize);
    taskScheduler.initialize();
    scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
  }
}
