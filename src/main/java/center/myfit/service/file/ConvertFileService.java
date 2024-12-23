package center.myfit.service.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

/** Сервис конвертирования файлов. */
@Slf4j
@Service
public class ConvertFileService {

  /**
   * Конвертация изображения по заданным размерам.
   *
   * @param inputStream Входной поток данных.
   * @param width Ширина.
   * @param height Высота.
   * @return InputStream с результатом конвертации.
   */
  public InputStream convertSize(InputStream inputStream, int width, int height) {
    log.info("Старт конвертации изображения по формату: {} x {}", width, height);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      Thumbnails.of(inputStream)
          .size(width, height)
          .outputFormat("jpg")
          .toOutputStream(outputStream);

      log.info("Успешная конвертация изображения");

    } catch (IOException e) {
      log.error("Ошибка при конвертации изображения: {}", e.getMessage());
      throw new RuntimeException("Ошибка при конвертации изображения", e);
    }
    return new ByteArrayInputStream(outputStream.toByteArray());
  }
}
