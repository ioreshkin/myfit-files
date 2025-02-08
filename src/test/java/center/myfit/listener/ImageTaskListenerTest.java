package center.myfit.listener;

import static center.myfit.ResourcePool.*;
import static center.myfit.starter.test.AbstractTestResourcePool.read;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import center.myfit.starter.dto.WorkoutImageDto;
import io.minio.GetObjectResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.WebApplicationContext;

class ImageTaskListenerTest extends BaseIntegrationTest {

  @Value("${stage}")
  private String stage;

  @Autowired QueueProperties queueConfig;
  @Autowired ImageTaskListener imageTaskListener;

  ImageTaskListenerTest(WebApplicationContext context) {
    super(context);
  }

  @Test
  void convertExerciseImage_shouldThrow_whenDownloadFiled() throws Exception {
    when(minioClient.getObject(any())).thenThrow(new RuntimeException());

    assertThrows(
        DownloadException.class,
        () ->
            imageTaskListener.convertExerciseImage(
                read(defaultExerciseImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertWorkoutImage_shouldThrow_whenDownloadFiled() throws Exception {
    when(minioClient.getObject(any())).thenThrow(new RuntimeException());

    assertThrows(
        DownloadException.class,
        () ->
            imageTaskListener.convertWorkoutImage(
                read(defaultWorkoutImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertExerciseImage_shouldThrow_whenConvertFiled() throws Exception {

    GetObjectResponse invalidInputStream =
        new GetObjectResponse(null, null, null, null, new ByteArrayInputStream("a".getBytes()));

    when(minioClient.getObject(any())).thenReturn(invalidInputStream);

    assertThrows(
        ConvertException.class,
        () ->
            imageTaskListener.convertExerciseImage(
                read(defaultExerciseImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertWorkoutImage_shouldThrow_whenConvertFiled() throws Exception {

    GetObjectResponse invalidInputStream =
        new GetObjectResponse(null, null, null, null, new ByteArrayInputStream("a".getBytes()));

    when(minioClient.getObject(any())).thenReturn(invalidInputStream);

    assertThrows(
        ConvertException.class,
        () ->
            imageTaskListener.convertWorkoutImage(
                read(defaultWorkoutImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertExerciseImage_shouldThrow_whenUploadFiled() throws Exception {

    GetObjectResponse imageStream =
        new GetObjectResponse(
            null, null, null, null, new FileInputStream("src/test/resources/image/img.png"));

    when(minioClient.getObject(any())).thenReturn(imageStream);
    when(minioClient.putObject(any())).thenThrow(new IOException());

    assertThrows(
        UploadException.class,
        () ->
            imageTaskListener.convertExerciseImage(
                read(defaultExerciseImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertWorkoutImage_shouldThrow_whenUploadFiled() throws Exception {

    GetObjectResponse imageStream =
        new GetObjectResponse(
            null, null, null, null, new FileInputStream("src/test/resources/image/img.png"));

    when(minioClient.getObject(any())).thenReturn(imageStream);
    when(minioClient.putObject(any())).thenThrow(new IOException());

    assertThrows(
        UploadException.class,
        () ->
            imageTaskListener.convertWorkoutImage(
                read(defaultWorkoutImageTask, ImageTaskDto.class)));

    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(ExerciseImageDto.class));
    verify(rabbitTemplate, never())
        .convertAndSend(anyString(), anyString(), any(WorkoutImageDto.class));
  }

  @Test
  void convertExerciseImage_shouldSuccess() throws Exception {
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
            any(ExerciseImageDto.class));

    imageTaskListener.convertExerciseImage(read(defaultExerciseImageTask, ImageTaskDto.class));

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            any(ExerciseImageDto.class));
  }

  @Test
  void convertWorkoutImage_shouldSuccess() throws Exception {
    GetObjectResponse imageStream =
        new GetObjectResponse(
            null, null, null, null, new FileInputStream("src/test/resources/image/img.png"));

    when(minioClient.getObject(any())).thenReturn(imageStream);
    when(minioClient.putObject(any())).thenReturn(null);

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToSave()),
            any(WorkoutImageDto.class));

    imageTaskListener.convertWorkoutImage(read(defaultWorkoutImageTask, ImageTaskDto.class));

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToSave()),
            any(WorkoutImageDto.class));
  }

  @Test
  void convertWorkoutImage_GIF_shouldNotResize() throws Exception {

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToSave()),
            any(WorkoutImageDto.class));

    imageTaskListener.convertWorkoutImage(read(workoutGifImageTask, ImageTaskDto.class));

    ArgumentCaptor<WorkoutImageDto> captor = ArgumentCaptor.forClass(WorkoutImageDto.class);

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getWorkout().getImageToSave()),
            captor.capture());
    verify(minioClient, never()).getObject(any());

    WorkoutImageDto actual = captor.getValue();
    WorkoutImageDto expected = read(expectedWorkoutImageDto, WorkoutImageDto.class);

    assertEquals(expected, actual);
  }

  @Test
  void convertExerciseImage_GIF_shouldNotResize() throws Exception {

    doNothing()
        .when(rabbitTemplate)
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            any(WorkoutImageDto.class));

    imageTaskListener.convertExerciseImage(read(exerciseGifImageTask, ImageTaskDto.class));

    ArgumentCaptor<ExerciseImageDto> captor = ArgumentCaptor.forClass(ExerciseImageDto.class);

    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq(stage),
            eq(stage + "_" + queueConfig.getExercise().getImageToSave()),
            captor.capture());
    verify(minioClient, never()).getObject(any());

    ExerciseImageDto actual = captor.getValue();
    ExerciseImageDto expected = read(expectedExerciseImageDto, ExerciseImageDto.class);

    assertEquals(expected, actual);
  }
}
