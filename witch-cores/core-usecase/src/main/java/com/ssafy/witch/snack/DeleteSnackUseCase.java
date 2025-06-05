package com.ssafy.witch.snack;

import com.ssafy.witch.snack.command.SnackDeleteCommand;

public interface DeleteSnackUseCase {

  void deleteSnack(SnackDeleteCommand command);

}
