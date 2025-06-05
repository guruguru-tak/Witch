package com.ssafy.witch.group;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinRequestApprovedNotification {

  private final String groupId;
  private final String groupName;
  private final List<String> targetUserFcmTokens;
  private final String joinUserNickname;
  private final String joinUserFcmToken;

}
