package center.myfit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


/** Конфигурация basic authentication.  */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /** Создание бина SecurityFilterChain. */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().permitAll() // Разрешаем доступ ко всем остальным запросам без проверки
        //сделал пока так потому что тут еще можно задать как проверяются запросы в общении
        // между сервисами, а у нас кейклоак и мне не ясно как он до конца реализован
        // например можно так .anyRequest().authenticated()
        //  Все остальные запросы требуют аутентификации
        //  ).oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Используем JWT для аутентификации
        // или
        // .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
        // .httpBasic(); // Включаем Basic Authentication
        );

    return http.build();
  }
}

