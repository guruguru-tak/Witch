package com.ssafy.witch.controller.group;

import com.ssafy.witch.controller.group.mapper.GroupMemberResponseMapper;
import com.ssafy.witch.controller.group.response.GroupMemberListResponse;
import com.ssafy.witch.group.ReadGroupMemberUseCase;
import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.query.GroupMemberListQuery;
import com.ssafy.witch.response.WitchApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GroupMemberReadController {

  private final ReadGroupMemberUseCase readGroupMemberUseCase;
  private final GroupMemberResponseMapper groupMemberResponseMapper;

  @GetMapping("/groups/{groupId}/members")
  public WitchApiResponse<GroupMemberListResponse> getGroupMemberList(
      @AuthenticationPrincipal String userId,
      @PathVariable("groupId") String groupId) {
    GroupMemberListQuery query = new GroupMemberListQuery(userId, groupId);
    GroupMemberListOutput output = readGroupMemberUseCase.getGroupMembers(query);
    GroupMemberListResponse response = groupMemberResponseMapper.toResponse(output);
    return WitchApiResponse.success(response);
  }
}
