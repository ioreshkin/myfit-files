package center.myfit.service.workout;

import center.myfit.config.QueueProperties;
import center.myfit.mapper.ImageTaskMapper;
import center.myfit.service.feignclient.MyFitBackClient;
import center.myfit.starter.dto.ImageTaskDto;
import center.myfit.starter.dto.WorkoutDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Сервис для работы с тренировками. */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutService {

  @Value("${stage}")
  private String stage;

  private final MyFitBackClient myFitBackClient;
  private final QueueProperties config;
  private final RabbitTemplate rabbitTemplate;
  private final ImageTaskMapper mapper;

  /** Отправка запроса на сохранение тренировки. */
  public ResponseEntity<WorkoutDto> saveExercise(WorkoutDto workoutDto) {
    return myFitBackClient.saveExercise(workoutDto);
  }

  /** Отправка в очередь Rabbit на конвертирование. */
  public void sendImageTaskToConvert(WorkoutDto workoutDto) {
    log.info("отправка imageTask на конвертирование");

    ImageTaskDto imageTask = mapper.toImageTask(workoutDto);

    rabbitTemplate.convertAndSend(
        stage, stage + "_" + config.getWorkout().getImageToConvert(), imageTask);
  }
}
