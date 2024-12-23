package center.myfit.controller;

import center.myfit.facade.exercise.ExerciseFacade;
import center.myfit.facade.file.ulils.FileUtils;
import center.myfit.mapper.ExerciseMapper;
import center.myfit.starter.dto.ExerciseDto;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
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

/** Контроллер для загрузки изображения. */
@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

  private final ExerciseFacade exerciseFacade;
  private final ExerciseMapper mapper;

  /** Обработка запроса POST /exercise. */
  @PostMapping(
      value = "/exercise",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<ExerciseDto> createExercise(
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestPart(value = "dto") @Valid ExerciseDto exerciseDto) {
    log.info("Запрос на создание упражнения {}", exerciseDto.title());

    InputStream fileInputStream = null;
    try {
      fileInputStream = FileUtils.getInputStream(file);
    } catch (IOException e) {
      log.error("Ошибка при получении потока из MultipartFile", e);
      throw new RuntimeException("Ошибка при получении потока из MultipartFile", e);
    }

    return exerciseFacade.createExercise(fileInputStream, exerciseDto);
  }
}
