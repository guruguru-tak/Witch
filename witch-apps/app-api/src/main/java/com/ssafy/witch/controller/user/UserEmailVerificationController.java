package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.ConfirmUserEmailVerificationCodeRequest;
import com.ssafy.witch.controller.user.request.UserEmailVerificationCodeRequest;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.VerifyUserEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserEmailVerificationController {

  private final VerifyUserEmailUseCase verifyUserEmailUseCase;
  private final UserRequestMapper mapper;

  @PostMapping("/users/email-verification-code")
  public WitchApiResponse<Void> generateUserEmailVerificationCode(
      @RequestBody UserEmailVerificationCodeRequest request) {
    verifyUserEmailUseCase.createUserEmailVerificationCode(mapper.toCommand(request));
    return WitchApiResponse.success();
  }

  @PostMapping("/users/email-verification-code/confirm")
  public WitchApiResponse<Void> confirmUserEmailVerificationCode(
      @RequestBody ConfirmUserEmailVerificationCodeRequest request) {
    verifyUserEmailUseCase.verifyEmailVerificationCode(mapper.toCommand(request));
    return WitchApiResponse.success();
  }
}
