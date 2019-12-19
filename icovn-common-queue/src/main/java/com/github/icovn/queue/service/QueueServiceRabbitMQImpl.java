package com.github.icovn.queue.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueServiceRabbitMQImpl implements QueueService {

  private final RabbitTemplate template;
  private final RabbitTemplate templateSimple;

  public QueueServiceRabbitMQImpl(
      RabbitTemplate template, RabbitTemplate templateSimple) {
    this.template = template;
    this.templateSimple = templateSimple;
  }

  @Override
  public void push(String message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    templateSimple.convertAndSend(queue, message);
  }

  @Override
  public void push(Object message, String queue) {
    log.info("(push)message: {}, queue: {}", message, queue);
    template.convertAndSend(queue, message);
  }

  /**
   *
   * @param messages
   * @param queue
   */
  @Override
  public void push(List<Object> messages, String queue) {
    log.info("(push)queue: {}, messages: {}", queue, messages.size());
    for (Object message : messages) {
      template.convertAndSend(queue, message);
    }
  }
}
