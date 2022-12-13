package com.demo.api.configuration;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("spring.security.user")
public class BasicAuthorizationProperties {
  private String name;
  private String password;
  private List<String> roles;
}
