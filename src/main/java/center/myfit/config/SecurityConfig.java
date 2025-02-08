package center.myfit.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/** Настройки безопасности приложения. */
@Configuration
@Slf4j
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final TuzProperties properties;

  /** Создание UserDetailsService с существующими пользователями. */
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails technoUser =
        User.withDefaultPasswordEncoder()
            .username(properties.getUsername())
            .password(properties.getPassword())
            .roles("TUZ")
            .build();
    return new InMemoryUserDetailsManager(technoUser);
  }
}
