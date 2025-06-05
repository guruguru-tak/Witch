package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.UserSignupRequest;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.SignupUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserSignupController {

  private final UserRequestMapper userRequestMapper;

  private final SignupUserUseCase signupUserUseCase;

  @PostMapping("/users")
  public WitchApiResponse<Void> signup(@RequestBody UserSignupRequest request) {
    signupUserUseCase.signup(userRequestMapper.toCommand(request));
    return WitchApiResponse.success();
  }
}
