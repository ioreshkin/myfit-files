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

  /** Exercise. */
  @Data
  public static class Exercise {
    private String image;
    private String imageToSave;
  }
}


