package com.ssafy.witch.controller.snack.mapper;

import com.ssafy.witch.controller.snack.request.SnackCreateRequest;
import com.ssafy.witch.snack.command.SnackCreateCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnackRequestMapper {

  SnackCreateCommand toCommand(String userId, String appointmentId, SnackCreateRequest request);
}
