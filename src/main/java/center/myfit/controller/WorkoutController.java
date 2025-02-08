package center.myfit.controller;

import center.myfit.facade.workout.WorkoutFacade;
import center.myfit.starter.dto.WorkoutDto;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Контроллер для работы с тренировками. */
@Slf4j
@RestController
@RequestMapping("${api-prefix}/workout")
@RequiredArgsConstructor
@Validated
public class WorkoutController {
  private final WorkoutFacade workoutFacade;

  /** Обработка запроса POST /exercise. */
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<WorkoutDto> createExercise(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart(value = "dto") @Valid WorkoutDto workoutDto) {

    log.info("Запрос на создание упражнения {}", workoutDto.title());

    try {
      return workoutFacade.createExercise(file, workoutDto);
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при обработке загружаемого файла", e);
    }
  }
}
