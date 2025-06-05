package com.ssafy.witch.group;

import com.ssafy.witch.event.GroupEventTopic;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyGroupFcmAdapter implements NotifyGroupPort {

  private final FcmNotificator fcmNotificator;

  @Override
  public void notifyJoinRequestApproved(JoinRequestApprovedNotification notification) {
    String groupName = notification.getGroupName();
    String joinUserNickname = notification.getJoinUserNickname();

    String joinUserTitle = String.format("[Witch] %s 모임 참여가 수락되었어요.", groupName);
    String title = String.format("[Witch] %s 모임에 %s 님이 참여했어요.", groupName, joinUserNickname);
    String body = "구성원 목록을 확인해 보세요!";

    Map<String, String> data =
        createGroupData(GroupEventTopic.GROUP_JOIN_REQUEST_APPROVE, notification.getGroupId());

    String fcmToken = notification.getJoinUserFcmToken();
    WitchNotification joinUserNotification =
        new WitchNotification(data, fcmToken, joinUserTitle, body);
    fcmNotificator.sendNotification(joinUserNotification);

    List<String> userFcmTokens = notification.getTargetUserFcmTokens();
    for (String userFcmToken : userFcmTokens) {
      WitchNotification witchNotification = new WitchNotification(data, userFcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyJoinRequestRejected(JoinRequestRejectedNotification notification) {
    String groupName = notification.getGroupName();

    String title = String.format("[Witch] %s 모임 참여가 거절되었어요.", groupName);
    String body = "참여 요청을 다시 시도해보세요.";

    Map<String, String> data =
        createGroupData(GroupEventTopic.GROUP_JOIN_REQUEST_REJECT, notification.getGroupId());

    String fcmToken = notification.getJoinUserFcmToken();
    WitchNotification joinUserNotification =
        new WitchNotification(data, fcmToken, title, body);
    fcmNotificator.sendNotification(joinUserNotification);

  }

  @Override
  public void notifyJoinRequestCreated(JoinRequestCreateNotification notification) {
    String title = String.format("[Witch] %s 모임으로 참여 요청이 도착했어요.", notification.getGroupName());
    String body = String.format("%s 님이 참여를 요청했어요!", notification.getRequestUserNickname());

    Map<String, String> data =
        createGroupData(GroupEventTopic.GROUP_JOIN_REQUEST, notification.getGroupId());

    String fcmToken = notification.getTargetUserFcmToken();
    WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
    fcmNotificator.sendNotification(witchNotification);
  }

  private Map<String, String> createGroupData(String groupType, String groupId) {
    return Map.of("type", groupType, "parameter", groupId);
  }

}
