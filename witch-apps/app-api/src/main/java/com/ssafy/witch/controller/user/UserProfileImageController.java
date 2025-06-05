package com.ssafy.witch.controller.user;

import com.ssafy.witch.controller.user.mapper.UserRequestMapper;
import com.ssafy.witch.controller.user.request.UpdateProfileImageRequest;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.file.response.PresignedUrlResponse;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.user.ProfileImageUseCase;
import com.ssafy.witch.user.command.UpdateProfileImageCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserProfileImageController {

  private final ProfileImageUseCase profileImageUseCase;

  private final UserRequestMapper userRequestMapper;
  private final FileRequestMapper fileRequestMapper;
  private final FileResponseMapper fileResponseMapper;

  @PostMapping("/users/me/profile-image/presigned-url")
  public WitchApiResponse<PresignedUrlResponse> generatePresignedUrl(
      @AuthenticationPrincipal String userId,
      @RequestBody PresignedUrlRequest request) {
    GeneratePresignedUrlCommand command = fileRequestMapper.toCommand(userId, request);
    PresignedUrl presignedUrl = profileImageUseCase.generateProfileImagePresignedUrl(command);
    PresignedUrlResponse response = fileResponseMapper.toResponse(presignedUrl);

    return WitchApiResponse.success(response);
  }

  @PatchMapping("/users/me/profile-image")
  public WitchApiResponse<Void> updateProfileImage(
      @AuthenticationPrincipal String userId,
      @RequestBody UpdateProfileImageRequest request) {

    UpdateProfileImageCommand command = userRequestMapper.toCommand(userId, request);
    profileImageUseCase.updateProfileImageUrl(command);
    return WitchApiResponse.success();
  }
}
