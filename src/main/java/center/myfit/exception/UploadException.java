package center.myfit.exception;

/** Ошибка при загрузке файла. */
public class UploadException extends RuntimeException {

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public UploadException(String message) {
    super(message);
  }

  /**
   * Конструктор с информационным сообщением и причиной.
   *
   * @param message - сообщение о причинах ошибки.
   * @param cause - причина ошибки.
   */
  public UploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
