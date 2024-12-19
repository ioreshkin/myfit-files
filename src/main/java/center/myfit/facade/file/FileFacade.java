package center.myfit.facade.file;

import center.myfit.config.SizeImageProperties;
import center.myfit.enums.ImageSize;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.ConvertFileService;
import center.myfit.service.file.DownloadFileService;
import center.myfit.service.file.FileService;
import center.myfit.service.file.utils.FileUtilsHelper;
import center.myfit.starter.dto.ImageTaskDto;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
   * загрузка из MinIO , конвертивароние файлов и отправка ссылок на сохранение для
   * обогащения exerciseImageDto ссылками mobile и desktop.
   */
  public void downloadAndConvertFile(ImageTaskDto imageTask) {

    try {
      String originalUrl = imageTask.image().original();

      MultipartFile originalFile = downloadFileService.downloadFile(originalUrl);

      File mobileImageFile = convertFileService.convertSize(originalFile,
          config.getMobile().getWidth(), config.getMobile().getHeight());
      File desktopImageFile = convertFileService.convertSize(originalFile,
          config.getDesktop().getWidth(), config.getDesktop().getHeight());

      MultipartFile mobileImage = FileUtilsHelper.toMultipartFile(mobileImageFile);
      MultipartFile desktopImage = FileUtilsHelper.toMultipartFile(desktopImageFile);


      String mobileUrl = fileService.uploadFile(mobileImage, ImageSize.MOBILE);
      String desktopUrl = fileService.uploadFile(desktopImage, ImageSize.DESKTOP);

      exerciseService.sendExerciseImageToSave(imageTask, originalUrl, mobileUrl,
          desktopUrl);

    } catch (Exception e) {

      throw new RuntimeException("Ошибка при обработке задачи изображения: " + e.getMessage(), e);
    }
  }
}
