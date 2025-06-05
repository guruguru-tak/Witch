package com.ssafy.witch.group.output;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupJoinRequestListOutput {

  private List<GroupJoinRequestOutput> joinRequests;
}
