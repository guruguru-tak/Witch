package com.ssafy.witch.appointment;

import com.ssafy.witch.apoointment.AppointmentMemberPositionCachePort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.appointment.output.AppointMemberPositionListOutput;
import com.ssafy.witch.appointment.output.AppointMemberPositionOutput;
import com.ssafy.witch.exception.appointment.AppointmentNotOnGoingException;
import com.ssafy.witch.exception.appointment.NotJoinedAppointmentException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadAppointmentMemberPositionService implements ReadAppointmentMemberPositionUseCase {

  private final AppointmentMemberPositionCachePort appointmentMemberPositionCachePort;
  private final OnGoingAppointmentCachePort ongoingAppointmentCachePort;

  @Override
  public AppointMemberPositionListOutput readAppointmentMemberPositionList(String userId,
      String appointmentId) {

    AppointmentDetailProjection appointmentDetailProjection =
        ongoingAppointmentCachePort.get(appointmentId);

    if (appointmentDetailProjection == null) {
      throw new AppointmentNotOnGoingException();
    }

    List<AppointmentMemberProjection> members = appointmentDetailProjection.getMembers();

    members.stream()
        .filter(member -> member.getUserId().equals(userId))
        .findFirst().orElseThrow(NotJoinedAppointmentException::new);

    return new AppointMemberPositionListOutput(members.stream().map(member -> {
          Position position = appointmentMemberPositionCachePort.get(member.getUserId());
          Double latitude = position == null ? null : position.getLatitude();
          Double longitude = position == null ? null : position.getLongitude();
          return new AppointMemberPositionOutput(
              member.getUserId(),
              member.getNickname(),
              member.getProfileImageUrl(),
              latitude,
              longitude
          );
        }
    ).toList());
  }
}
