package center.myfit.service;

import center.myfit.service.feignclient.MyFitBackClient;
import center.myfit.starter.dto.ExerciseDto;
import center.myfit.starter.dto.ImageTaskDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *  Сервис для передачи загруженного файла для дальнейшей обработки.
 */
@Service
@RequiredArgsConstructor
public class ExerciseService {

  private final MyFitBackClient myFitBackClient;
  private final RabbitTemplate rabbitTemplate;

  /**
   *  отправка запроса на валидацию.
   */
  public ResponseEntity<ExerciseDto> validateExercise(ExerciseDto exerciseDto, String token) {
    try {
      return myFitBackClient.validateExercise(exerciseDto, "Bearer " + token);
    } catch (Exception e) {
      throw new ValidationException("ошибка валидации: " + e.getMessage());
    }
  }

  /**
   *  отправка в очередь Rabbit.
   */
  public void sendToPressAndCat(ExerciseDto exerciseDto) {
    // Создаем объект ImageTaskDto для отправки в очередь
    ImageTaskDto imageTask = new ImageTaskDto(
        exerciseDto.id(),
        new ImageTaskDto.ImageDto(exerciseDto.image().original())
    );

    // Отправляем задачу в очередь RabbitMQ
    rabbitTemplate.convertAndSend("files_files_exercise_image", imageTask);
  }
}