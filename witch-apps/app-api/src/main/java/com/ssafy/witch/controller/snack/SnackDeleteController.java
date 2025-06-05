package com.ssafy.witch.controller.snack;

import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.snack.DeleteSnackUseCase;
import com.ssafy.witch.snack.command.SnackDeleteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SnackDeleteController {

  private final DeleteSnackUseCase deleteSnackUseCase;

  @DeleteMapping("/snack/{snackId}")
  public WitchApiResponse<Void> deleteSnack(
      @AuthenticationPrincipal String userId,
      @PathVariable String snackId
  ) {

    SnackDeleteCommand command = new SnackDeleteCommand(userId, snackId);
    deleteSnackUseCase.deleteSnack(command);

    return WitchApiResponse.success();
  }
}
