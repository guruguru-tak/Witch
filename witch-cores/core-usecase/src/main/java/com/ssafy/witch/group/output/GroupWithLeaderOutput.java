package com.ssafy.witch.group.output;

import com.ssafy.witch.user.output.UserBasicOutput;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupWithLeaderOutput {

  private String groupId;
  private String name;
  private String groupImageUrl;
  private LocalDateTime createdAt;
  private UserBasicOutput leader;

}
