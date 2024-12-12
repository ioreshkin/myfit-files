package center.myfit.service.exercise;

import center.myfit.config.QueueProperties;
import center.myfit.mapper.ImageTaskMapper;
import center.myfit.service.feignclient.MyFitBackClient;
import center.myfit.starter.dto.ExerciseDto;
import center.myfit.starter.dto.ImageTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Сервис для передачи загруженного файла для дальнейшей обработки. */
@Service
@RequiredArgsConstructor
public class ExerciseService {

  private final MyFitBackClient myFitBackClient;
  private final RabbitTemplate rabbitTemplate;
  private final ImageTaskMapper mapper;
  private final QueueProperties config;

  /** Отправка запроса на валидацию. */
  public ResponseEntity<ExerciseDto> saveExercise(ExerciseDto exerciseDto) {
    return myFitBackClient.saveExercise(exerciseDto);
  }

  /** Отправка в очередь Rabbit. */
  public void sendImageTask(ExerciseDto exerciseDto) {

    ImageTaskDto imageTask = mapper.toImageTask(exerciseDto);

    rabbitTemplate.convertAndSend(config.getExercise().getImage(), imageTask);
  }
}
