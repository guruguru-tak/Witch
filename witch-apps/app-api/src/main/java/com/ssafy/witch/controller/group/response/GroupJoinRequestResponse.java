package com.ssafy.witch.controller.group.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ssafy.witch.controller.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupJoinRequestResponse {

  private String joinRequestId;

  @JsonInclude(Include.NON_NULL)
  private UserResponse user;
}
