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
@FeignClient(name = "myfit-back")
public interface MyFitBackClient {

  /**
   *  myfit-back POST validate.
   */
  @PostMapping("/exercise")
  ResponseEntity<ExerciseDto> saveExercise(
      @RequestBody ExerciseDto exerciseDto,
      @RequestHeader("Authorization") String token);
}