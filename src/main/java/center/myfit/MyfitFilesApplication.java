package center.myfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

/** Запуск SpringBoot приложения. */
@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients
public class MyfitFilesApplication {

  /** Запуск приложения. */
  public static void main(String[] args) {
    SpringApplication.run(MyfitFilesApplication.class, args);
  }
}
