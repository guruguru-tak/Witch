package com.ssafy.witch.controller.snack.mapper;

import com.ssafy.witch.controller.snack.response.SnackDetailResponse;
import com.ssafy.witch.controller.snack.response.SnackListResponse;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.snack.output.SnackListOutput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackResponseMapper {

  SnackDetailResponse toDetailResponse(SnackDetailOutput snack);

  SnackListResponse toResponse(SnackListOutput output);
}
