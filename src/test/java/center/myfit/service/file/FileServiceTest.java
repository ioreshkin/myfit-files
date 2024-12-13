package center.myfit.service.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import center.myfit.config.MinioProperties;
import io.minio.MinioClient;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FileServiceTest {
  private static FileService fileService;
  private static final MinioClient minioClient = Mockito.mock(MinioClient.class);

  @BeforeAll
  static void init() {
    MinioProperties minioProperties = new MinioProperties();
    minioProperties.setBucketName("somebucket");
    fileService = new FileService(minioClient, minioProperties);
  }

  @Test
  void initializeBucket_shouldThrow_whenMinioClientThrow() throws Exception {
    when(minioClient.bucketExists(any())).thenReturn(false);
    doThrow(IOException.class).when(minioClient).makeBucket(any());

    assertThrows(RuntimeException.class, () -> fileService.initializeBucket());
  }

  @Test
  void initializeBucket_shouldNotCreateBucket_whenBucketExists() throws Exception {
    when(minioClient.bucketExists(any())).thenReturn(true);

    fileService.initializeBucket();

    verify(minioClient, never()).makeBucket(any());
  }

  @Test
  void initializeBucket_shouldCreateBucket_whenBucketNotExists() throws Exception {
    when(minioClient.bucketExists(any())).thenReturn(false);
    doNothing().when(minioClient).makeBucket(any());

    fileService.initializeBucket();

    verify(minioClient, times(1)).makeBucket(any());
  }
}
