package com.ssafy.witch.group.output;

import com.ssafy.witch.user.output.UserBasicOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupJoinRequestOutput {

  private String joinRequestId;
  private UserBasicOutput user;
}
