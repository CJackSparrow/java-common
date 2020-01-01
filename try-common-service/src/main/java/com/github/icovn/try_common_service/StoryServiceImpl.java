package com.github.icovn.try_common_service;

import com.github.icovn.service.repository.CustomRepository;
import com.github.icovn.service.service.BaseServiceSqlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoryServiceImpl extends BaseServiceSqlImpl<Story, String> implements StoryService {

  @Autowired
  private StoryRepository repository;

  @Override
  public CustomRepository<Story, String> getRepository() {
    return repository;
  }
}
