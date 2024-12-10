package center.myfit.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация клиента Rabbit.
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

  /**
   * Создание бина Rabbit.
   */
  @Bean
  public RabbitTemplate rabbitTemplate() {
    return new RabbitTemplate();
  }
}
