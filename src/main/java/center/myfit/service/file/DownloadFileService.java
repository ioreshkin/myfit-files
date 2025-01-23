package center.myfit.service.file;

import center.myfit.config.MinioProperties;
import center.myfit.exception.DownloadException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Сервис загрузки файлов. */
@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadFileService {

  private final MinioClient minioClient;
  private final MinioProperties config;

  /** Загрузка файла из MinIO по URL. */
  public InputStream downloadFile(String objectName) {
    try {
      log.info("Загружаем файл из MinIO: {}", objectName);

      return minioClient.getObject(
          GetObjectArgs.builder()
              .bucket(config.getBucketName())
              .object(objectName)
              .build()
      );
    } catch (Exception e) {
      throw new DownloadException("Ошибка загрузки файла из MinIO", e);
    }
  }
}
