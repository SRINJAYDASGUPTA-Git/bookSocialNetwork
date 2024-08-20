package com.srinjay.book_network.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Srinjay Das Gupta",
                        email = "dasguptasrinjay2004@gmail.com",
                        url="https://www.srinjaydg.in/"
                ),
                title = "Book Network API",
                version = "1.0.0",
                description = "API for managing books and feedbacks",
                license = @License(
                        name="MIT License",
                        url="https://opensource.org/licenses/MIT"
                ),
                termsOfService = "https://www.srinjaydg.in/"
        ),
        servers = {
                @Server(
                        description = "Local Server",
                        url = "http://localhost:8088/api/v1"
                ),
                @Server(
                        description = "Production Server",
                        url = "http://13.61.27.122:8088/api/v1"
                )
        },
        security = {
                @SecurityRequirement (
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme (
        name = "bearerAuth",
        description = "JWT Auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
