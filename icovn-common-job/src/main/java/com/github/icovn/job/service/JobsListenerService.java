package com.github.icovn.job.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobsListenerService implements JobListener {

  @Override
  public String getName() {
    return this.getClass().getName();
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
    log.info("Job to be executed: " + jobExecutionContext.getJobDetail().getKey().getName());
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
    log.info("Job execution vetoed: " + jobExecutionContext.getJobDetail().getKey().getName());
  }

  @Override
  public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
    log.info("Job was executed: " + jobExecutionContext.getJobDetail().getKey().getName() + (e != null ? ", with error" : ""));
  }
}
