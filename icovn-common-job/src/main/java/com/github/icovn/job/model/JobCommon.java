package com.github.icovn.job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.quartz.Job;

@AllArgsConstructor(staticName = "of")
@Data
public class JobCommon {

  private String id;
  private String cron;

  private Class<? extends Job> jobClass;
}
