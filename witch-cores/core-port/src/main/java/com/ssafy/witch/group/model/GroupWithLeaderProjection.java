package com.ssafy.witch.group.model;

import com.ssafy.witch.user.model.UserBasicProjection;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupWithLeaderProjection {

  private String groupId;
  private String name;
  private String groupImageUrl;
  private LocalDateTime createdAt;

  private UserBasicProjection leader;

}
