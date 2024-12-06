package center.myfit.service.feignclient;

import center.myfit.starter.dto.CreateExerciseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "myfit-back", url = "http:???????????????//myfit-back")//тут надо адрес вписать
public interface MyFitBackClient {

  @PostMapping("/api/exercises")
  ResponseEntity<CreateExerciseDto> validateExercise(
      @RequestBody CreateExerciseDto createExerciseDto,
      @RequestHeader("Authorization") String token);
}