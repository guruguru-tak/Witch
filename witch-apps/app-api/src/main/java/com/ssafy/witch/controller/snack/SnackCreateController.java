package com.ssafy.witch.controller.snack;

import com.ssafy.witch.controller.snack.mapper.SnackRequestMapper;
import com.ssafy.witch.controller.snack.request.SnackCreateRequest;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.snack.CreateSnackUseCase;
import com.ssafy.witch.snack.command.SnackCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SnackCreateController {

  private final SnackRequestMapper snackRequestMapper;
  private final CreateSnackUseCase createSnackUseCase;

  @PostMapping("/appointments/{appointmentId}/snacks")
  public WitchApiResponse<Void> createSnack(
      @AuthenticationPrincipal String userId,
      @PathVariable("appointmentId") String appointmentId,
      @RequestBody SnackCreateRequest request) {

    SnackCreateCommand command = snackRequestMapper.toCommand(userId, appointmentId, request);
    createSnackUseCase.createSnack(command);
    return WitchApiResponse.success();
  }

}
