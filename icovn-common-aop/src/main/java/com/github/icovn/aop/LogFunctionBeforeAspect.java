package com.github.icovn.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogFunctionBeforeAspect {

  @Before("@annotation(com.github.icovn.aop.Logging)")
  public void beforeWebMethodExecution(JoinPoint joinPoint) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      String method = joinPoint.getSignature().getName();
      Object[] args = joinPoint.getArgs();
      log.info("USER_ACTIVITY, user {}, joinPoint {}, method {}, args {}", authentication.getName(),
          joinPoint, method, args);
    }
  }
}
