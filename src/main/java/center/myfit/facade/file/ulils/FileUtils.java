package center.myfit.facade.file.ulils;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

/**Утильный клас преобразования файла в поток.*/
public class FileUtils {

  /**
   * Преобразует MultipartFile в InputStream.
   *
   * @param file - исходный MultipartFile
   * @return InputStream, представляющий файл
   * @throws IOException если возникает ошибка при получении потока
   */
  public static InputStream getInputStream(MultipartFile file) throws IOException {
    if (file != null) {
      return file.getInputStream();
    }
    return null;
  }
}