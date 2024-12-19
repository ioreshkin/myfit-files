package center.myfit.service.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Сервис конвертирования файлов. */
@Slf4j
@Service
public class ConvertFileService {
  /** Конвертация файла по заданным размерам. */
  public File convertSize(MultipartFile file, int width, int height) throws IOException {

    log.info("старт конвертации файла по формату: {}", width, " x ", height);


    File tempFile = File.createTempFile("tempImage", ".jpg");

    BufferedImage originalImage = Thumbnails.of(file.getInputStream())
        .size(width, height)
        .asBufferedImage();


    Thumbnails.of(originalImage)
        .size(width, height)
        .toFile(tempFile);
    log.info("успешная конвертация  файла: {}", file.getName());

    return tempFile; // Возвращаем временный файл
  }
}
