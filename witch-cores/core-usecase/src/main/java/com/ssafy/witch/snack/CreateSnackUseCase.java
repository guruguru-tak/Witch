package com.ssafy.witch.snack;

import com.ssafy.witch.snack.command.SnackCreateCommand;

public interface CreateSnackUseCase {

  void createSnack(SnackCreateCommand command);
}
