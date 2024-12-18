package com.kuba.gymtrackerapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "GymTracker REST API",
                description = "REST API exposing several endpoints for tracking workouts",
                version = "1.0.0",
                contact = @Contact(
                        name = "Jakub Josef Krása",
                        email = "jjkrasa@gmail.com"
                )
        ),
        security = {
        @SecurityRequirement(
                name = "jwtAuth"
        )
})
@SecurityScheme(
        name = "jwtAuth",
        description = "JWT auth through access_token cookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "access_token"
)
public class OpenApiConfig {
}
