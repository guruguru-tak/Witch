package com.ssafy.witch.snack;

import static org.mockito.Mockito.any;

import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.exception.snack.UnauthorizedSnackAccessException;
import com.ssafy.witch.snack.command.SnackDeleteCommand;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class DeleteSnackServiceTest {

  @InjectMocks
  private DeleteSnackService sut;

  @Mock
  private SnackPort snackPort;

  @Test
  void 존재하지_않는_스낵을_삭제할_수_없다() {
    //given
    String snackId = "test-snack-id";
    String userId = "test-user-id";

    BDDMockito
        .given(snackPort.findById(any()))
        .willReturn(Optional.empty());

    //when&then
    Assertions.assertThatThrownBy(() -> {
      SnackDeleteCommand command = new SnackDeleteCommand(userId, snackId);
      sut.deleteSnack(command);
    }).isInstanceOf(SnackNotFoundException.class);
  }

  @Test
  void 스낵을_생성한_사용자가_아니면_스낵을_삭제할_수_없다() {
    //given
    String snackId = "test-snack-id";
    String userId = "test-user-id";
    String fakeId = "fake-user-id";

    Snack snack = new Snack(
        snackId,
        "test-appointment-id",
        userId,
        37.2,
        138.13,
        "http://test.image.url",
        "http://test.sound.url",
        LocalDateTime.parse("2025-02-14T14:21:24")
    );

    BDDMockito
        .given(snackPort.findById(any()))
        .willReturn(Optional.of(snack));

    BDDMockito
        .given(snackPort.isOwnerByUserIdAndSnackId(fakeId, snackId))
        .willReturn(false);

    //when&then
    Assertions.assertThatThrownBy(() -> {
      SnackDeleteCommand command = new SnackDeleteCommand(fakeId, snackId);
      sut.deleteSnack(command);
    }).isInstanceOf(UnauthorizedSnackAccessException.class);
  }

  @Test
  void 사용자는_스낵을_삭제할_수_있다() {
    //given
    String snackId = "test-snack-id";
    String userId = "test-user-id";

    Snack snack = new Snack(
        snackId,
        "test-appointment-id",
        userId,
        37.2,
        138.13,
        "http://test.image.url",
        "http://test.sound.url",
        LocalDateTime.parse("2025-02-14T14:21:24")
    );

    BDDMockito
        .given(snackPort.findById(any()))
        .willReturn(Optional.of(snack));

    BDDMockito
        .given(snackPort.isOwnerByUserIdAndSnackId(userId, snackId))
        .willReturn(true);

    //when&then
    Assertions.assertThatCode(() -> {
      SnackDeleteCommand command = new SnackDeleteCommand(userId, snackId);
      sut.deleteSnack(command);
    }).doesNotThrowAnyException();

    BDDMockito.then(snackPort).should().deleteById(snackId);
  }
}