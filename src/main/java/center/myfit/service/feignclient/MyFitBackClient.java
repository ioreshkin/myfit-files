package center.myfit.service.feignclient;

import center.myfit.starter.dto.ExerciseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *  Настройка FeignClient для взаимодействия с myfit-back.
 */
@FeignClient(name = "myfit-back", contextId = "myFitBackClient") // Указываем только имя и contextId
public interface MyFitBackClient {

  /**
   *  myfit-back POST validate.
   */
  @PostMapping("/exercise/validateAndSave") // ну или какой он там будет
  ResponseEntity<ExerciseDto> validateExercise(
      @RequestBody ExerciseDto exerciseDto,
      @RequestHeader("Authorization") String token);
}