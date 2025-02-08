package center.myfit.listener;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import center.myfit.BaseIntegrationTest;
import center.myfit.config.QueueProperties;
import center.myfit.exception.ConvertException;
import center.myfit.exception.DownloadException;
import center.myfit.exception.UploadException;
import center.myfit.starter.dto.ExerciseImageDto;
import center.myfit.starter.dto.ImageTaskDto;
import center.myfit.starter.enums.EntityType;
import io.minio.GetObjectResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;

class ImageTaskListenerTest extends BaseIntegrationTest {

  @Value("${stage}")
  private String stage;

  @Autowired QueueProperties queueConfig;
  @Autowired ImageTaskListener imageTaskListener;

  // это тоже в пул ресурсов?
  public static final ImageTaskDto EXERCISE_IMAGE_TASK_DTO =
      new ImageTaskDto(1L, EntityType.EXERCISE, new ImageTaskDto.ImageDto("public/123.jpg"));

  public static final ExerciseImageDto EXERCISE_IMAGE_DTO =
      new ExerciseImageDto(
          1L, new ExerciseImageDto.ImageDto("original.jpg", "mobile.jpg", "desctop.jpg"));

  ImageTaskListenerTest(WebApplicationContext context) {
    super(context);
  }

  @Test
  void convertImage_shouldThrow_whenDownloadFiled() throws Exception {
    when(minioClient.getObject(any())).thenThrow(new RuntimeException());

    assertThrows(
        DownloadException.class, () -> imageTaskListener.convertImage(EXERCISE_IMAGE_TASK_DTO));

    verify(rabbitTemplate, never())
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            eq(EXERCISE_IMAGE_TASK_DTO));
    ;
  }

  @Test
  void convertImage_shouldThrow_whenConvertFiled() throws Exception {

    GetObjectResponse invalidInputStream =
        new GetObjectResponse(null, null, null, null, new ByteArrayInputStream("a".getBytes()));

    when(minioClient.getObject(any())).thenReturn(invalidInputStream);

    assertThrows(
        ConvertException.class,
        () -> {
          imageTaskListener.convertImage(EXERCISE_IMAGE_TASK_DTO);
        });

    verify(rabbitTemplate, never())
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            eq(EXERCISE_IMAGE_TASK_DTO));
  }

  @Test
  void convertImage_shouldThrow_whenUploadFiled() throws Exception {

    GetObjectResponse imageStream =
        new GetObjectResponse(
            null, null, null, null, new FileInputStream("src/test/resources/image/img.png"));

    when(minioClient.getObject(any())).thenReturn(imageStream);
    when(minioClient.putObject(any())).thenThrow(new IOException());

    assertThrows(
        UploadException.class,
        () -> {
          imageTaskListener.convertImage(EXERCISE_IMAGE_TASK_DTO);
        });

    verify(rabbitTemplate, never())
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            eq(EXERCISE_IMAGE_TASK_DTO));
  }

  @Test
  void convertImage_shouldSuccess() throws Exception {
    GetObjectResponse imageStream =
        new GetObjectResponse(
            null, null, null, null, new FileInputStream("src/test/resources/image/img.png"));

    when(minioClient.getObject(any())).thenReturn(imageStream);
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            eq(EXERCISE_IMAGE_DTO));

    imageTaskListener.convertImage(EXERCISE_IMAGE_TASK_DTO);

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            any(ExerciseImageDto.class));
  }
}
