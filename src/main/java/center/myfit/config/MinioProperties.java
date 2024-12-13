package center.myfit.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация минио. */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
  @NotNull private String url;
  @NotNull private String accessKey;
  @NotNull private String secretKey;
  @NotNull private String bucketName;
  @NotNull private String publicFolder;
}
