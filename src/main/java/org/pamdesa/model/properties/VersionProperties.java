package org.pamdesa.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties("version")
public class VersionProperties {

  private String groupId;

  private String artifactId;

  private String version;

  private String buildTime;

}
