package com.ssafy.witch.controller.group.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupDetailResponse {

  private String groupId;
  private String name;
  private String groupImageUrl;

  private boolean isLeader;
  private int cntLateArrival;

}
