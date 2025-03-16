package org.pamdesa;

import org.pamdesa.model.properties.AccessRulesProperties;
import org.pamdesa.model.properties.VersionProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({AccessRulesProperties.class, VersionProperties.class})
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}