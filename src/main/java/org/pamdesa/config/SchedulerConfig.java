package org.pamdesa.config;

import org.pamdesa.model.scheduler.TokenCleanupScheduler;
import org.pamdesa.service.ValidTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.cleanUpExpiredTokens", havingValue = "true")
public class SchedulerConfig {

  @Bean
  public TokenCleanupScheduler tokenCleanupScheduler(ValidTokenService validTokenService) {
    return new TokenCleanupScheduler(validTokenService);
  }

}
