package com.ssafy.witch.controller.group;


import com.ssafy.witch.controller.group.mapper.GroupRequestMapper;
import com.ssafy.witch.controller.group.request.GroupNameChangeRequest;
import com.ssafy.witch.group.ChangeGroupInformationUseCase;
import com.ssafy.witch.group.command.ChangeGroupNameCommand;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupInformationController {

  private final ChangeGroupInformationUseCase changeGroupInformationUseCase;
  private final GroupRequestMapper groupRequestMapper;

  @PatchMapping("/groups/{groupId}/name")
  public WitchApiResponse<Void> changeGroupName(
      @AuthenticationPrincipal String userId,
      @PathVariable String groupId,
      @RequestBody GroupNameChangeRequest request) {

    ChangeGroupNameCommand command = groupRequestMapper.toCommand(userId, groupId, request);
    changeGroupInformationUseCase.changeGroupName(command);

    return WitchApiResponse.success();
  }

}
