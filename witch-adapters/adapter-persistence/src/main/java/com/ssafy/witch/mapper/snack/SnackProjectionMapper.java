package com.ssafy.witch.mapper.snack;

import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;
import com.ssafy.witch.entity.Snack.SnackEntityProjection;
import com.ssafy.witch.snack.model.SnackDetailProjection;
import com.ssafy.witch.snack.model.SnackProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackProjectionMapper {

  SnackProjection toProjection(SnackEntityProjection snackEntityProjection);
}
