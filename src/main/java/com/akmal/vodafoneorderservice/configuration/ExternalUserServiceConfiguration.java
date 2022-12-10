package com.akmal.vodafoneorderservice.configuration;

import com.akmal.vodafoneorderservice.repository.UserRepository;
import com.akmal.vodafoneorderservice.repository.implementation.InMemoryCachingUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

/**
 * Externalised configuration of a client and a repository for the user management
 */
@Configuration
@RequiredArgsConstructor
public class ExternalUserServiceConfiguration {

  private final ExternalUserServiceConfigurationProperties configurationProperties;

  @Bean("userServiceRestTemplate")
  public RestTemplate userServiceRestTemplate() {
    return new RestTemplateBuilder().rootUri(configurationProperties.getApiUrl())
               .build();
  }

  @Bean
  @DependsOn("userServiceRestTemplate")
  public UserRepository userRepository(@Qualifier("userServiceRestTemplate") RestTemplate restTemplate) {
    return new InMemoryCachingUserRepository(restTemplate);
  }
}
