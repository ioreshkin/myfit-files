package center.myfit.exeption;

/**
 *  Кастомная ошибка при загрузке.
 */
public class UploadExeption extends RuntimeException {

  /**
   *  конструктор.
   */
  public UploadExeption(String message) {
    super(message);
  }

}