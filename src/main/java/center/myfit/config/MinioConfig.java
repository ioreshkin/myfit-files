package center.myfit.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Конфигурация клиента MinIO. */
@Configuration
@RequiredArgsConstructor
public class MinioConfig {
  private final MinioProperties config;

  /** Создание бина minioClient. */
  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(config.getUrl())
        .credentials(config.getAccessKey(), config.getSecretKey())
        .build();
  }
}
