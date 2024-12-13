package center.myfit.facade.exercise;

import center.myfit.enums.ImageSize;
import center.myfit.mapper.ExerciseMapper;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.FileService;
import center.myfit.service.user.UserAwareImpl;
import center.myfit.starter.dto.ExerciseDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Фасад для работы с упражнениями. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseFacade {

  private final FileService fileService;
  private final ExerciseService exerciseService;
  private final ExerciseMapper mapper;
  private final UserAwareImpl userAware;

  /** Создание упражнения. */
  public ResponseEntity<ExerciseDto> createExercise(MultipartFile file, ExerciseDto exerciseDto) {

    ExerciseDto enrichedDto = mapper.enrichKeycloakId(exerciseDto, userAware.getKeycloakId());

    if (Objects.nonNull(file)) {
      String path = fileService.uploadFile(file, ImageSize.ORIGINAL);
      enrichedDto = mapper.enrichOriginal(enrichedDto, path);
    }

    ResponseEntity<ExerciseDto> response = exerciseService.saveExercise(enrichedDto);

    if (Objects.nonNull(file)) {
      exerciseService.sendImageTask(response.getBody());
    }

    return ResponseEntity.ok(response.getBody());
  }
}
