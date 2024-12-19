package center.myfit.service.file;

import center.myfit.exception.UploadException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Сервис загрузки файлов. */
@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadFileService {

  private final MinioClient minioClient;

  /** Загрузка файла из MinIO по URL без авторизации. */
  public MultipartFile downloadFile(String originalUrl) {
    try {
      log.info("получаем бакет и имя файла из: {}", originalUrl);
      String[] parts = originalUrl.split("/");
      if (parts.length < 5) {
        throw new IllegalArgumentException("Invalid URL format: " + originalUrl);
      }

      String bucketName = parts[3]; // Предполагается формат http://<minio-url>/<bucket-name>/<object-name>
      String objectName = parts[4];

      log.info("загружаем файл: {}", objectName);
      InputStream inputStream = minioClient.getObject(
               GetObjectArgs.builder()
              .bucket(bucketName)
              .object(objectName)
              .build()
      );

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      byte[] fileBytes = outputStream.toByteArray();

      return new MockMultipartFile(
          objectName, // Имя файла
          objectName, // Оригинальное имя файла
          null,       // MIME-тип, можно указать явно при необходимости
          fileBytes   // Содержимое файла
      );
    } catch (IOException e) {
      throw new RuntimeException("Ошибка чтения данных из потока", e);
    } catch (Exception e) {
      throw new UploadException("Ошибка загрузки из MinIO", e);
    }
  }
}
