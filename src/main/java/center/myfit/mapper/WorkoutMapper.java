package center.myfit.mapper;

import center.myfit.starter.dto.WorkoutDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер WorkoutDto. */
@Mapper
public interface WorkoutMapper {

  /** Обогащение дто ссылкой original. */
  @Mapping(target = "image.original", source = "original")
  WorkoutDto enrichOriginal(WorkoutDto dto, String original);

  /** Обогащение дто keycloakId. */
  @Mapping(target = "keycloakId", source = "keycloakId")
  WorkoutDto enrichKeycloakId(WorkoutDto dto, String keycloakId);
}
