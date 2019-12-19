package com.github.icovn.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class TrackTimeAspect {

  @Around("@annotation(com.github.icovn.aop.TrackTime)")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();

    stopWatch.start();
    Object value = joinPoint.proceed();
    stopWatch.stop();

    log.info("TIME_TAKEN, by {} is {}", joinPoint, stopWatch.getTotalTimeMillis());

    return value;
  }
}
