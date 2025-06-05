package com.ssafy.witch.snack;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentMemberPositionCachePort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.appointment.Position;
import com.ssafy.witch.common.DistanceUtils;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.exception.snack.SnackViewNotAvailableException;
import com.ssafy.witch.snack.mapper.SnackOutputMapper;
import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.snack.output.SnackListOutput;
import com.ssafy.witch.user.output.UserBasicOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReadSnackService implements ReadSnackUseCase {


  private final SnackPort snackPort;
  private final AppointmentMemberPort appointmentMemberPort;
  private final OnGoingAppointmentCachePort onGoingAppointmentCachePort;
  private final AppointmentMemberPositionCachePort appointmentMemberPositionCachePort;
  private final AppointmentPort appointmentPort;
  private final SnackReadPort snackReadPort;

  private final SnackOutputMapper snackOutputMapper;

  private static AppointmentMemberProjection getMemberProjection(String userId,
      AppointmentDetailProjection appointmentDetailProjection) {
    return appointmentDetailProjection.getMembers()
        .stream()
        .filter(member -> member.getUserId().equals(userId))
        .findFirst()
        .orElseThrow(UnauthorizedAppointmentAccessException::new);
  }

  private static void validateAppointmentOngoing(
      AppointmentDetailProjection appointmentDetailProjection) {
    if (appointmentDetailProjection == null) {
      throw new NotOngoingAppointmentException();
    }
  }

  @Override
  public SnackDetailOutput getSnackDetail(String userId, String snackId) {

    Snack snack = snackPort.findById(snackId).orElseThrow(SnackNotFoundException::new);

    String appointmentId = snack.getAppointmentId();
    AppointmentDetailProjection appointment =
        onGoingAppointmentCachePort.get(appointmentId);

    //진행 중인 약속인지 검증
    validateAppointmentOngoing(appointment);

    //해당 사용자가 약속의 멤버인지 검증
    AppointmentMemberProjection viewerProjection = getMemberProjection(userId,
        appointment);

    onGoingAppointmentCachePort.get(appointment.getAppointmentId());

    // 조회한 유저의 위치를 알 수 없으면 스낵 조회 에러
    Position viewerPosition = appointmentMemberPositionCachePort.get(viewerProjection.getUserId());
    if (viewerPosition.getLatitude() == null
        || viewerPosition.getLongitude() == null) {
      throw new SnackViewNotAvailableException();
    }

    //조회를 시도한 사용자가 스낵을 생성한 사용자가 아닌 경우
    if (!snack.getUserId().equals(userId)) {
      Position appointmentPosition = new Position(appointment.getLatitude(),
          appointment.getLongitude());
      Position snackPosition = new Position(snack.getLatitude(), snack.getLongitude());
      double targetDist = DistanceUtils.getDistance(appointmentPosition, snackPosition);
      double currentDist = DistanceUtils.getDistance(appointmentPosition, viewerPosition);

      if (currentDist > targetDist + 10) {
        throw new SnackViewNotAvailableException();
      }
    }

    AppointmentMemberProjection snackCreateMember = getMemberProjection(snack.getUserId(),
        appointment);

    return new SnackDetailOutput(
        snackId,
        snack.getAppointmentId(),
        snack.getLongitude(),
        snack.getLatitude(),
        snack.getSnackImageUrl(),
        snack.getSnackSoundUrl(),
        snack.getCreatedAt(),
        new UserBasicOutput(snackCreateMember.getUserId(),
            snackCreateMember.getNickname(),
            snackCreateMember.getProfileImageUrl())
    );
  }

  @Override
  public SnackListOutput getSnacks(String userId, String appointmentId) {

    //약속 존재 하는지 검증
    validateAppointmentExists(appointmentId);

    AppointmentDetailProjection appointment =
        onGoingAppointmentCachePort.get(appointmentId);
    //진행 중인 약속인지 검증
    validateAppointmentOngoing(appointment);
    //해당 사용자가 약속의 멤버인지 검증
    validateUserInAppointment(userId, appointmentId);

    return snackOutputMapper.toOutput(snackReadPort.getSnacks(userId, appointmentId));
  }

  private void validateUserInAppointment(String userId, String appointmentId) {
    if (!appointmentMemberPort.existsByUserIdAndAppointmentId(userId, appointmentId)) {
      throw new UnauthorizedAppointmentAccessException();
    }
  }


  private void validateAppointmentExists(String appointmentId) {
    if (!appointmentPort.existsById(appointmentId)) {
      throw new AppointmentNotFoundException();
    }
  }
}
