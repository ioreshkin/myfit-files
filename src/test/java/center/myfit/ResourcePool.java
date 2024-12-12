package center.myfit;

import center.myfit.starter.test.AbstractTestResourcePool;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class ResourcePool extends AbstractTestResourcePool {
  public static final MockMultipartFile defaultExercisePart;
  public static final MockMultipartFile defaultImage;

  public static final Resource defaultError =
      new ClassPathResource("json/exercise/default-error.json");
  public static final Resource defaultExercise =
      new ClassPathResource("json/exercise/default-exercise.json");
  public static final Resource defaultExerciseKeycloakId =
      new ClassPathResource("json/exercise/default-exercise_keycloakId.json");
  public static final Resource defaultExerciseKeycloakIdOriginal =
      new ClassPathResource("json/exercise/default-exercise_keycloakId_original.json");
  public static final Resource savedExerciseOriginal =
      new ClassPathResource("json/exercise/saved-exercise_original.json");
  public static final Resource savedExercise =
      new ClassPathResource("json/exercise/saved-exercise.json");
  public static final Resource image = new ClassPathResource("image/img.png");

  static {
    try {
      defaultImage =
          new MockMultipartFile(
              "file", "img.png", MediaType.IMAGE_PNG_VALUE, image.getInputStream());
      defaultExercisePart =
          new MockMultipartFile(
              "dto", "", MediaType.APPLICATION_JSON_VALUE, defaultExercise.getInputStream());
    } catch (Exception e) {
      throw new RuntimeException("Не удалось инициализировать ресурсы!");
    }
  }
}
