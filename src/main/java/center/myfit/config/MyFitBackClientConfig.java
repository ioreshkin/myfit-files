package center.myfit.config;

import center.myfit.exception.BadRequestException;
import center.myfit.exception.InternalServerException;
import center.myfit.exception.NotFoundException;
import center.myfit.starter.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Конфигурация для клиента myfit-back. */
@Configuration
@EnableFeignClients
@RequiredArgsConstructor
public class MyFitBackClientConfig {

  private final MyFitBackProperties config;

  /** Создание бина BasicAuthRequestInterceptor. */
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor(config.getUsername(), config.getPassword());
  }

  /** Создание бина ErrorDecoder. */
  @Bean
  public ErrorDecoder errorDecoder() {
    final ErrorDecoder errorDecoder = new ErrorDecoder.Default();
    return (s, response) -> {
      ErrorDto errorDto = null;
      try (InputStream is = response.body().asInputStream()) {
        errorDto = mapper().readValue(is, ErrorDto.class);
      } catch (IOException e) {
        throw new RuntimeException("Произошла непредвиденная ошибка!");
      }
      return switch (response.status()) {
        case 400 -> new BadRequestException(errorDto.message(), errorDto);
        case 404 -> new NotFoundException(errorDto.message(), errorDto);
        case 500 -> new InternalServerException(errorDto.message(), errorDto);
        default -> errorDecoder.decode(s, response);
      };
    };
  }

  /** Создание бина ObjectMapper. */
  @Bean
  public ObjectMapper mapper() {
    return (new ObjectMapper()).registerModule(new JavaTimeModule());
  }
}
