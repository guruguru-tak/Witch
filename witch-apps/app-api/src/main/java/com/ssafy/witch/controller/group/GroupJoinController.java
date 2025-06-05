package com.ssafy.witch.controller.group;

import com.ssafy.witch.controller.group.mapper.GroupResponseMapper;
import com.ssafy.witch.controller.group.response.GroupJoinRequestListResponse;
import com.ssafy.witch.controller.group.response.GroupListResponse;
import com.ssafy.witch.group.CreateGroupJoinRequestUseCase;
import com.ssafy.witch.group.HandleGroupJoinRequestUseCase;
import com.ssafy.witch.group.command.ApproveGroupJoinRequestCommand;
import com.ssafy.witch.group.command.GetGroupJoinRequestListCommand;
import com.ssafy.witch.group.command.GroupJoinRequestCreateCommand;
import com.ssafy.witch.group.command.RejectGroupJoinRequestCommand;
import com.ssafy.witch.group.output.GroupJoinRequestListOutput;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupJoinController {

  private final CreateGroupJoinRequestUseCase createGroupJoinRequestUseCase;
  private final HandleGroupJoinRequestUseCase handleGroupJoinRequestUseCase;
  private final GroupResponseMapper groupResponseMapper;


  @PostMapping("/groups/{groupId}/join-requests")
  public WitchApiResponse<Void> createJoinRequests(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {

    GroupJoinRequestCreateCommand command =
        new GroupJoinRequestCreateCommand(userId, groupId);

    createGroupJoinRequestUseCase.creatGroupJoinRequest(command);

    return WitchApiResponse.success();
  }

  @PostMapping("/groups/join-requests/{joinRequestId}/approve")
  public WitchApiResponse<Void> approveJoinRequest(
      @AuthenticationPrincipal String userId,
      @PathVariable("joinRequestId") String joinRequestId) {

    ApproveGroupJoinRequestCommand command = new ApproveGroupJoinRequestCommand(userId,
        joinRequestId);

    handleGroupJoinRequestUseCase.approveGroupJoinRequest(command);

    return WitchApiResponse.success();
  }


  @DeleteMapping("/groups/join-requests/{joinRequestId}")
  public WitchApiResponse<Void> rejectJoinRequest(
      @AuthenticationPrincipal String userId,
      @PathVariable("joinRequestId") String joinRequestId) {

    RejectGroupJoinRequestCommand command = new RejectGroupJoinRequestCommand(userId,
        joinRequestId);

    handleGroupJoinRequestUseCase.rejectGroupJoinRequest(command);

    return WitchApiResponse.success();
  }

  @GetMapping("/groups/{groupId}/join-requests")
  public WitchApiResponse<GroupJoinRequestListResponse> getJoinRequests(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {

    GetGroupJoinRequestListCommand command = new GetGroupJoinRequestListCommand(userId, groupId);

    GroupJoinRequestListOutput output = handleGroupJoinRequestUseCase.getGroupJoinRequestList(command);

    GroupJoinRequestListResponse response = groupResponseMapper.toGroupJoinRequestListResponse(output);

    return WitchApiResponse.success(response);
  }

}
