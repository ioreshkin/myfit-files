package center.myfit.service.feignclient;

import center.myfit.starter.dto.ExerciseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Настройка FeignClient для взаимодействия с myfit-back. */
@FeignClient(name = "myfit-back", url = "${myfit-back.url}")
public interface MyFitBackClient {

  /** Сохранение упражнения. */
  @PostMapping("/exercise")
  ResponseEntity<ExerciseDto> saveExercise(@RequestBody ExerciseDto exerciseDto);
}
