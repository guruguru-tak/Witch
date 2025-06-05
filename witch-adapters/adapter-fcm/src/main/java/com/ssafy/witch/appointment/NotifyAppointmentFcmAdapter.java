package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentArrivalNotification;
import com.ssafy.witch.apoointment.AppointmentCreatedNotification;
import com.ssafy.witch.apoointment.AppointmentEndNotification;
import com.ssafy.witch.apoointment.AppointmentExitNotification;
import com.ssafy.witch.apoointment.AppointmentJoinNotification;
import com.ssafy.witch.apoointment.AppointmentStartNotification;
import com.ssafy.witch.apoointment.NotifyAppointmentPort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.event.AppointmentEventTopic;
import com.ssafy.witch.group.GroupMemberUser;
import com.ssafy.witch.group.GroupWithMemberUsers;
import com.ssafy.witch.notification.FcmNotificator;
import com.ssafy.witch.notification.WitchNotification;
import com.ssafy.witch.user.UserNotification;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotifyAppointmentFcmAdapter implements NotifyAppointmentPort {

  private final FcmNotificator fcmNotificator;


  @Override
  public void notifyJoinAppointment(AppointmentJoinNotification appointmentJoinNotification) {
    String appointmentId = appointmentJoinNotification.getAppointmentId();
    List<UserNotification> members = appointmentJoinNotification.getMembers();
    String name = appointmentJoinNotification.getName();
    String joinUserId = appointmentJoinNotification.getJoinUserId();
    UserNotification joinUser = members.stream()
        .filter(u -> u.getUserId().equals(joinUserId))
        .findFirst()
        .orElseThrow();
    String joinUserNickname = joinUser.getNickname();

    String title = String.format("[Witch] %s 약속에 %s 님이 참여했어요.", name, joinUserNickname);
    String body = "약속 참여자 목록을 확인해 보세요!";

    Map<String, String> data =
        createAppointmentData(AppointmentEventTopic.JOIN_APPOINTMENT, appointmentId);

    for (UserNotification member : appointmentJoinNotification.getMembers()) {
      if (member.getUserId().equals(joinUserId)) {
        continue;
      }

      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyStartAppointment(AppointmentStartNotification notification) {

    String appointmentName = notification.getName();
    String appointmentId = notification.getAppointmentId();

    String title = String.format("[Witch] %s 약속이 시작되었어요", appointmentName);
    String body = "약속 참여자들의 위치를 확인해 보세요!";

    Map<String, String> data =
        createAppointmentData(AppointmentEventTopic.START_APPOINTMENT, appointmentId);

    for (UserNotification member : notification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyEndAppointment(AppointmentEndNotification notification) {
    String appointmentId = notification.getAppointmentId();

    String title = String.format("[Witch] %s 약속이 종료되었어요.", notification.getName());
    String body = "약속 참여자들의 최종 위치를 확인해 보세요!";

    Map<String, String> data =
        createAppointmentData(AppointmentEventTopic.END_APPOINTMENT, appointmentId);

    for (UserNotification member : notification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(data, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyArrivalAppointment(AppointmentArrivalNotification notification) {
    String appointmentName = notification.getName();
    String appointmentId = notification.getAppointmentId();
    UserNotification arrivalUser = notification.getMembers()
        .stream()
        .filter(member -> member.getUserId().equals(notification.getArrivalUserId()))
        .findAny().orElseThrow();

    String title =
        String.format("%s 약속에 %s 님이 도착했어요.", appointmentName, arrivalUser.getNickname());
    String body = "약속 참여자들의 위치를 확인해 보세요!";

    Map<String, String> appointmentData = createAppointmentData(
        AppointmentEventTopic.ARRIVAL_APPOINTMENT, appointmentId);

    for (UserNotification member : notification.getMembers()) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification = new WitchNotification(appointmentData, fcmToken, title,
          body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  @Override
  public void notifyAppointmentCreated(AppointmentCreatedNotification notification) {
    Appointment appointment = notification.getAppointment();
    GroupWithMemberUsers group = notification.getGroup();
    String createUserId = notification.getCreateUserId();
    List<GroupMemberUser> groupMemberUsers = group.getGroupMemberUsers();

    String title = String.format("%s 모임에 %s 약속이 생성 되었습니다.", group.getGroup().getName(),
        appointment.getName());
    String body = "약속을 확인해 보세요!";

    Map<String, String> appointmentData = createAppointmentData(
        AppointmentEventTopic.APPOINTMENT_CREATED, appointment.getAppointmentId());

    for (GroupMemberUser member : groupMemberUsers) {
      String userId = member.getUserWithFcmToken().getUser().getUserId();
      if (createUserId.equals(userId)) {
        continue;
      }
      String fcmToken = member.getUserWithFcmToken().getFcmToken();
      WitchNotification witchNotification = new WitchNotification(appointmentData, fcmToken, title,
          body);
      fcmNotificator.sendNotification(witchNotification);
    }

  }

  @Override
  public void notifyAppointmentExit(AppointmentExitNotification notification) {
    AppointmentDetailProjection appointment = notification.getAppointment();
    String exitUserNickname = notification.getExitUser().getNickname();

    String title = String.format("%s 약속에서 %s 님이 탈했어요.", appointment.getName(), exitUserNickname);
    String body = "약속 참여자 목록을 확인해 보세요!";

    Map<String, String> appointmentData = createAppointmentData(
        AppointmentEventTopic.APPOINTMENT_EXIT, appointment.getAppointmentId());

    List<AppointmentMemberProjection> members = appointment.getMembers();
    for (AppointmentMemberProjection member : members) {
      String fcmToken = member.getFcmToken();
      WitchNotification witchNotification =
          new WitchNotification(appointmentData, fcmToken, title, body);
      fcmNotificator.sendNotification(witchNotification);
    }
  }

  private Map<String, String> createAppointmentData(String appointmentType,
      String appointmentId) {
    return Map.of("type", appointmentType, "parameter", appointmentId);
  }

}
