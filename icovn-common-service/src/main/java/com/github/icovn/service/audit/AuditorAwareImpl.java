package com.github.icovn.service.audit;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String loggedName = "SYSTEM";
    if(SecurityContextHolder.getContext().getAuthentication() != null){
      loggedName =  SecurityContextHolder.getContext().getAuthentication().getName();
    }

    return Optional.of(loggedName);
  }

}
