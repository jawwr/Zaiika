package com.project.zaiika.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
    @Value("${app.version}")
    private String version;

    @Bean
    public OpenAPI baseOpenApi() {
        ApiResponse permissionDenied = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{ \"message\" : \"Permission denied\" }")
                        )
                )
        );

        ApiResponse jwtResponse = new ApiResponse().content(
                new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("{ \"token\" : \"some token\" }")
                        )
                )
        );

        Components components = new Components();
        components.addResponses("permissionDenied", permissionDenied);
        components.addResponses("jwtResponse", jwtResponse);

        return new OpenAPI()
                .components(components)
                .info(new Info()
                        .title("Zaiika API Docs")
                        .version(version)
                        .description("APi Docs")
                );
    }
}
