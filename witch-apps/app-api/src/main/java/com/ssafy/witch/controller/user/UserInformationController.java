package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.mapper.UserResponseMapper;
import com.ssafy.witch.controller.user.request.UserNicknameChangeRequest;
import com.ssafy.witch.controller.user.request.UserPasswordChangeRequest;
import com.ssafy.witch.controller.user.response.UserDetailResponse;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.ChangeUserInformationUseCase;
import com.ssafy.witch.user.ReadUserUseCase;
import com.ssafy.witch.user.User;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import com.ssafy.witch.user.command.ChangeUserPasswordCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserInformationController {

  private final ChangeUserInformationUseCase changeUserInformationUseCase;
  private final ReadUserUseCase readUserUseCase;

  private final UserRequestMapper userRequestMapper;
  private final UserResponseMapper userResponseMapper;

  @GetMapping("/users/me")
  public WitchApiResponse<UserDetailResponse> changeNickname(
      @AuthenticationPrincipal String userId) {
    User user = readUserUseCase.getUser(userId);
    UserDetailResponse response = userResponseMapper.toDetailResponse(user);
    return WitchApiResponse.success(response);
  }

  @PatchMapping("/users/me/nickname")
  public WitchApiResponse<Void> changeNickname(
      @AuthenticationPrincipal String userId,
      @RequestBody UserNicknameChangeRequest request) {
    ChangeUserNicknameCommand command = userRequestMapper.toCommand(userId, request);
    changeUserInformationUseCase.changeUserNickname(command);
    return WitchApiResponse.success();
  }

  @PatchMapping("/users/me/password")
  public WitchApiResponse<Void> changePassword(
      @AuthenticationPrincipal String userId,
      @RequestBody UserPasswordChangeRequest request) {
    ChangeUserPasswordCommand command = userRequestMapper.toCommand(userId, request);
    changeUserInformationUseCase.changePassword(command);
    return WitchApiResponse.success();
  }

}
