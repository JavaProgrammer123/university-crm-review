package com.example.microservice.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

//Swagger http://localhost:8080/swagger-ui/index.html#/

@OpenAPIDefinition(
        info = @Info(title = "Reports API", version = "1.0", description = "Reports")
)
@Configuration
public class SwaggerConfig {
}
