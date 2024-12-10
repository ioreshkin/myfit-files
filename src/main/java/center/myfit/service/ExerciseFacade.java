package center.myfit.service;

import center.myfit.starter.dto.ExerciseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *  фасад для контроллера Upload.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseFacade {

  private final FileService fileService;
  private final ExerciseService exerciseService;

  /**
   *  аккумулирующий метод для вызова всех методов по созранению
   *  файла и отправки на подрезку.
   */
  public ResponseEntity<ExerciseDto> createExercise(MultipartFile file,
                                                    ExerciseDto exerciseDto,
                                                    String token) {

    ExerciseDto enrichedDto = fileService.uploadFileAndEnrichDto(exerciseDto, file);

    ResponseEntity<ExerciseDto> response = exerciseService.saveExercise(enrichedDto, token);

    if (response.getStatusCode().is2xxSuccessful()) {
      exerciseService.sendToPressAndCat(response.getBody());

      return ResponseEntity.ok(response.getBody());
    } else {

      return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
  }
}