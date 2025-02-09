package center.myfit.mapper;

import center.myfit.starter.dto.WorkoutImageDto;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер WorkoutImageMapper. */
@Mapper
public interface WorkoutImageMapper {

  /** Обогащение дто ссылками mobileUrl и desktopUrl. */
  @Mapping(
      target = "image",
      expression = "java(new WorkoutImageDto.ImageDto(originalUrl, mobileUrl, desktopUrl))")
  @Mapping(target = "workoutId", source = "workoutId")
  WorkoutImageDto toWorkoutImageDto(
      @NotBlank Long workoutId, String originalUrl, String mobileUrl, String desktopUrl);
}
