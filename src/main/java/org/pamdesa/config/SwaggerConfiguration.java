package org.pamdesa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenApiCustomiser addHeaderBasedOnPath() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            if (path.startsWith("/api/internal/") || path.startsWith("/api/customer/")
                    || path.startsWith("/api/secure/") || path.equals("/api/auth/logout")
                    || path.equals("/api/auth/current")) {
                pathItem.readOperations().forEach(operation -> {
                    operation.addParametersItem(new HeaderParameter()
                            .name("token")
                            .description("token")
                            .example("Bearer xxxxxx")
                            .required(true)
                            .schema(new io.swagger.v3.oas.models.media.StringSchema()));
                });
            }
        });
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("pamdesa API")
                        .version("1.0"));
    }

}
