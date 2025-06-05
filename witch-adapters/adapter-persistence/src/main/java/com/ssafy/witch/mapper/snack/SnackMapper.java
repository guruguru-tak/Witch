package com.ssafy.witch.mapper.snack;

import com.ssafy.witch.entity.Snack.SnackEntity;
import com.ssafy.witch.snack.Snack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackMapper {

  SnackEntity toEntity(Snack snack);

  Snack toDomain(SnackEntity snackEntity);
}
