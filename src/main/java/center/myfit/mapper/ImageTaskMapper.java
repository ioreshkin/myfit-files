package center.myfit.mapper;

import center.myfit.starter.dto.ExerciseDto;
import center.myfit.starter.dto.ImageTaskDto;
import center.myfit.starter.dto.WorkoutDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер ImageTaskDto. */
@Mapper
public interface ImageTaskMapper {

  /** Маппинг ExerciseDto в ImageTaskDto. */
  @Mapping(target = "objectId", source = "id")
  @Mapping(target = "entityType", expression = "java(EntityType.EXERCISE)")
  ImageTaskDto toImageTask(ExerciseDto dto);

  /** Маппинг WorkoutDto в ImageTaskDto. */
  @Mapping(target = "objectId", source = "id")
  @Mapping(target = "entityType", expression = "java(EntityType.WORKOUT)")
  ImageTaskDto toImageTask(WorkoutDto dto);
}
