package com.ssafy.witch.controller.group.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.witch.controller.user.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupResponse {

  private String groupId;
  private String name;
  private String groupImageUrl;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String createdAt;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UserResponse leader;

}
