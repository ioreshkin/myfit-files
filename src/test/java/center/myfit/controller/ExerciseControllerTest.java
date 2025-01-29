package center.myfit.controller;

import static center.myfit.ResourcePool.*;
import static center.myfit.util.TestConstants.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import center.myfit.BaseIntegrationTest;
import center.myfit.config.MyFitBackProperties;
import center.myfit.config.QueueProperties;
import center.myfit.starter.dto.ImageTaskDto;
import center.myfit.starter.test.WithMockUser;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

class ExerciseControllerTest extends BaseIntegrationTest {

  public static final RequestPostProcessor POST =
      request -> {
        request.setMethod("POST");
        return request;
      };
  public static final ImageTaskDto TASK_DTO =
      new ImageTaskDto(1L, new ImageTaskDto.ImageDto("public/123.jpg"));
  private static final String BASE_URL = "/api/files/exercise";
  private static final String BASE_URLBACK = "/api/back/exercise";

  @Value("${stage}")
  private String stage;

  @Autowired MyFitBackProperties config;
  @Autowired QueueProperties queueConfig;

  ExerciseControllerTest(WebApplicationContext context) {
    super(context);
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturnEnrichedDtoWithOriginalLink() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(eq(stage), eq(stage+ "_" +
            queueConfig.getExercise().getImageToConvert()), eq(TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(
                matchingJsonPath(
                    "$.[?(@.title == 'exercise title' " + "&& @.keycloakId == '123')]"))
            .withRequestBody(matchingJsonPath("$.image.original"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(getString(savedExerciseOriginal))
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultImage).file(defaultExercisePart).with(POST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.keycloakId", is("123")))
        .andExpect(jsonPath("$.title", is("exercise title")))
        .andExpect(jsonPath("$.description", is("exercise description")))
        .andExpect(jsonPath("$.videoUrl", is("https://you.tube/abc")))
        .andExpect(jsonPath("$.image.original", is("public/123.jpg")));

    verify(rabbitTemplate, times(1)).convertAndSend(stage, stage+ "_" +
            queueConfig.getExercise().getImageToConvert(), TASK_DTO);
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturnEnrichedDto() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(eq(stage), eq(stage+ "_" +
        queueConfig.getExercise().getImageToConvert()), eq(TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(equalToJson(getString(defaultExerciseKeycloakId)))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(getString(savedExercise))
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultExercisePart).with(POST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.keycloakId", is("123")))
        .andExpect(jsonPath("$.title", is("exercise title")))
        .andExpect(jsonPath("$.description", is("exercise description")))
        .andExpect(jsonPath("$.videoUrl", is("https://you.tube/abc")))
        .andExpect(jsonPath("$.image", Matchers.nullValue()));

    verify(rabbitTemplate, never())
        .convertAndSend(eq(stage), eq(stage+ "_" +
            queueConfig.getExercise().getImageToConvert()), eq(TASK_DTO));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturn400_whenBackReturn400() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(eq(stage), eq(stage + "_" + queueConfig.getExercise().getImageToConvert()),
            eq(TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(equalToJson(getString(defaultExerciseKeycloakId)))
            .willReturn(aResponse().withStatus(400).withBody(getString(defaultError))));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultExercisePart).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("error message")));

    verify(rabbitTemplate, never())
        .convertAndSend(eq(stage), eq(stage + "_" + queueConfig.getExercise().getImageToConvert()),
            eq(TASK_DTO));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturn500_whenUploadFail() throws Exception {
    when(minioClient.putObject(any())).thenThrow(new IOException());
    mockMvc
        .perform(multipart(BASE_URL).file(defaultExercisePart).file(defaultImage).with(POST))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message", is("Ошибка загрузки файла")));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturn400_whenNoDtoPresent() throws Exception {
    when(minioClient.putObject(any())).thenThrow(new IOException());
    mockMvc
        .perform(multipart(BASE_URL).file(defaultImage).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Не заполнены обязательные поля формы!")));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createExercise_shouldReturn400_whenNoTitlePresent() throws Exception {
    when(minioClient.putObject(any())).thenThrow(new IOException());
    mockMvc
        .perform(multipart(BASE_URL).file(onlyDescriptionExercisePart).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Заголовок не может быть пустым")));
  }
}
