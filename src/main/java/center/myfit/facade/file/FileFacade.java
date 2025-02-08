package center.myfit.facade.file;

import center.myfit.config.SizeImageProperties;
import center.myfit.enums.ImageSize;
import center.myfit.exception.ConvertException;
import center.myfit.exception.DownloadException;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.ConvertFileService;
import center.myfit.service.file.DownloadFileService;
import center.myfit.service.file.FileService;
import center.myfit.service.workout.WorkoutService;
import center.myfit.starter.dto.ImageTaskDto;
import center.myfit.starter.enums.EntityType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/** Фасад для работы с конвертацией изображений. */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileFacade {
  private static final String IMAGE = "image/";

  private final FileService fileService;
  private final ConvertFileService convertFileService;
  private final ExerciseService exerciseService;
  private final DownloadFileService downloadFileService;
  private final SizeImageProperties config;
  private final WorkoutService workoutService;

  /**
   * Загрузка из MinIO, конвертивароние файлов и отправка ссылок на сохранение для обогащения
   * ImageDto ссылками mobile и desktop.
   */
  public void convertImage(ImageTaskDto imageTask) {
    String originalUrl = imageTask.image().original();
    String extension = FileNameUtils.getExtension(originalUrl);
    MediaType mediaType = MediaType.parseMediaType(IMAGE + extension);

    if ("gif".equals(extension)) {
      sendImageToSave(imageTask, originalUrl, originalUrl, originalUrl);
      return;
    }

    SizeImageProperties.Size mobileSize = getMobileSize(imageTask.entityType());
    SizeImageProperties.Size desktopSize = getDesktopSize(imageTask.entityType());

    String mobileUrl;
    String desktopUrl;
    byte[] originalImageStream;
    try {
      originalImageStream = IOUtils.toByteArray(downloadFileService.downloadFile(originalUrl));
      try (InputStream mobileImageStream =
              convertFileService.convertSize(
                  new ByteArrayInputStream(originalImageStream),
                  mobileSize.getWidth(),
                  mobileSize.getHeight());
          InputStream desktopImageStream =
              convertFileService.convertSize(
                  new ByteArrayInputStream(originalImageStream),
                  desktopSize.getWidth(),
                  desktopSize.getHeight())) {

        mobileUrl =
            fileService.uploadFile(mobileImageStream, ImageSize.MOBILE, originalUrl, mediaType);
        desktopUrl =
            fileService.uploadFile(desktopImageStream, ImageSize.DESKTOP, originalUrl, mediaType);
      }
    } catch (IOException | DownloadException e) {
      log.error("ошибка при создании массива байт: {}", e.getMessage());
      throw new DownloadException("ошибка при создании массива байт", e);
    }

    sendImageToSave(imageTask, originalUrl, mobileUrl, desktopUrl);
  }

  private void sendImageToSave(
      ImageTaskDto imageTask, String originalUrl, String mobileUrl, String desktopUrl) {
    switch (imageTask.entityType()) {
      case EXERCISE ->
          exerciseService.sendExerciseImageToSave(imageTask, originalUrl, mobileUrl, desktopUrl);
      case WORKOUT ->
          workoutService.sendExerciseImageToSave(imageTask, originalUrl, mobileUrl, desktopUrl);
      default -> throw new ConvertException("Unknown entity type!");
    }
  }

  private SizeImageProperties.Size getMobileSize(EntityType type) {
    return switch (type) {
      case EXERCISE -> config.getExercise().getMobile();
      case WORKOUT -> config.getWorkout().getMobile();
    };
  }

  private SizeImageProperties.Size getDesktopSize(EntityType type) {
    return switch (type) {
      case EXERCISE -> config.getExercise().getDesktop();
      case WORKOUT -> config.getWorkout().getDesktop();
    };
  }
}
