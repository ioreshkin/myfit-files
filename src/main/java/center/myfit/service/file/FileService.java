package center.myfit.service.file;

import center.myfit.config.MinioProperties;
import center.myfit.enums.ImageSize;
import center.myfit.exception.UploadException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/** Сервис сохранения в MinIO. */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  private final MinioClient minioClient;
  private final MinioProperties config;

  /** Подготовка бакета для сохранения. */
  @PostConstruct
  public void initializeBucket() {
    try {
      log.debug("Проверяем существование bucket: {}", config.getBucketName());

      boolean isBucketExist =
          minioClient.bucketExists(
              BucketExistsArgs.builder().bucket(config.getBucketName()).build());

      log.debug("Bucket существует? {}", isBucketExist);

      if (isBucketExist) {
        return;
      }
      log.info("Создаем bucket: {}", config.getBucketName());

      minioClient.makeBucket(MakeBucketArgs.builder().bucket(config.getBucketName()).build());
      // TODO       minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().config().build());

    } catch (Exception e) {
      log.error("Ошибка при инициализации бакета: {}", e.getMessage());
      throw new RuntimeException("Ошибка при создании бакета", e);
    }
  }

  /** Загрузка в MinIO. */
  public String uploadFile(InputStream inputStream, ImageSize imageSize, String fileName,
                           MediaType mediaType) {
    String objectName = String.format(
        "%s/%s.%s.%s",
        config.getPublicFolder(),
        UUID.randomUUID(),
        imageSize.getPostfix(),
        FileNameUtils.getExtension(fileName));

    try {
      log.info("Загружаем файл: {}", objectName);

      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(config.getBucketName())
              .object(objectName)
              .stream(inputStream, inputStream.available(), -1)
              .contentType(mediaType.toString())
              .build()
      );

      log.info("Файл загружен: {}", objectName);
      return objectName;

    } catch (Exception e) {
      throw new UploadException("Ошибка загрузки файла", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }
}
