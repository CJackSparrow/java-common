package com.github.icovn.try_common_service;

import com.github.icovn.service.repository.CustomRepository;
import com.github.icovn.service.service.BaseServiceSqlImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterServiceImpl extends BaseServiceSqlImpl<Chapter, Integer> implements ChapterService {

  @Autowired
  private ChapterRepository repository;

  @Override
  public CustomRepository<Chapter, Integer> getRepository() {
    return repository;
  }
}
