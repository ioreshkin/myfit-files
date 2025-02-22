package center.myfit.controller;

import center.myfit.exception.UploadException;
import center.myfit.facade.exercise.ExerciseFacade;
import center.myfit.starter.dto.ExerciseDto;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Контроллер для работы с упражнениями. */
@Slf4j
@RestController
@RequestMapping("${api-prefix}/exercise")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

  private final ExerciseFacade exerciseFacade;

  /** Обработка запроса на создание упражнения. */
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ExerciseDto> createExercise(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart(value = "dto") @Valid ExerciseDto exerciseDto) {

    log.info("Запрос на создание упражнения {}", exerciseDto.title());

    try {
      return exerciseFacade.createExercise(file, exerciseDto);
    } catch (IOException e) {
      throw new UploadException("Ошибка при обработке загружаемого файла", e);
    }
  }

  /** Обработка запроса на изменение упражнения. */
  @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ExerciseDto> updateExercise(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart(value = "dto") @Valid ExerciseDto exerciseDto) {

    log.info("Запрос на изменение упражнения {}", exerciseDto.title());

    try {
      return exerciseFacade.updateExercise(file, exerciseDto);
    } catch (IOException e) {
      throw new UploadException("Ошибка при обработке загружаемого файла", e);
    }
  }
}
