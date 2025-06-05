package com.ssafy.witch.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinRequestCreateNotification {

  private final String groupId;
  private final String groupName;
  private final String targetUserFcmToken;
  private final String requestUserNickname;

}
