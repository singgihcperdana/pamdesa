package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.properties.VersionProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VersionController implements InitializingBean {

  private final VersionProperties properties;

  private String maven;

  @Override
  public void afterPropertiesSet() {
    maven = "maven.groupId=" + properties.getGroupId() + "\n" +
      "maven.artifactId=" + properties.getArtifactId() + "\n" +
      "maven.pom.version=" + properties.getVersion() + "\n" +
      "maven.build.time=" + properties.getBuildTime() + "\n";
  }

  @RequestMapping(value = AppPath.VERSION, produces = MediaType.TEXT_PLAIN_VALUE)
  public String version() {
    return maven;
  }
}
