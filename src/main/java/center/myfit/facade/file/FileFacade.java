package center.myfit.facade.file;

import center.myfit.config.SizeImageProperties;
import center.myfit.enums.ImageSize;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.ConvertFileService;
import center.myfit.service.file.DownloadFileService;
import center.myfit.service.file.FileService;
import center.myfit.starter.dto.ImageTaskDto;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
   * Загрузка из MinIO, конвертивароние файлов и отправка ссылок на сохранение для
   * обогащения exerciseImageDto ссылками mobile и desktop.
   */
  public void downloadAndConvertFile(ImageTaskDto imageTask) {
    String originalUrl = imageTask.image().original();
    InputStream originalFile = downloadFileService.downloadFile(originalUrl);

    try {
      InputStream mobileImageStream = convertFileService.convertSize(originalFile,
          config.getSize().getMobileWidth(), config.getSize().getMobileHeight());
      InputStream desktopImageStream = convertFileService.convertSize(originalFile,
          config.getSize().getDesktopWidth(), config.getSize().getDesktopHeight());


      String mobileUrl = fileService.uploadFile(mobileImageStream, ImageSize.MOBILE,
          "mobile_image.jpg", mobileImageStream.available(), "image/jpeg");
      String desktopUrl = fileService.uploadFile(desktopImageStream, ImageSize.DESKTOP,
          "desktop_image.jpg", desktopImageStream.available(), "image/jpeg");

      exerciseService.sendExerciseImageToSave(imageTask, originalUrl, mobileUrl,
          desktopUrl);

    } catch (Exception e) {

      throw new RuntimeException("Ошибка при обработке задачи изображения: " + e.getMessage(), e);
    }
  }
}
