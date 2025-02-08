package center.myfit.facade.file;

import center.myfit.config.SizeImageProperties;
import center.myfit.enums.ImageSize;
import center.myfit.exception.DownloadException;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.ConvertFileService;
import center.myfit.service.file.DownloadFileService;
import center.myfit.service.file.FileService;
import center.myfit.starter.dto.ImageTaskDto;
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

  private final FileService fileService;
  private final ConvertFileService convertFileService;
  private final ExerciseService exerciseService;
  private final DownloadFileService downloadFileService;
  private final SizeImageProperties config;

  /**
   * Загрузка из MinIO, конвертивароние файлов и отправка ссылок на сохранение для обогащения
   * exerciseImageDto ссылками mobile и desktop.
   */
  public void convertImage(ImageTaskDto imageTask) {
    String originalUrl = imageTask.image().original();
    String extension = FileNameUtils.getExtension(originalUrl);

    byte[] originalImageStream = null;
    try {
      originalImageStream = IOUtils.toByteArray(downloadFileService.downloadFile(originalUrl));
      try (InputStream mobileImageStream =
              convertFileService.convertSize(
                  new ByteArrayInputStream(originalImageStream),
                  config.getMobile().getWidth(),
                  config.getMobile().getHeight());
          InputStream desktopImageStream =
              convertFileService.convertSize(
                  new ByteArrayInputStream(originalImageStream),
                  config.getDesktop().getWidth(),
                  config.getDesktop().getHeight())) {

        String mobileUrl =
            fileService.uploadFile(
                mobileImageStream, ImageSize.MOBILE, "mobile_image.jpg", MediaType.IMAGE_JPEG);
        String desktopUrl =
            fileService.uploadFile(
                desktopImageStream, ImageSize.DESKTOP, "desktop_image.jpg", MediaType.IMAGE_JPEG);

        exerciseService.sendExerciseImageToSave(imageTask, originalUrl, mobileUrl, desktopUrl);
      }
    } catch (IOException e) {
      log.error("ошибка при создании массива байт: {}", e.getMessage());
      throw new DownloadException("ошибка при создании массива байт");
    }
  }
}
