package com.ssafy.witch.group.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupDetailProjection {

  private String groupId;
  private String name;
  private String groupImageUrl;

  private boolean isLeader;
  private int cntLateArrival;
}
