package center.myfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MyfitFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyfitFilesApplication.class, args);
    }

}
