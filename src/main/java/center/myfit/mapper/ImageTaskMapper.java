package center.myfit.mapper;

import center.myfit.starter.dto.ExerciseDto;
import center.myfit.starter.dto.ImageTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер ImageTaskDto. */
@Mapper
public interface ImageTaskMapper {
  /** Маппинг ExerciseDto в ImageTaskDto. */
  @Mapping(target = "exerciseId", source = "id")
  ImageTaskDto toImageTask(ExerciseDto dto);
}
