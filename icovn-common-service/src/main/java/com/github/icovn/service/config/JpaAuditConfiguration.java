package com.github.icovn.service.config;

import com.github.icovn.service.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfiguration {

  @Bean
  public AuditorAwareImpl auditorAware() {
    return new AuditorAwareImpl();
  }
}
