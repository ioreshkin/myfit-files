package center.myfit.service;

import center.myfit.service.feignclient.MyFitBackClient;
import center.myfit.starter.dto.CreateExerciseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseValidationService {

    private final MyFitBackClient myFitBackClient;

    public ResponseEntity<CreateExerciseDto> validateExercise(CreateExerciseDto createExerciseDto, String token) {
        return myFitBackClient.validateExercise(createExerciseDto, "Bearer " + token);
    }
}