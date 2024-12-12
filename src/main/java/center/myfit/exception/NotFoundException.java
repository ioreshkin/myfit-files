package center.myfit.exception;

import center.myfit.starter.dto.ErrorDto;
import lombok.Getter;

/** Ошибка не найден объект. */
@Getter
public class NotFoundException extends RuntimeException {

  private ErrorDto errorDto;

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   * @param errorDto - дто сообщения об ошибке
   */
  public NotFoundException(String message, ErrorDto errorDto) {
    super(message);
    this.errorDto = errorDto;
  }

  /**
   * Конструктор с информационным сообщением.
   *
   * @param message - сообщение о причинах ошибки.
   */
  public NotFoundException(String message) {
    super(message);
  }
}
