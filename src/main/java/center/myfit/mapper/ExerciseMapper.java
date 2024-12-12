package center.myfit.mapper;

import center.myfit.starter.dto.ExerciseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер ExerciseDto. */
@Mapper
public interface ExerciseMapper {
  /** Обогащение дто ссылкой original. */
  @Mapping(target = "image.original", source = "original")
  ExerciseDto enrichOriginal(ExerciseDto dto, String original);

  /** Обогащение дто keycloakId. */
  @Mapping(target = "keycloakId", source = "keycloakId")
  ExerciseDto enrichKeycloakId(ExerciseDto dto, String keycloakId);
}
