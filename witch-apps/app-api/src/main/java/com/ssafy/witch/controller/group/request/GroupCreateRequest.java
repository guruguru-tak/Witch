package com.ssafy.witch.controller.group.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupCreateRequest {

  private String name;
  private String groupImageObjectKey;

}
