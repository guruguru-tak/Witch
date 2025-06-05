package com.ssafy.witch.controller.user.mapper;

import com.ssafy.witch.controller.user.request.ConfirmUserEmailVerificationCodeRequest;
import com.ssafy.witch.controller.user.request.UpdateProfileImageRequest;
import com.ssafy.witch.controller.user.request.UserEmailVerificationCodeRequest;
import com.ssafy.witch.controller.user.request.UserNicknameChangeRequest;
import com.ssafy.witch.controller.user.request.UserPasswordChangeRequest;
import com.ssafy.witch.controller.user.request.UserSignupRequest;
import com.ssafy.witch.user.command.ChangeUserNicknameCommand;
import com.ssafy.witch.user.command.ChangeUserPasswordCommand;
import com.ssafy.witch.user.command.CreateUserEmailVerificationCodeCommand;
import com.ssafy.witch.user.command.SignupUserCommand;
import com.ssafy.witch.user.command.UpdateProfileImageCommand;
import com.ssafy.witch.user.command.VerifyUserEmailVerificationCodeCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

  CreateUserEmailVerificationCodeCommand toCommand(UserEmailVerificationCodeRequest request);

  VerifyUserEmailVerificationCodeCommand toCommand(ConfirmUserEmailVerificationCodeRequest request);

  SignupUserCommand toCommand(UserSignupRequest request);

  ChangeUserNicknameCommand toCommand(String userId, UserNicknameChangeRequest request);

  ChangeUserPasswordCommand toCommand(String userId, UserPasswordChangeRequest request);

  UpdateProfileImageCommand toCommand(String userId, UpdateProfileImageRequest request);

}
