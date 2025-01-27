package center.myfit.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Конфигурация клиента Rabbit. */
@Configuration
@EnableRabbit
public class RabbitConfig {

  /**  Создание Jackson2JsonMessageConverter. */
  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }
}
