package center.myfit.service.file.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/** утилитный класс для конвертирования типа представления файла. */
public class FileUtilsHelper {

  /** Метод для приведения File в MultipartFile. */
  public static MultipartFile toMultipartFile(File file) throws IOException {
    return new MockMultipartFile(
        file.getName(),
        file.getName(),
        null,                       // MIME-тип, можно указать
        FileUtils.readFileToByteArray(file)
    );
  }
}
