package com.camelcase.taskapi.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "API with JWT Authentication", version = "1.0"),
    servers = @Server(url = "http://localhost:8080"),
    security = @SecurityRequirement(name = "OAuth2")
)
@SecurityScheme(
    name = "OAuth2",
    type = SecuritySchemeType.OAUTH2,
    scheme = "bearer",
    bearerFormat = "JWT",
    flows = @OAuthFlows(
        password = @OAuthFlow(
            tokenUrl = "http://localhost:8080/api/auth/login",
            scopes = {}
        )
    )
)

public class OpenApiConfig {
}