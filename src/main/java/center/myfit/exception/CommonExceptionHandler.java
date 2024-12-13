package center.myfit.exception;

import center.myfit.starter.dto.ErrorDto;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/** Общий обработчик исключений. */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

  /** Обработка ошибки загрузки. */
  @ExceptionHandler(UploadException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto handleException(UploadException e) {
    log.error(e.getMessage(), e);
    return getErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  /** Обработка ошибки валидации. */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleException(BadRequestException e) {
    log.error(e.getMessage(), e);
    return Objects.isNull(e.getErrorDto())
        ? getErrorDto(e.getMessage(), HttpStatus.BAD_REQUEST.value())
        : e.getErrorDto();
  }

  /** Обработка ошибки не найден объект. */
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorDto handleException(NotFoundException e) {
    log.error(e.getMessage(), e);
    return Objects.isNull(e.getErrorDto())
        ? getErrorDto(e.getMessage(), HttpStatus.NOT_FOUND.value())
        : e.getErrorDto();
  }

  /** Обработка ошибки не найден объект. */
  @ExceptionHandler(InternalServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto handleException(InternalServerException e) {
    log.error(e.getMessage(), e);
    return Objects.isNull(e.getErrorDto())
        ? getErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        : e.getErrorDto();
  }

  /** Обработка ошибки нет дто. */
  @ExceptionHandler(MissingServletRequestPartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleException(MissingServletRequestPartException e) {
    log.error(e.getMessage(), e);
    return getErrorDto("Не заполнены обязательные поля формы!", HttpStatus.BAD_REQUEST.value());
  }

  /** Обработка ошибки валидации дто. */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleException(MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    String messages =
        e.getBindingResult().getFieldErrors().stream()
            .filter(Objects::nonNull)
            .map(FieldError::getDefaultMessage)
            .filter(Objects::nonNull)
            .collect(Collectors.joining(", "));
    return getErrorDto(messages, HttpStatus.BAD_REQUEST.value());
  }

  /** Обработка любых исключений. */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto handleException(Exception e) {
    log.error(e.getMessage(), e);
    return getErrorDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  private ErrorDto getErrorDto(String message, int status) {
    return new ErrorDto(message, status, LocalDateTime.now());
  }
}
