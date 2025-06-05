package com.ssafy.witch.controller.group.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupJoinRequestListResponse {

  private List<GroupJoinRequestResponse> joinRequests;
}
