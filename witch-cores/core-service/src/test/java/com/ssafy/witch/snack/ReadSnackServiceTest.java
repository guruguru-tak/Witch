package com.ssafy.witch.snack;

import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ssafy.witch.apoointment.AppointmentMemberPort;
import com.ssafy.witch.apoointment.AppointmentPort;
import com.ssafy.witch.apoointment.OnGoingAppointmentCachePort;
import com.ssafy.witch.apoointment.model.AppointmentDetailProjection;
import com.ssafy.witch.apoointment.model.AppointmentMemberProjection;
import com.ssafy.witch.appointment.AppointmentStatus;
import com.ssafy.witch.exception.appointment.AppointmentNotFoundException;
import com.ssafy.witch.exception.appointment.UnauthorizedAppointmentAccessException;
import com.ssafy.witch.exception.snack.NotOngoingAppointmentException;
import com.ssafy.witch.snack.mapper.SnackOutputMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ReadSnackServiceTest {

  @InjectMocks
  private ReadSnackService sut;

  @Mock
  private AppointmentPort appointmentPort;

  @Mock
  private AppointmentMemberPort appointmentMemberPort;

  @Mock
  private OnGoingAppointmentCachePort onGoingAppointmentCachePort;

  @Mock
  private SnackReadPort snackReadPort;

  @Mock
  private SnackOutputMapper snackOutputMapper;

  @Test
  void 존재하지_않는_약속의_스낵_목록을_조회할_수_없다() {
    //given
    String userId = "test-user-id";
    String appointmentId = "test-appointment-id";

    given(appointmentPort.existsById(appointmentId))
        .willReturn(false);

    //whe&then
    Assertions.assertThatThrownBy(() -> {
      sut.getSnacks(userId, appointmentId);
    }).isInstanceOf(AppointmentNotFoundException.class);
  }

  @Test
  void 진행_중인_약속이_아닌_경우_스낵을_조회할_수_없다() {
    //given
    String userId = "test-user-id";
    String appointmentId = "test-appointment-id";

    given(appointmentPort.existsById(appointmentId))
        .willReturn(true);

    given(onGoingAppointmentCachePort.get(appointmentId))
        .willReturn(null);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      sut.getSnacks(userId, appointmentId);
    }).isInstanceOf(NotOngoingAppointmentException.class);
  }

  @Test
  void 사용자가_해당_약속의_멤버가_아닌_경우_스낵을_조회할_수_없다() {
    //given
    String userId = "test-user-id";
    String appointmentId = "test-appointment-id";

    AppointmentDetailProjection appointment =
        new AppointmentDetailProjection(
            appointmentId,
            "윗치 마지막 회식",
            AppointmentStatus.ONGOING,
            "회식",
            LocalDateTime.now(),
            "경북 구미시 인동중앙로11길 7-1",
            36.145,
            128.395,
            List.of(
                new AppointmentMemberProjection(
                    "test-user-1",
                    "야채용수",
                    "http://test.url/profile1.png",
                    true,
                    "fcmToken1"
                ),
                new AppointmentMemberProjection(
                    "test-user-2",
                    "duckcode",
                    "http://test.url/profile2.png",
                    false,
                    "fcmToken2"
                )
            )
        );

    given(appointmentPort.existsById(appointmentId))
        .willReturn(true);

    given(onGoingAppointmentCachePort.get(appointmentId))
        .willReturn(appointment);

    given(appointmentMemberPort.existsByUserIdAndAppointmentId(userId, appointmentId))
        .willReturn(false);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      sut.getSnacks(userId, appointmentId);
    }).isInstanceOf(UnauthorizedAppointmentAccessException.class);
  }

  @Test
  void 해당_약속의_스낵_목록을_조회할_수_있다() {
    //given
    String userId = "test-user-id";
    String appointmentId = "test-appointment-id";

    AppointmentDetailProjection appointment =
        new AppointmentDetailProjection(
            appointmentId,
            "윗치 마지막 회식",
            AppointmentStatus.ONGOING,
            "회식",
            LocalDateTime.now(),
            "경북 구미시 인동중앙로11길 7-1",
            36.145,
            128.395,
            List.of(
                new AppointmentMemberProjection(
                    userId,
                    "야채용수",
                    "http://test.url/profile1.png",
                    true,
                    "fcmToken1"
                ),
                new AppointmentMemberProjection(
                    "test-user-2",
                    "duckcode",
                    "http://test.url/profile2.png",
                    false,
                    "fcmToken2"
                )
            )
        );

    given(appointmentPort.existsById(appointmentId))
        .willReturn(true);

    given(onGoingAppointmentCachePort.get(appointmentId))
        .willReturn(appointment);

    given(appointmentMemberPort.existsByUserIdAndAppointmentId(userId, appointmentId))
        .willReturn(true);

    //when&then
    Assertions.assertThatCode(() -> {
      sut.getSnacks(userId, appointmentId);
    }).doesNotThrowAnyException();

    then(snackReadPort).should().getSnacks(userId, appointmentId);
    then(snackOutputMapper).should().toOutput(anyList());
  }


}