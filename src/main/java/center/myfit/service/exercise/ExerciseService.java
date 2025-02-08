package center.myfit.service.exercise;

import center.myfit.config.QueueProperties;
import center.myfit.mapper.ExerciseImageMapper;
import center.myfit.mapper.ImageTaskMapper;
import center.myfit.service.feignclient.MyFitBackClient;
import center.myfit.starter.dto.ExerciseDto;
import center.myfit.starter.dto.ExerciseImageDto;
import center.myfit.starter.dto.ImageTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Сервис для работы с упражнениями. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

  @Value("${stage}")
  private String stage;

  private final MyFitBackClient myFitBackClient;
  private final RabbitTemplate rabbitTemplate;
  private final ImageTaskMapper mapper;
  private final ExerciseImageMapper exerciseImageMapper;
  private final QueueProperties config;

  /** Отправка запроса на сохранение упражнения. */
  public ResponseEntity<ExerciseDto> saveExercise(ExerciseDto exerciseDto) {
    return myFitBackClient.saveExercise(exerciseDto);
  }

  /** Отправка в очередь Rabbit на конвертирование. */
  public void sendImageTaskToConvert(ExerciseDto exerciseDto) {
    log.info("отправка imageTask на конвертирование");

    ImageTaskDto imageTask = mapper.toImageTask(exerciseDto);

    rabbitTemplate.convertAndSend(
        stage, stage + "_" + config.getExercise().getImageToConvert(), imageTask);
  }

  /** Отправка в очередь myfit-back на сохранение. */
  public void sendExerciseImageToSave(
      ImageTaskDto imageTask, String originalUrl, String mobileUrl, String desktopUrl) {

    log.info("отправка обогащенного exerciseImageDto на сохранение");

    ExerciseImageDto exerciseImageDto =
        exerciseImageMapper.toExerciseImageDto(
            imageTask.objectId(), originalUrl, mobileUrl, desktopUrl);

    rabbitTemplate.convertAndSend(
        stage, stage + "_" + config.getExercise().getImageToSave(), exerciseImageDto);
  }
}
