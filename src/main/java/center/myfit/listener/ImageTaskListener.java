package center.myfit.listener;


import center.myfit.config.QueueProperties;
import center.myfit.facade.file.FileFacade;
import center.myfit.starter.dto.ImageTaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


/** TaskListener, слушает задачу из  Rabbit files_files_exercise_image. */
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageTaskListener {
  private final FileFacade fileFacade;
  private final QueueProperties config;



  /** конвертация original формата изображения и отправка на сохранение в myfit-back. */
  @RabbitListener(queues = "#{config.exercise.image}")
  public void processImageTask(ImageTaskDto imageTask) {
    log.info("принята задача из очереди {}", config.getExercise().getImage());
    fileFacade.downloadAndConvertFile(imageTask);

  }
}



