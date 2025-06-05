package com.ssafy.witch.controller.user;

import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.ValidateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserValidationController {

  private final ValidateUserUseCase validateUserUseCase;

  @GetMapping("/users/email/is-unique")
  public WitchApiResponse<Void> checkEmailUnique(@RequestParam String email) {
    validateUserUseCase.checkUserEmailDuplication(email);
    return WitchApiResponse.success();
  }

  @GetMapping("/users/nickname/is-unique")
  public WitchApiResponse<Void> checkNicknameUnique(@RequestParam String nickname) {
    validateUserUseCase.checkUserNicknameDuplication(nickname);
    return WitchApiResponse.success();
  }

}
