package center.myfit.listener;

import center.myfit.config.QueueProperties;
import center.myfit.facade.file.FileFacade;
import center.myfit.starter.dto.ImageTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/** TaskListener, получает ImageTask из Rabbit. */
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageTaskListener {

  private final FileFacade fileFacade;
  private final QueueProperties config;

  /** Конвертация original формата изображения и отправка на сохранение в myfit-back. */
  @RabbitListener(queues = "${stage}" + "_files_files_exercise_image_q")
  public void convertExerciseImage(ImageTaskDto imageTask) {
    log.info(
        "принята задача из очереди {}, на конвертацию изображения упражнения для objectId: {}",
        config.getExercise().getImageToConvert(),
        imageTask.objectId());
    fileFacade.convertImage(imageTask);
  }

  /** Конвертация original формата изображения и отправка на сохранение в myfit-back. */
  @RabbitListener(queues = "${stage}" + "_files_files_workout_image_q")
  public void convertWorkoutImage(ImageTaskDto imageTask) {
    log.info(
            "принята задача из очереди {}, на конвертацию изображения тренировки для objectId: {}",
            config.getExercise().getImageToConvert(),
            imageTask.objectId());
    fileFacade.convertImage(imageTask);
  }
}
