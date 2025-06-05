package com.ssafy.witch.controller.snack;

import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.file.response.PresignedUrlResponse;
import com.ssafy.witch.response.WitchApiResponse;
import com.ssafy.witch.snack.SnackFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SnackSoundController {

  private final SnackFileUseCase snackFileUseCase;
  private final FileRequestMapper fileRequestMapper;
  private final FileResponseMapper fileResponseMapper;

  @PostMapping("/snacks/snack-sound/presigned-url")
  public WitchApiResponse<PresignedUrlResponse> generateSoundPresignedUrl(
      @AuthenticationPrincipal String userId,
      @RequestBody PresignedUrlRequest request) {

    GeneratePresignedUrlCommand command = fileRequestMapper.toCommand(userId, request);
    PresignedUrl presignedUrl = snackFileUseCase.generateSnackFilePresignedUrl(command);
    PresignedUrlResponse response = fileResponseMapper.toResponse(presignedUrl);

    return WitchApiResponse.success(response);
  }

}
