package com.ssafy.witch.repository.snack;

import com.ssafy.witch.entity.Snack.SnackDetailEntityProjection;
import com.ssafy.witch.entity.Snack.SnackEntityProjection;
import java.util.List;

public interface SnackCustomRepository {

  boolean isOwnerByUserIdAndSnackId(String userId, String snackId);

  List<SnackEntityProjection> getSnacks(String userId, String appointmentId);

}
