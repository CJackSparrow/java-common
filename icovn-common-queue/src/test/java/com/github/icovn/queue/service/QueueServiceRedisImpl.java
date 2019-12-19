package com.github.icovn.queue.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class QueueServiceRedisImpl implements QueueService {

  protected RedisTemplate<String, Object> template;

  @Override
  public void push(String message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    template.convertAndSend(queue, message);
  }

  @Override
  public void push(Object message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    template.convertAndSend(queue, message);
  }

  @Override
  public void push(List<Object> messages, String queue) {
    log.info("(push)queue: {}, messages: {}", queue, messages.size());
    for (Object message : messages) {
      template.convertAndSend(queue, message);
    }
  }
}
