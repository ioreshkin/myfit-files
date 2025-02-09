package center.myfit.controller;

import static center.myfit.ResourcePool.defaultError;
import static center.myfit.ResourcePool.defaultImage;
import static center.myfit.ResourcePool.defaultWorkoutKeycloakId;
import static center.myfit.ResourcePool.defaultWorkoutPart;
import static center.myfit.ResourcePool.onlyDescriptionWorkoutPart;
import static center.myfit.ResourcePool.savedWorkout;
import static center.myfit.ResourcePool.savedWorkoutOriginal;
import static center.myfit.starter.test.AbstractTestResourcePool.getString;
import static center.myfit.util.TestConstants.KEYCLOAK_ID;
import static center.myfit.util.TestConstants.POST;
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
import center.myfit.starter.enums.EntityType;
import center.myfit.starter.test.WithMockUser;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;

public class WorkoutControllerTest extends BaseIntegrationTest {

  private static final ImageTaskDto WORKOUT_IMAGE_TASK_DTO =
      new ImageTaskDto(1L, EntityType.WORKOUT, new ImageTaskDto.ImageDto("public/123.jpg"));
  private static final String BASE_URL = "/api/files/workout";
  private static final String BASE_URLBACK = "/api/back/workout";

  @Value("${stage}")
  private String stage;

  @Autowired MyFitBackProperties config;
  @Autowired QueueProperties queueConfig;

  protected WorkoutControllerTest(WebApplicationContext context) {
    super(context);
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturnEnrichedDtoWithOriginalLink() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToConvert()),
            eq(WORKOUT_IMAGE_TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(
                matchingJsonPath("$.[?(@.title == 'workout title' " + "&& @.keycloakId == '123')]"))
            .withRequestBody(matchingJsonPath("$.image.original"))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(getString(savedWorkoutOriginal))
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultImage).file(defaultWorkoutPart).with(POST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.keycloakId", is("123")))
        .andExpect(jsonPath("$.title", is("workout title")))
        .andExpect(jsonPath("$.description", is("workout description")))
        .andExpect(jsonPath("$.image.original", is("public/123.jpg")));

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            stage,
            stage + "_" + queueConfig.getWorkout().getImageToConvert(),
            WORKOUT_IMAGE_TASK_DTO);
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturnEnrichedDto() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToConvert()),
            eq(WORKOUT_IMAGE_TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(equalToJson(getString(defaultWorkoutKeycloakId)))
            .willReturn(
                aResponse()
                    .withStatus(200)
                    .withBody(getString(savedWorkout))
                    .withHeader("Content-Type", APPLICATION_JSON_VALUE)));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultWorkoutPart).with(POST))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.keycloakId", is("123")))
        .andExpect(jsonPath("$.title", is("workout title")))
        .andExpect(jsonPath("$.description", is("workout description")))
        .andExpect(jsonPath("$.image", Matchers.nullValue()));

    verify(rabbitTemplate, never())
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToConvert()),
            eq(WORKOUT_IMAGE_TASK_DTO));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturn400_whenBackReturn400() throws Exception {
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToConvert()),
            eq(WORKOUT_IMAGE_TASK_DTO));

    stubFor(
        WireMock.post(urlEqualTo(BASE_URLBACK))
            .withBasicAuth(config.getUsername(), config.getPassword())
            .withRequestBody(equalToJson(getString(defaultWorkoutKeycloakId)))
            .willReturn(aResponse().withStatus(400).withBody(getString(defaultError))));

    mockMvc
        .perform(multipart(BASE_URL).file(defaultWorkoutPart).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("error message")));

    verify(rabbitTemplate, never())
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToConvert()),
            eq(WORKOUT_IMAGE_TASK_DTO));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturn500_whenUploadFail() throws Exception {
    when(minioClient.putObject(any())).thenThrow(new IOException());
    mockMvc
        .perform(multipart(BASE_URL).file(defaultWorkoutPart).file(defaultImage).with(POST))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message", is("Ошибка загрузки файла")));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturn400_whenNoDtoPresent() throws Exception {
    when(minioClient.putObject(any())).thenThrow(new IOException());
    mockMvc
        .perform(multipart(BASE_URL).file(defaultImage).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Не заполнены обязательные поля формы!")));
  }

  @Test
  @WithMockUser(keycloakId = KEYCLOAK_ID)
  void createWorkout_shouldReturn400_whenNoTitlePresent() throws Exception {
    mockMvc
        .perform(multipart(BASE_URL).file(onlyDescriptionWorkoutPart).with(POST))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Заголовок не может быть пустым")));
  }
}
