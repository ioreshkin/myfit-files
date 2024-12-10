package center.myfit.service;

import center.myfit.starter.dto.ExerciseDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 *  фасад для контроллера Upload.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadExerciseService {

  private final FileService fileService;
  private final ExerciseService exerciseService;

  /**
   *  аккумулирующий метод для вызова всех методов по созранению
   *  файла и отправки на подрезку.
   */
  public ResponseEntity<ExerciseDto> uploadFile(MultipartFile file,
                                                ExerciseDto exerciseDto,
                                                String token) {
    log.info("приняты парметры для сохранения запрос на сохранение файла");


    ExerciseDto enrichedDto = fileService.uploadFileAndEnrichDto(exerciseDto, file);


    ResponseEntity<ExerciseDto> response = exerciseService.validateExercise(enrichedDto, token);

    if (response.getStatusCode().is2xxSuccessful()) {
      exerciseService.sendToPressAndCat(response.getBody());
      //вот тут у меня вопрос а что если пустой ответ придет?
      // как лучше обработать?
      return ResponseEntity.ok(response.getBody());
    } else {

      throw new ValidationException("Ошибка валидации: " + response.getBody());
    }
  }
}