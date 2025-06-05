package com.ssafy.witch.group;

import com.ssafy.witch.group.output.GroupMemberListOutput;
import com.ssafy.witch.group.query.GroupMemberListQuery;

public interface ReadGroupMemberUseCase {

  GroupMemberListOutput getGroupMembers(GroupMemberListQuery query);
}
