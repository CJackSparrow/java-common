package com.github.icovn.util;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtil {

  public static long getCurrentTime() {
    Instant instant = Instant.now();
    log.info("(getCurrentTime)now: {}", instant.getEpochSecond());
    return instant.getEpochSecond() * 1000;
  }
}
