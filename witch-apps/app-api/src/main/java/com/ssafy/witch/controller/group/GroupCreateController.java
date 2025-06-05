package com.ssafy.witch.controller.group;

import com.ssafy.witch.controller.group.mapper.GroupRequestMapper;
import com.ssafy.witch.controller.group.request.GroupCreateRequest;
import com.ssafy.witch.group.CreateGroupUseCase;
import com.ssafy.witch.group.command.GroupCreateCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupCreateController {

  private final CreateGroupUseCase createGroupUseCase;
  private final GroupRequestMapper groupRequestMapper;

  @PostMapping("/groups")
  public WitchApiResponse<Void> creatGroup(
      @AuthenticationPrincipal String userId,
      @RequestBody GroupCreateRequest request) {
    GroupCreateCommand command = groupRequestMapper.toCommand(userId, request);
    createGroupUseCase.createGroup(command);
    return WitchApiResponse.success();
  }
}
