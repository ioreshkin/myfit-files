package center.myfit.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация соединения с myfit-back. */
@Data
@Component
@ConfigurationProperties(prefix = "myfit-back")
public class MyFitBackProperties {
  @NotNull private String url;
  @NotNull private String username;
  @NotNull private String password;
}
