package com.ssafy.witch.group;

import com.ssafy.witch.group.output.GroupDetailOutput;
import com.ssafy.witch.group.output.GroupWithLeaderListOutput;

public interface GroupReadUseCase {

  Group getGroupPreview(String groupId);

  GroupWithLeaderListOutput getGroupWithLeaderList(String userId);

  GroupDetailOutput getGroupDetail(String userId, String groupId);
}
