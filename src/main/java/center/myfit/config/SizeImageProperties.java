package center.myfit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация размеров. */
@Data
@Component
@ConfigurationProperties(prefix = "image-size")
public class SizeImageProperties {

  public Exercise exercise;
  public Workout workout;

  /** Exercise image-size. */
  @Data
  public static class Exercise {
    private Size mobile;
    private Size desktop;
  }

  /** Workout image-size. */
  @Data
  public static class Workout {
    private Size mobile;
    private Size desktop;
  }

  /** Size. */
  @Data
  public static class Size {
    private int width;
    private int height;
  }
}
