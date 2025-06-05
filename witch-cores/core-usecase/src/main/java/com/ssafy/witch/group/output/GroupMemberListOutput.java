package com.ssafy.witch.group.output;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupMemberListOutput {

  private List<GroupMemberOutput> members;
}
