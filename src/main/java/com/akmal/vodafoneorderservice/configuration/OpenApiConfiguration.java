package com.akmal.vodafoneorderservice.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
               .components(new Components())
               .info(new Info().title("Order Service API")
                         .description("The API reference of the order service")
                         .version("v0.0.1"));

  }
}
