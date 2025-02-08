package center.myfit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация размеров. */
@Data
@Component
@ConfigurationProperties(prefix = "image-size.exercise")
public class SizeImageProperties {

  /** image-size. */
  private Size mobile;

  private Size desktop;

  /** Size. */
  @Data
  public static class Size {
    private int width;
    private int height;
  }
}
