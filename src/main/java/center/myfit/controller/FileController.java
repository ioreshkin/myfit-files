package center.myfit.controller;

import center.myfit.service.ExerciseValidationService;
import center.myfit.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import center.myfit.starter.dto.CreateExerciseDto;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final ExerciseValidationService exerciseValidationService;

    @PostMapping("/upload")
    public ResponseEntity<CreateExerciseDto> uploadFile(@RequestParam("file") MultipartFile file,
                                                        @RequestBody CreateExerciseDto createExerciseDto,
                                                        @RequestHeader("Authorization") String token) {
        log.info("Принят запрос на сохранение файла");


        CreateExerciseDto enrichedDto = fileService.uploadFileAndEnrichDto(createExerciseDto, file);


        ResponseEntity<CreateExerciseDto> response = exerciseValidationService.validateExercise(enrichedDto, token);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
    /* TODO (выше поменять ретерн) дополнить в бизнес логики после валидации myfit-files в случае
        получения
        400 авляет его обратно на фронт
     */
    //myfit-files в случае получения 200 ставит в очередь задачу на обработку (ImageTaskDto) изображения и отправляет на фронт ответ 200 с обогащенным CreateExerciseDto

}