package center.myfit.exeption;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *  Представляет ошибку в ответе API.
 */
@Getter
public class ResponseError {

  private final HttpStatus status;

  private final String message;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime time = LocalDateTime.now();

  /**
   *  конструктор.
   */
  public ResponseError(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
