package com.github.icovn.try_common_service;

import com.github.icovn.service.repository.CustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends CustomRepository<Story, String> {

}
