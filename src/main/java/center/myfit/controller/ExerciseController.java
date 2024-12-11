package center.myfit.controller;


import center.myfit.service.ExerciseFacade;
import center.myfit.starter.dto.ExerciseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для загрузки изображения.
 */
@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class ExerciseController {

  private final ExerciseFacade exerciseFacade; // Используем новый сервис

  /**
   * myfit-files POST /exercise.
   */
  @PostMapping("/exercise")
  public ResponseEntity<ExerciseDto> createExercise(@RequestParam("file") MultipartFile file,
                                                @RequestBody ExerciseDto exerciseDto,
                                                @RequestHeader("Authorization") String token) {
    log.info("Запрос на создание упражнения {}", exerciseDto.title());

    return exerciseFacade.createExercise(file, exerciseDto, token);
  }
}
