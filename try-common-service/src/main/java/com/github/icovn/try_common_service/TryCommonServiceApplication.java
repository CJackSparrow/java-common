package com.github.icovn.try_common_service;

import com.github.icovn.service.repository.CustomRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
    basePackages = "com.github.icovn.try_common_service",
    repositoryBaseClass = CustomRepositoryImpl.class)
@EntityScan("com.github.icovn.try_common_service")
@Slf4j
@SpringBootApplication
public class TryCommonServiceApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(TryCommonServiceApplication.class, args);
  }

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private StoryService storyService;

  @Override
  public void run(String... args) throws Exception {
    Chapter chapter = Chapter.of("Chapter 233: Test", 2);
    chapterService.create(chapter);

    Story story = Story.of("Jindo");
    storyService.create(story);

    Page<Story> stories = storyService.find(PageRequest.of(0, 100));
    log.info("number of stories: {}", stories.getTotalElements());
  }
}
