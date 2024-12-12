package center.myfit.exception;

import center.myfit.starter.dto.ErrorDto;
import lombok.Getter;

/** Ошибка сервера. */
@Getter
public class InternalServerException extends RuntimeException {

  private ErrorDto errorDto;

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   * @param errorDto - дто сообщения об ошибке
   */
  public InternalServerException(String message, ErrorDto errorDto) {
    super(message);
    this.errorDto = errorDto;
  }

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public InternalServerException(String message) {
    super(message);
  }
}
