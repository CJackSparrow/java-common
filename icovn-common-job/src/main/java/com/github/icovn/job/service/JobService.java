package com.github.icovn.job.service;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.github.icovn.job.model.JobCommon;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService {

  @Value("${icovn.job.default-group-name:icovn_job}")
  private String groupName;

  private final Scheduler scheduler;

  @Autowired
  public JobService(SchedulerFactoryBean schedulerFactory) {
    this.scheduler = schedulerFactory.getScheduler();
  }

  public void addJob(JobCommon process) throws SchedulerException {
    addJob(process, groupName);
  }
  public void addJob(JobCommon process, String group) throws SchedulerException {
    JobDetail job =
        newJob(process.getJobClass())
            .withIdentity(process.getId(), group)
            // http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/ConfigJDBCJobStoreClustering.html
            // https://stackoverflow.com/a/19270566/285571
            .requestRecovery(true)
            .build();

    Trigger trigger =
        newTrigger()
            .withIdentity(process.getId() + "-trigger", group)
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule(process.getCron()))
            .build();

    scheduler.scheduleJob(job, trigger);
  }

  public boolean deleteJob(String processId) throws SchedulerException {
    return deleteJob(processId, groupName);
  }
  public boolean deleteJob(String processId, String group) throws SchedulerException {
    JobKey jobKey = new JobKey(processId, group);
    return scheduler.deleteJob(jobKey);
  }

  public void updateJob(JobCommon process) throws SchedulerException {
    updateJob(process, groupName);
  }
  public void updateJob(JobCommon process, String group) throws SchedulerException {
    deleteJob(process.getId(), group);
    addJob(process, group);
  }

  public List<String> getJobs() throws SchedulerException {
    return getJobs(groupName);
  }
  public List<String> getJobs(String group) throws SchedulerException {
    return scheduler
        .getJobKeys(GroupMatcher.jobGroupEquals(group))
        .stream()
        .map(Key::getName)
        .sorted(Comparator.naturalOrder())
        .collect(Collectors.toList());
  }
}
