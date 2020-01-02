package com.github.icovn.job.service;

import com.github.icovn.job.model.JobCommon;
import java.util.List;
import org.quartz.SchedulerException;

public interface JobService {

  void addJob(JobCommon process) throws SchedulerException;

  void addJob(JobCommon process, String group) throws SchedulerException;

  void addJobsIfNotExist(List<JobCommon> processes) throws SchedulerException;

  void addJobsIfNotExist(List<JobCommon> processes, String group) throws SchedulerException;

  boolean deleteJob(String processId) throws SchedulerException;

  boolean deleteJob(String processId, String group) throws SchedulerException;

  boolean isExist(String processId) throws SchedulerException;

  boolean isExist(String processId, String group) throws SchedulerException;

  void updateJob(JobCommon process) throws SchedulerException;

  void updateJob(JobCommon process, String group) throws SchedulerException;

  List<String> getJobs() throws SchedulerException;

  List<String> getJobs(String group) throws SchedulerException;
}
