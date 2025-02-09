package center.myfit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Настройки технической учетной записи. */
@Data
@Component
@ConfigurationProperties(prefix = "tuz")
public class TuzProperties {
  private String username;
  private String password;
}
