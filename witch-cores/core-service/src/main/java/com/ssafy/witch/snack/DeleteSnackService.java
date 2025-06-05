package com.ssafy.witch.snack;

import com.ssafy.witch.exception.snack.SnackNotFoundException;
import com.ssafy.witch.exception.snack.UnauthorizedSnackAccessException;
import com.ssafy.witch.snack.command.SnackDeleteCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteSnackService implements DeleteSnackUseCase {

  private final SnackPort snackPort;


  @Transactional
  @Override
  public void deleteSnack(SnackDeleteCommand command) {
    String userId = command.getUserId();
    String snackId = command.getSnackId();

    Snack snack = snackPort.findById(snackId).orElseThrow(SnackNotFoundException::new);

    validateSnackOwner(userId, snackId);
    snackPort.deleteById(snackId);
  }

  private void validateSnackOwner(String userId, String snackId) {
    if (!snackPort.isOwnerByUserIdAndSnackId(userId, snackId)) {
      throw new UnauthorizedSnackAccessException();
    }
  }
}
