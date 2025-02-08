package center.myfit.exception;

/** Ошибка при конвертировании файла. */
public class ConvertException extends RuntimeException {

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public ConvertException(String message) {
    super(message);
  }

  /**
   * Конструктор с информационным сообщением и причиной.
   *
   * @param message - сообщение о причинах ошибки.
   * @param cause - причина ошибки.
   */
  public ConvertException(String message, Throwable cause) {
    super(message, cause);
  }
}
