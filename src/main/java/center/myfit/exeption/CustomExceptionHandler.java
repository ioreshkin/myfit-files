package center.myfit.exeption;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Кастомный обработчик исключений.
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

  /**
   * обработка ошибки загрузки.
   */
  @ExceptionHandler(UploadExeption.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseError handleExeption(UploadExeption e) {
    log.error(e.getMessage(), e);
    return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  /**
   * обработка любых исключений.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseError handleExeption(Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseError(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  /**
   * обработка ошибки валидации запроса.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseError handleExeption(ValidationException e) {
    log.error(e.getMessage(), e);
    return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
  }


}
