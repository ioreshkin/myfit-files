package center.myfit;

import center.myfit.starter.test.AbstractTestResourcePool;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class ResourcePool extends AbstractTestResourcePool {
  public static final MockMultipartFile defaultExercisePart;
  public static final MockMultipartFile defaultExercisePartWithId;
  public static final MockMultipartFile defaultWorkoutPart;
  public static final MockMultipartFile defaultImage;
  public static final MockMultipartFile onlyDescriptionExercisePart;
  public static final MockMultipartFile onlyDescriptionWorkoutPart;

  public static final Resource defaultError =
      new ClassPathResource("json/exercise/default-error.json");
  // exercise
  public static final Resource defaultExercise =
      new ClassPathResource("json/exercise/default-exercise.json");
  public static final Resource defaultExerciseWithId =
      new ClassPathResource("json/exercise/default-exercise-with-id.json");
  public static final Resource defaultExerciseKeycloakIdWithId =
          new ClassPathResource("json/exercise/default-exercise-keycloak-id-with-id.json");
  public static final Resource defaultExerciseKeycloakId =
      new ClassPathResource("json/exercise/default-exercise_keycloakId.json");
  public static final Resource savedExerciseOriginal =
      new ClassPathResource("json/exercise/saved-exercise_original.json");
  public static final Resource sameExerciseOriginal =
          new ClassPathResource("json/exercise/same-exercise_original.json");
  public static final Resource savedExercise =
      new ClassPathResource("json/exercise/saved-exercise.json");
  public static final Resource image = new ClassPathResource("image/img.png");
  public static final Resource onlyDescriptionExercise =
      new ClassPathResource("json/exercise/only-description-exercise.json");

  // workout
  public static final Resource savedWorkoutOriginal =
      new ClassPathResource("json/workout/saved-workout-original.json");
  public static final Resource defaultWorkout =
      new ClassPathResource("json/workout/default-workout.json");
  public static final Resource defaultWorkoutKeycloakId =
      new ClassPathResource("json/workout/default-workout-keycloak-id.json");
  public static final Resource onlyDescriptionWorkout =
      new ClassPathResource("json/workout/only-description-workout.json");
  public static final Resource savedWorkout =
      new ClassPathResource("json/workout/saved-workout.json");

  // image
  public static final Resource defaultExerciseImageTask =
      new ClassPathResource("json/image/default-exercise-image-task.json");
  public static final Resource defaultWorkoutImageTask =
      new ClassPathResource("json/image/default-workout-image-task.json");
  public static final Resource expectedExerciseImageDto =
      new ClassPathResource("json/image/expected-exercise-image-dto.json");
  public static final Resource expectedWorkoutImageDto =
      new ClassPathResource("json/image/expected-workout-image-dto.json");
  public static final Resource workoutGifImageTask =
      new ClassPathResource("json/image/workout-gif-image-task.json");
  public static final Resource exerciseGifImageTask =
      new ClassPathResource("json/image/exercise-gif-image-task.json");

  static {
    try {
      defaultImage =
          new MockMultipartFile(
              "file", "img.png", MediaType.IMAGE_PNG_VALUE, image.getInputStream());
      defaultExercisePart =
          new MockMultipartFile(
              "dto", "", MediaType.APPLICATION_JSON_VALUE, defaultExercise.getInputStream());
      defaultExercisePartWithId =
          new MockMultipartFile(
              "dto", "", MediaType.APPLICATION_JSON_VALUE, defaultExerciseWithId.getInputStream());
      onlyDescriptionExercisePart =
          new MockMultipartFile(
              "dto",
              "",
              MediaType.APPLICATION_JSON_VALUE,
              onlyDescriptionExercise.getInputStream());
      defaultWorkoutPart =
          new MockMultipartFile(
              "dto", "", MediaType.APPLICATION_JSON_VALUE, defaultWorkout.getInputStream());
      onlyDescriptionWorkoutPart =
          new MockMultipartFile(
              "dto", "", MediaType.APPLICATION_JSON_VALUE, onlyDescriptionWorkout.getInputStream());
    } catch (Exception e) {
      throw new RuntimeException("Не удалось инициализировать ресурсы!");
    }
  }
}
