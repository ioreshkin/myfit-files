package center.myfit.exception;

import center.myfit.starter.dto.ErrorDto;
import lombok.Getter;

/** Ошибка валидации. */
@Getter
public class BadRequestException extends RuntimeException {

  private ErrorDto errorDto;

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   * @param errorDto - дто сообщения об ошибке
   */
  public BadRequestException(String message, ErrorDto errorDto) {
    super(message);
    this.errorDto = errorDto;
  }

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public BadRequestException(String message) {
    super(message);
  }
}
