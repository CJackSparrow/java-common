package com.github.icovn.job;

import com.github.icovn.job.config.EnableCommonJob;
import com.github.icovn.job.model.JobCommon;
import com.github.icovn.job.service.JobService;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCommonJob
@Slf4j
@SpringBootApplication
public class TestCommonJobApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(TestCommonJobApplication.class, args);
  }

  @Autowired
  private JobService jobService;

  @Override
  public void run(String... args) throws Exception {
    jobService.addJobsIfNotExist(Arrays.asList(
        JobCommon.of("every minutes", "0 0/1 * 1/1 * ? *", EchoJob.class),
        JobCommon.of("hourly", "0 0 0/1 1/1 * ? *", EchoJob.class)
    ));

    List<String> jobs = jobService.getJobs();
    log.info("jobs: {}", jobs);
  }
}
