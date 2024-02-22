package com.batsworks.batch.config.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerDoc {

    private static final String AUTH = "BearerAuth";
    @Value("${documentation.title}")
    private String title;
    @Value("${documentation.version}")
    private String version;
    @Value("${documentation.url}")
    private String url;
    @Value("${documentation.description}")
    private String description;
    @Value("${documentation.name}")
    private String name;
    @Value("${documentation.email}")
    private String email;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(meta())
                .addSecurityItem(new SecurityRequirement().addList(AUTH))
                .components(new Components().addSecuritySchemes(AUTH, new SecurityScheme()
                        .name(AUTH).type(SecurityScheme.Type.HTTP)
                        .scheme("bearer").bearerFormat("JWT")));
    }

    private Info meta() {
        return new Info().title(title).version(version).contact(new Contact().name(name).email(email).url(url))
                .description(description).termsOfService(null).license(null);
    }
}
