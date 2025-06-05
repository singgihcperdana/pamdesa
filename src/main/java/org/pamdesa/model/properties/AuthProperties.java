package org.pamdesa.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@Data
public class AuthProperties {

  private String secretKey;

  private Long tokenExpirationTime;

}
