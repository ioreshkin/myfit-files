package center.myfit.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//это нужна настройка? пока тут базово, може надо что то докрутить?
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi publicExerciseApi() {
    return GroupedOpenApi.builder()
        .group("exercise")
        .pathsToMatch("/exercise/**")
        .build();
  }


  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Your API Title")
            .version("1.0")
            .description("Your API Description")
            .license(new License().name("Apache 2.0").url("http://springdoc.org")));
  }
}

