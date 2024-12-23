package center.myfit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация размеров. */
@Data
@Component
@ConfigurationProperties(prefix = "image-size")
public class SizeImageProperties {

  /** image-size. */
  private Size size;

  /** Size. */
  @Data
  public static class Size {
    private int mobileWidth;
    private int mobileHeight;
    private int desktopWidth;
    private int desktopHeight;
  }
}
