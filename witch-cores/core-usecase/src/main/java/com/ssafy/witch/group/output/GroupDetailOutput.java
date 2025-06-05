package com.ssafy.witch.group.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupDetailOutput {

  private String groupId;
  private String name;
  private String groupImageUrl;

  private boolean isLeader;
  private int cntLateArrival;

}
