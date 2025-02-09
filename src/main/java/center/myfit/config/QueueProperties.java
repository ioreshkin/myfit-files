package center.myfit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Конфигурация очередей. */
@Data
@Component
@ConfigurationProperties(prefix = "queue")
public class QueueProperties {
  /** Exercise. */
  private Exercise exercise;

  /** Workout. */
  private Workout workout;

  /** Exercise. */
  @Data
  public static class Exercise {
    private String imageToConvert;
    private String imageToSave;
  }

  /** Workout. */
  @Data
  public static class Workout {
    private String imageToConvert;
    private String imageToSave;
  }
}
