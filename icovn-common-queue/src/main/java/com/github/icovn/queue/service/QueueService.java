package com.github.icovn.queue.service;

import java.util.List;

public interface QueueService {

  void push(String message, String queue);

  void push(Object message, String queue);

  void push(List<Object> messages, String queue);
}
