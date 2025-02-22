package center.myfit.facade.workout;

import center.myfit.enums.ImageSize;
import center.myfit.mapper.WorkoutMapper;
import center.myfit.service.file.FileService;
import center.myfit.service.user.UserAwareImpl;
import center.myfit.service.workout.WorkoutService;
import center.myfit.starter.dto.WorkoutDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Фасад для работы с тренировками. */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutFacade {

  private final FileService fileService;
  private final WorkoutMapper workoutMapper;
  private final UserAwareImpl userAware;
  private final WorkoutService workoutService;

  /** Создание тренировки. */
  public ResponseEntity<WorkoutDto> createExercise(MultipartFile file, WorkoutDto workoutDto)
      throws IOException {

    WorkoutDto enrichedDto = workoutMapper.enrichKeycloakId(workoutDto, userAware.getKeycloakId());

    if (file != null) {
      String path =
          fileService.uploadFile(
              file.getInputStream(),
              ImageSize.ORIGINAL,
              file.getOriginalFilename(),
              MediaType.parseMediaType(file.getContentType()));

      enrichedDto = workoutMapper.enrichOriginal(enrichedDto, path);
    }

    ResponseEntity<WorkoutDto> response = workoutService.saveWorkout(enrichedDto);

    if (file != null) {
      workoutService.sendImageTaskToConvert(response.getBody());
    }

    return ResponseEntity.ok(response.getBody());
  }
}
