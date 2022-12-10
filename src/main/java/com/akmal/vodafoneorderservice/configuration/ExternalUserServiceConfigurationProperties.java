package com.akmal.vodafoneorderservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Entails set of configuration properties for contacting the external service to retrieve user information.
 */
@Configuration
@ConfigurationProperties(prefix = "app.user-service")
@Validated
@Getter
@Setter
public class ExternalUserServiceConfigurationProperties {
  @URL(message = "Valid apiUrl is required")
  private String apiUrl;
}
