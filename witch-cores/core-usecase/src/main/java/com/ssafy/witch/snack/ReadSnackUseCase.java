package com.ssafy.witch.snack;

import com.ssafy.witch.snack.output.SnackDetailOutput;
import com.ssafy.witch.snack.output.SnackListOutput;

public interface ReadSnackUseCase {

  SnackDetailOutput getSnackDetail(String userId, String snackId);

  SnackListOutput getSnacks(String userId, String appointmentId);
}
