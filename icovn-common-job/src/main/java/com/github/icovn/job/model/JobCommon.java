package com.github.icovn.job.model;

import lombok.Data;
import org.quartz.Job;

@Data
public class JobCommon {

  private String id;
  private String cron;

  private Class<? extends Job> jobClass;
}
