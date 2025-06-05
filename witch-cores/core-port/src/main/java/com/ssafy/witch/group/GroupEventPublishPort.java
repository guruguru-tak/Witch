package com.ssafy.witch.group;

import com.ssafy.witch.group.event.ApproveGroupJoinRequestEvent;
import com.ssafy.witch.group.event.CreateGroupJoinRequestEvent;
import com.ssafy.witch.group.event.RejectGroupJoinRequestEvent;

public interface GroupEventPublishPort {

  void publish(CreateGroupJoinRequestEvent event);

  void publish(ApproveGroupJoinRequestEvent event);

  void publish(RejectGroupJoinRequestEvent event);
}
