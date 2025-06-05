package com.ssafy.witch.controller.group.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupMemberListResponse {

  private List<GroupMemberResponse> members;

}
