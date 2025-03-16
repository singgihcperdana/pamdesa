package org.pamdesa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.pamdesa.model.constant.AppPath;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenApiCustomiser addHeaderBasedOnPath() {
    return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
      if (path.startsWith("/api/internal/") || path.startsWith("/api/customer/") || path.startsWith(
          "/api/secure/") || path.equals(AppPath.CURRENT)) {
        pathItem.readOperations().forEach(operation -> operation.addParametersItem(
            new HeaderParameter().name("token").description("token").example("Bearer xxxxxx")
                .required(true).schema(new StringSchema())));
      }
    });
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().info(new Info().title("pamdesa API").version("1.0"));
  }

}
