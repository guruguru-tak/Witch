package com.ssafy.witch.controller.snack;

import com.ssafy.witch.controller.snack.mapper.SnackResponseMapper;
import com.ssafy.witch.controller.snack.response.SnackDetailResponse;
import com.ssafy.witch.controller.snack.response.SnackListResponse;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.snack.ReadSnackUseCase;
import com.ssafy.witch.snack.Snack;
import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.snack.output.SnackListOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SnackReadController {

  private final ReadSnackUseCase readSnackUseCase;
  private final SnackResponseMapper snackResponseMapper;

  @GetMapping("snacks/{snackId}")
  public WitchApiResponse<SnackDetailResponse> getSnackDetail(
      @AuthenticationPrincipal String userId,
      @PathVariable String snackId) {

    SnackDetailOutput snack = readSnackUseCase.getSnackDetail(userId, snackId);
    SnackDetailResponse response = snackResponseMapper.toDetailResponse(snack);
    return WitchApiResponse.success(response);
  }

  @GetMapping("appointments/{appointmentId}/snacks")
  public WitchApiResponse<SnackListResponse> getSnacks(
      @AuthenticationPrincipal String userId,
      @PathVariable String appointmentId) {

    SnackListOutput output = readSnackUseCase.getSnacks(userId, appointmentId);
    SnackListResponse response = snackResponseMapper.toResponse(output);

    return WitchApiResponse.success(response);
  }
}
