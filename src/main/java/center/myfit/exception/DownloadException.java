package center.myfit.exception;

/** Ошибка при загрузке файла. */
public class DownloadException extends RuntimeException {

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public DownloadException(String message) {
    super(message);
  }

  /**
   * Конструктор с информационным сообщением и причиной.
   *
   * @param message - сообщение о причинах ошибки.
   * @param cause   - причина ошибки.
   */
  public DownloadException(String message, Throwable cause) {
    super(message, cause);
  }
}
