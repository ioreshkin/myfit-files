package center.myfit.facade.exercise;

import center.myfit.enums.ImageSize;
import center.myfit.mapper.ExerciseMapper;
import center.myfit.service.exercise.ExerciseService;
import center.myfit.service.file.FileService;
import center.myfit.service.user.UserAwareImpl;
import center.myfit.starter.dto.ExerciseDto;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
  public ResponseEntity<ExerciseDto> createExercise(InputStream fileInputStream,
                                                    ExerciseDto exerciseDto) {

    ExerciseDto enrichedDto = mapper.enrichKeycloakId(exerciseDto, userAware.getKeycloakId());

    if (fileInputStream != null) {
      String path = fileService
          .uploadFile(fileInputStream, ImageSize.ORIGINAL, "uploaded_image.jpg",
          MediaType.IMAGE_JPEG);

      enrichedDto = mapper.enrichOriginal(enrichedDto, path);
    }

    ResponseEntity<ExerciseDto> response = exerciseService.saveExercise(enrichedDto);

    if (fileInputStream != null) {
      exerciseService.sendImageTaskToConvert(response.getBody());
    }

    return ResponseEntity.ok(response.getBody());
  }
}
