package com.ssafy.witch.controller.group;

import com.ssafy.witch.controller.group.mapper.GroupRequestMapper;
import com.ssafy.witch.controller.group.request.UpdateGroupImageRequest;
import com.ssafy.witch.file.PresignedUrl;
import com.ssafy.witch.file.command.GeneratePresignedUrlCommand;
import com.ssafy.witch.file.mapper.FileRequestMapper;
import com.ssafy.witch.file.mapper.FileResponseMapper;
import com.ssafy.witch.file.request.PresignedUrlRequest;
import com.ssafy.witch.file.response.PresignedUrlResponse;
import com.ssafy.witch.group.GroupImageUseCase;
import com.ssafy.witch.group.command.UpdateGroupImageCommand;
import com.ssafy.witch.response.WitchApiResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupImageController {

  private final GroupImageUseCase groupImageUseCase;

  private final FileRequestMapper fileRequestMapper;
  private final FileResponseMapper fileResponseMapper;
  private final GroupRequestMapper groupRequestMapper;

  @PostMapping("/groups/group-image/presigned-url")
  public WitchApiResponse<PresignedUrlResponse> generatePresignedUrl(
      @AuthenticationPrincipal String userId,
      @RequestBody PresignedUrlRequest request) {
    GeneratePresignedUrlCommand command = fileRequestMapper.toCommand(userId, request);
    PresignedUrl presignedUrl = groupImageUseCase.generateGroupImagePresignedUrl(command);
    PresignedUrlResponse response = fileResponseMapper.toResponse(presignedUrl);

    return WitchApiResponse.success(response);
  }

  @PatchMapping("/groups/{groupId}/image")
  public WitchApiResponse<Void> updateGroupImage(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId,
      @RequestBody UpdateGroupImageRequest request) {

    UpdateGroupImageCommand command = groupRequestMapper.toCommand(userId, groupId, request);
    groupImageUseCase.updateGroupImageUrl(command);

    return WitchApiResponse.success();

  }


}
