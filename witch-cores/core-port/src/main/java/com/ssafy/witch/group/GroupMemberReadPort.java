package com.ssafy.witch.group;

import com.ssafy.witch.group.model.GroupMemberProjection;
import java.util.List;

public interface GroupMemberReadPort {

  List<GroupMemberProjection> readGroupMemberList(String groupId);

}
