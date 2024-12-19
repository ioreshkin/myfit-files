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
  private Mobile mobile;
  private Desktop desktop;

  /** Mobile. */
  @Data
  public static class Mobile {
    private int width;
    private int height;

  }

  /** Desktop.  */
  @Data
  public static class Desktop {
    private int width;
    private int height;
  }
}
