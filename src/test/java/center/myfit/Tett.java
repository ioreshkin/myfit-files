package center.myfit;

import center.myfit.starter.dto.ImageDto;
import center.myfit.starter.dto.WorkoutDto;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class Tett {

  @Test
  void testt() throws JsonProcessingException {
    WorkoutDto workoutDto =
        new WorkoutDto(
            1L,
            null,
            "title",
            List.of(
                new WorkoutDto.ExerciseWorkoutDto(
                    2L, 1, List.of(new WorkoutDto.IterationDto(5, 10.0f)))),
            "descr",
            new ImageDto("orig", null, null));

    System.out.println(new ObjectMapper().writeValueAsString(workoutDto));
  }
}
