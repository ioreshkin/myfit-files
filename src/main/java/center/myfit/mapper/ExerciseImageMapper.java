package center.myfit.mapper;

import center.myfit.starter.dto.ExerciseImageDto;
import jakarta.validation.constraints.NotBlank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Маппер ExerciseImageDto. */
@Mapper
public interface ExerciseImageMapper {

  /** Обогащение дто ссылками  mobileUrl и desktopUrl. */
  @Mapping(target = "image",
      expression = "java(new ExerciseImageDto.ImageDto(originalUrl, mobileUrl, desktopUrl))")

  @Mapping(target = "exerciseId", source = "exerciseId")
  ExerciseImageDto toExerciseImageDto(@NotBlank Long exerciseId, String originalUrl,
                                      String mobileUrl, String desktopUrl);
}
