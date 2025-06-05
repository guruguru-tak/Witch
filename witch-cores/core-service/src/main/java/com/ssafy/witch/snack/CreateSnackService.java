package com.ssafy.witch.snack;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.file.InvalidFileOwnerException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.file.FileOwnerCachePort;
import com.ssafy.witch.file.PresignedUrlPort;
import com.ssafy.witch.snack.command.SnackCreateCommand;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateSnackService implements CreateSnackUseCase {

  private final AppointmentPort appointmentPort;
  private final AppointmentMemberPort appointmentMemberPort;
  private final SnackPort snackPort;

  private final FileOwnerCachePort fileOwnerCachePort;
  private final PresignedUrlPort presignedUrlPort;
  private final OnGoingAppointmentCachePort onGoingAppointmentCachePort;

  public void createSnack(SnackCreateCommand command) {
    String userId = command.getUserId();
    String appointmentId = command.getAppointmentId();
    Double longitude = command.getLongitude();
    Double latitude = command.getLatitude();
    String snackImageObjectKey = command.getSnackImageObjectKey();
    String snackSoundObjectKey = command.getSnackSoundObjectKey();

    AppointmentDetailProjection appointmentDetailProjection = onGoingAppointmentCachePort.get(
        appointmentId);

    validateAppointmentOngoing(appointmentDetailProjection);

    List<AppointmentMemberProjection> members = appointmentDetailProjection.getMembers();

    validateAppointmentAuthorization(members, userId);
    //파일의 소유권이 있는지?
    validateImageOwnership(userId, snackImageObjectKey);

    if (Objects.nonNull(snackSoundObjectKey)) {
      validateSoundOwnership(userId, snackSoundObjectKey);
    }
    //presignedUrlPort.getAccessUrl
    String snackImageUrl = getImageAccessUrl(snackImageObjectKey);
    String snackSoundUrl = getSoundAccessUrl(snackSoundObjectKey);

    Snack newSnack = Snack.createNewSnack(appointmentId, userId, longitude, latitude, snackImageUrl,
        snackSoundUrl);

    snackPort.save(newSnack);
  }

  private static void validateAppointmentOngoing(AppointmentDetailProjection appointmentDetailProjection) {
    if (appointmentDetailProjection == null) {
      throw new NotOngoingAppointmentException();
    }
  }

  private static void validateAppointmentAuthorization(List<AppointmentMemberProjection> members, String userId) {
    boolean exist = false;
    for (AppointmentMemberProjection member: members) {
      if (member.getUserId().equals(userId)) {
        exist = true;
        break;
      }
    }

    if (!exist) {
      throw new UnauthorizedAppointmentAccessException();
    }
  }

  private String getSoundAccessUrl(String snackSoundObjectKey) {
    if (Objects.isNull(snackSoundObjectKey)) {
      return null;
    }

    return presignedUrlPort.getAccessUrl(snackSoundObjectKey);
  }

  private String getImageAccessUrl(String snackImageObjectKey) {
    return presignedUrlPort.getAccessUrl(snackImageObjectKey);
  }

  private void validateImageOwnership(String userId, String snackImageObjectKey) {

    String imageOwnerId = fileOwnerCachePort.getOwnerId(snackImageObjectKey);

    fileOwnerCachePort.delete(snackImageObjectKey);

    if (!userId.equals(imageOwnerId)) {
      throw new InvalidFileOwnerException();
    }
  }
  private void validateSoundOwnership(String userId, String snackSoundObjectKey) {

    String soundOwnerId = fileOwnerCachePort.getOwnerId(snackSoundObjectKey);

    fileOwnerCachePort.delete(snackSoundObjectKey);

    if (!userId.equals(soundOwnerId)) {
      throw new InvalidFileOwnerException();
    }
  }

}
